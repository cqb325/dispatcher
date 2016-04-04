package com.cqb.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionInvocation implements IActionInvocation{
	
	//Action类
	private Object action;
	
	//action执行方法
	private Method method;
	
	//截断
	private boolean BREAK;
	
	public boolean isBREAK() {
		return BREAK;
	}

	public void setBREAK(boolean bREAK) {
		BREAK = bREAK;
	}

	public ActionInvocation(){
		
	}
	
	/**
	 * 构造函数
	 * @param action
	 * @param method
	 */
	public ActionInvocation(Object action, Method method){
		this.action = action;
		this.method = method;
	}
	
	public String doInvocation(){
		if(action == null){
			throw new RuntimeException("指定的action为空");
		}
		if(method == null){
			throw new RuntimeException("指定的method为空不能正确执行");
		}
		//do other
		String result = null;
		try {
			result = (String)method.invoke(action);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Object getAction() {
		return action;
	}

	public void setAction(Object action) {
		this.action = action;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getActionName() {
		return action.getClass().getName();
	}

	public String getMethodName() {
		return method.getName();
	}
	
}
