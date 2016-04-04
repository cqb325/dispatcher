package com.cqb.core;

import java.util.ArrayList;
import java.util.List;

import com.cqb.action.IActionInvocation;

public class InterceptorChain implements Interceptor{
	
	private List<Interceptor> stack = null;
	
	private int currentIndex = -1;

	public InterceptorChain(){
		stack = new ArrayList<Interceptor>();
	}
	
	/**
	 * Ìí¼ÓÀ¹½ØÆ÷
	 * @param interceptor
	 */
	public InterceptorChain addInterceptor(Interceptor interceptor){
		stack.add(interceptor);
		return this;
	}
	
	
	public String intercept(IActionInvocation actionInvocation,
			InterceptorChain chain) throws Exception {
		if(currentIndex == stack.size() -1){
			return actionInvocation.doInvocation();
		}
		currentIndex++;
		
		Interceptor interceptor = stack.get(currentIndex);
		String result = interceptor.intercept(actionInvocation, chain);
		return result;
	}
}
