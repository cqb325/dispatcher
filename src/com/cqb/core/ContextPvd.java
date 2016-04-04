package com.cqb.core;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ContextPvd {
	/**
	 * 获得系统绝对路径 如：F:\webapps\CmsSys
	 * 
	 * @param path
	 *            可以传入空串
	 * @return
	 */
	public String getAppRealPath(String path);

	/**
	 * 获得应用绝对根路径
	 * 
	 * @return
	 */
	public String getAppRoot();

	/**
	 * 获得系统根路径 如：/CmsSys
	 * 
	 * @return
	 */
	public String getAppCxtPath();

	/**
	 * 获得应用端口号
	 * 
	 * @return
	 */
	public int getServerPort();

	/**
	 * 注销
	 * 
	 * @return
	 */
	public void logout();

	/**
	 * 获得response
	 * 
	 * @return
	 */
	public HttpServletResponse getResponse();
	
	public HttpServletRequest getRequest();
	
	
	/**
	 * 从Request中获取参数值
	 * 
	 * @param key
	 * @return
	 */
	public String getRequestParam(String key);
	
	/**
	 * 从Request的Attribute中获取值
	 * 
	 * @param key
	 * @return
	 */
	public Object getRequestAttr(String key);

	/**
	 * 给Request的Attribute中赋值
	 * 
	 * @param key
	 * @param value
	 */
	public void setRequestAttr(String key, Object value);

	/**
	 * 从SESSION中获得值
	 * 
	 * @param key
	 * @return
	 */
	public Object getSessionAttr(String key);

	/**
	 * 给session赋值
	 * 
	 * @param key
	 * @param value
	 */
	public void setSessionAttr(String key, Object value);

	/**
	 * 移除session中的属性
	 * 
	 * @param key
	 */
	public void removeAttribute(String key);

	public Object getApplicationAttr(String key);

	public void setApplicationAttr(String key, Object value);

	/**
	 * 获得sessionId
	 * 
	 * @param isCreate
	 *            如果session不存在是否创建
	 * @return
	 */
	public String getSessionId(boolean isCreate);

	/**
	 * 获得访问者IP
	 * 
	 * @return
	 */
	public String getRemoteIp();

	/**
	 * 获得访问者PORT
	 * 
	 * @return
	 */
	public int getRemotePort();

	/**
	 * 获得访问者URL
	 * 
	 * @return
	 */
	public String getRequestURL();

	/**
	 * 获得访问者浏览器
	 * 
	 * @return
	 */
	public String getRequestBrowser();

	/**
	 * 获得访问者操作系统
	 * 
	 * @return
	 */
	public String getRequestOs();

	/**
	 * 获得访问者的代理全部信息
	 * 
	 * @return
	 */
	public String getRequestUserAgent();

	/**
	 * 添加cookie
	 * 
	 * @param cookie
	 */
	public void addCookie(Cookie cookie);

	/**
	 * 获取cookie
	 * 
	 * @param name
	 * @return
	 */
	public Cookie getCookie(String name);

	/**
	 * 是否是post请求
	 * 
	 * @return
	 */
	public boolean isMethodPost();
}
