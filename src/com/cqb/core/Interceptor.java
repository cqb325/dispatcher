package com.cqb.core;

import com.cqb.action.IActionInvocation;

public interface Interceptor {
	
	/**
	 * interceptor ������Actionִ�й����н�������
	 * @param actionInvocation
	 * @return
	 * @throws Exception
	 */
	public String intercept(IActionInvocation actionInvocation, InterceptorChain chain) throws Exception;
}
