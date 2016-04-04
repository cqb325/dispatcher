package com.cqb.action;

import java.lang.reflect.Method;


public interface IActionInvocation {
	/**
	 * 执行action请求
	 * @return
	 */
	public String doInvocation();
	
	/**
	 * 获取正在执行的action的类名
	 * @return	类名
	 */
	public String getActionName();
	
	/**
	 * 获取正在执行的action方法
	 * @return	执行方法名称
	 */
	public String getMethodName();
	
	/**
	 * 获取正在执行的action的类名
	 * @return	类名
	 */
	public Object getAction();
	
	/**
	 * 获取正在执行的action方法
	 * @return	执行方法名称
	 */
	public Method getMethod();
	
	public boolean isBREAK();
	
	public void setBREAK(boolean bREAK);
}
