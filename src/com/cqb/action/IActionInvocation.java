package com.cqb.action;

import java.lang.reflect.Method;


public interface IActionInvocation {
	/**
	 * ִ��action����
	 * @return
	 */
	public String doInvocation();
	
	/**
	 * ��ȡ����ִ�е�action������
	 * @return	����
	 */
	public String getActionName();
	
	/**
	 * ��ȡ����ִ�е�action����
	 * @return	ִ�з�������
	 */
	public String getMethodName();
	
	/**
	 * ��ȡ����ִ�е�action������
	 * @return	����
	 */
	public Object getAction();
	
	/**
	 * ��ȡ����ִ�е�action����
	 * @return	ִ�з�������
	 */
	public Method getMethod();
	
	public boolean isBREAK();
	
	public void setBREAK(boolean bREAK);
}
