package com.cqb.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.cqb.annotation.AutoForm;
import com.cqb.core.ApplicationMap;
import com.cqb.core.ContextPvd;
import com.cqb.core.ContextPvdImpl;
import com.cqb.core.Interceptor;
import com.cqb.core.InterceptorChain;
import com.ioc.core.BeanFactory;

import cqb.config.ActionPropertyManager;
import cqb.config.InterceptorPropertyManager;
import cqb.config.PropertyManager;

public class DispatcherAction implements ActionChain {
	
	private static final String FILEUPLOADENCOING = "UTF-8";
	
	private Logger logger = Logger.getLogger(DispatcherAction.class);
	
	//在内存中的最大容量
	private static int SizeThreshold = 4096;
	//可以上传的最大容量
	private static int UPLOADFILEMAXSIZE = 2048000;
	
	public DispatcherAction(){
		String size = PropertyManager.getPeroperty("UPLOADFILEMAXSIZE");
		if(size != null){
			UPLOADFILEMAXSIZE = Integer.parseInt(size);
		}
	}
	
	
	public String doTask(HttpServletRequest req, HttpServletResponse resp, String action, String method, ActionChain chain) {
		
		String path = null;
		Object actionClass = BeanFactory.getAction(action);
		
		ContextPvd ctx = new ContextPvdImpl(req, resp);
		ApplicationMap.addAction(actionClass, ctx);
		
		if(actionClass == null){
			throw new RuntimeException(action+"没有配置@Server");
		}else{
			try {
				//设置好页面传送的参数
				setFields(actionClass,req);
				
				Class<?> clazz = actionClass.getClass();
				//获取执行的方法名称
				Method excutemethod = clazz.getMethod(method);
				//执行方法，返回结果
//				String result = (String)excutemethod.invoke(actionClass);
				IActionInvocation actioninvocation = new ActionInvocation(actionClass, excutemethod);
				String result = doInterceptors(actioninvocation);
				
				if(result == null){
					//请求被截断
					if(actioninvocation.isBREAK()){
						return result;
					}
					throw new RuntimeException("返回结果视图不能为空");
				}
				
				if(ActionPropertyManager.isForward(action, method, result)){
					path = ActionPropertyManager.getForward(action, method, result);
//					req.getRequestDispatcher(path).forward(req, resp);
				}
				
				else if(ActionPropertyManager.isLink(action, method, result)){
					String linkaction = ActionPropertyManager.getlink(action, method, result).getActions();
					String linkmethod = ActionPropertyManager.getlink(action, method, result).getAction();
					//如果指向另一个action则递归调用该方法
					path = chain.doTask(req, resp, linkaction, linkmethod, chain);
				}
				
				else{
					throw new RuntimeException("返回视图"+result+"没有正确配置!");
				}
				ApplicationMap.pop(actionClass);
				
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return path;
	}
	
	private String doInterceptors(IActionInvocation actioninvocation) throws Exception {
		InterceptorChain chain = new InterceptorChain();
		//do some add interceptors
//		chain.addInterceptor(new TestInterceptor()).addInterceptor(new TestInterceptor2());
		List<Object> interceptors = InterceptorPropertyManager.getInterceptors();
		for(Object interceptor : interceptors){
			if(interceptor instanceof Interceptor){
				chain.addInterceptor((Interceptor)interceptor);
			}
		}
		String result = chain.intercept(actioninvocation, chain);
		return result;
	}


	/**
	 * 设置action中的属性，如果跟页面参数名称相同则设置该值
	 * @param actionClass
	 * @param req
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private Object setFields(Object actionClass, HttpServletRequest req) throws IllegalArgumentException, IllegalAccessException{
		Class<?> clazz = actionClass.getClass();
		Field[] fields = clazz.getDeclaredFields();
		String contenttype = req.getContentType();
		if(contenttype == null || contenttype.indexOf("multipart/form-data") < 0){
			Enumeration<?> paramnames = req.getParameterNames();
			
			//创建具有AutoForm注解的字段对象
			Map<String, Object> formfields = new HashMap<String, Object>();
			for(Field field : fields){
				if(field.isAnnotationPresent(AutoForm.class)){
					Class<?> fieldType = field.getType();
					String fieldname = field.getName();
					String methodname = "set"+fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
					Object obj;
					
					try {
						obj = fieldType.newInstance();
						invokeMethod(methodname, actionClass, obj);
						//不安全 弃用
//						field.setAccessible(true);
//						field.set(actionClass, obj);
						formfields.put(field.getName(), obj);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				}
			}
			
			while(paramnames.hasMoreElements()){
				String paramname = (String)paramnames.nextElement();
				logger.info("参数名称:"+paramname);
				
				for(Field field : fields){
					if(paramname.equals(field.getName())){
						String methodname = "set"+paramname.substring(0, 1).toUpperCase() + paramname.substring(1);
						Object value = getTypeVlaue(field.getType(),req.getParameter(field.getName()), req.getMethod());
						invokeMethod(methodname, actionClass, value);
						//不安全 弃用
//						field.setAccessible(true);
//						field.set(actionClass, value);
					}
				}
				
				for(Iterator<String> iter=formfields.keySet().iterator(); iter.hasNext();){
					String autofieldname = iter.next();
					try {
						Object type = formfields.get(autofieldname);
						Class<?> clacc = type.getClass();
						Field[] autofields = clacc.getDeclaredFields();
						for(Field paramfield : autofields){
							if(paramfield.getName().equals(paramname)){
								String filedname = paramfield.getName();
								String methodname = "set"+filedname.substring(0, 1).toUpperCase() + filedname.substring(1);
								invokeMethod(methodname, type, getTypeVlaue(paramfield.getType(), req.getParameter(paramname), req.getMethod()));
								
								break;
							}
						}
						
						//不安全 弃用
//						设置可以通行
//						paramfield.setAccessible(true);
//						paramfield.set(type, getTypeVlaue(paramfield.getType(), req.getParameter(paramname), req.getMethod()));
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			doUniqueTask(actionClass,req);
		}
		
		return actionClass;
	}
	
	/**
	 * 从一个类中获取方法
	 * @param methodname
	 * @param clazz
	 * @return
	 */
	private Method getMethod(String methodname, Class<?> clazz){
		Method[] methods = clazz.getDeclaredMethods();
		for(Method method : methods){
			if(method.getName().equals(methodname)){
				return method;
			}
		}
		return null;
	}
	
	/**
	 * 从一个类中获取方法
	 * @param methodname
	 * @param clazz
	 * @return
	 */
	private Method getMethod(String methodname, Object clazz){
		return getMethod(methodname, clazz.getClass());
	}
	
	/**
	 * 调用方法
	 * @param method
	 * @param instance
	 * @param paramTypes
	 */
	private void invokeMethod(String methodname, Object instance, Object... params){
		try {
			Method method = getMethod(methodname, instance);
			if(method != null){
				invokeMethod(method, instance, params);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 调用方法
	 * @param method
	 * @param instance
	 * @param paramTypes
	 */
	private void invokeMethod(Method method, Object instance, Object... params){
		try {
			method.invoke(instance, params);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 页面传送过来的参数的类型转换
	 * @param type
	 * @param paramvalue
	 * @return
	 */
	private Object getTypeVlaue(Class<?> type, String paramvalue, String method){
		Object value = null;
		if(type.equals(Integer.class) || type.equals(int.class)){
			value = Integer.parseInt(paramvalue);
		}else if(type.equals(Double.class) || type.equals(double.class)){
			value = Double.parseDouble(paramvalue);
		}else if(type.equals(Float.class) || type.equals(float.class)){
			value = Float.parseFloat(paramvalue);
		}else if(type.equals(Boolean.class) || type.equals(boolean.class)){
			value = Boolean.parseBoolean(paramvalue);
		}else if(type.equals(String.class)){
			if(method.equalsIgnoreCase("GET")){
				try {
					value = URLDecoder.decode(paramvalue, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}else{
				value = paramvalue;
			}
		}
		
		return value;
	}
	
	/**
	 * 上传文件
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doUniqueTask(Object actionClass, HttpServletRequest req) {
		// 工厂产生一系列产品的类
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// 文件上传
		ServletFileUpload upload;
		
		// maximum size that will be stored in memory
		factory.setSizeThreshold(SizeThreshold);

		upload = new ServletFileUpload(factory);
		// maximum size before a FileUploadException will be thrown
		upload.setSizeMax(UPLOADFILEMAXSIZE);
		
		Class<?> clazz = actionClass.getClass();
		Field[] fields = clazz.getDeclaredFields();
		try {
			List<?> fileItems = upload.parseRequest(req);
			Iterator<?> iter = fileItems.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if(item.isFormField()){
					for(Field field : fields){
						if(item.getFieldName().equals(field.getName())){
							String fieldname = field.getName();
							String methodname = "set"+fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
							Object value = getTypeVlaue(field.getType(),item.getString(FILEUPLOADENCOING), req.getMethod());
							
							invokeMethod(methodname, actionClass, value);
							//不安全 弃用
//							field.setAccessible(true);
//							field.set(actionClass, value);
						}
					}
				}
				if (!item.isFormField()) {
					String name = item.getFieldName();
					Field field = clazz.getDeclaredField(name);
					if(field == null){
						logger.warn(clazz.getName()+"中没有"+name+"属性");
						continue;
					}
					if(!field.getType().equals(FormFile.class)){
						logger.warn(name+"属性的类型应该是"+FormFile.class.getName());
						continue;
					}
					InputStream is = item.getInputStream();
					FormFile file = new FormFile();
					file.setInputStream(is);
					file.setName(item.getName());
					file.setSize(item.getSize());
					
					String fieldname = field.getName();
					String methodname = "set"+fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
					invokeMethod(methodname, actionClass, file);
					//不安全 弃用
//					field.setAccessible(true);
//					field.set(actionClass, file);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
