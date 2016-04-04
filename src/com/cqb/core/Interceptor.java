package com.cqb.core;

import com.cqb.action.IActionInvocation;

public interface Interceptor {
	
	/**
	 * interceptor 可以在Action执行过程中进行拦截
	 * @param actionInvocation
	 * @return
	 * @throws Exception
	 */
	public String intercept(IActionInvocation actionInvocation, InterceptorChain chain) throws Exception;
}
