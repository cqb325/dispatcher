package com.cqb.action;

import java.io.IOException;

import com.cqb.core.ApplicationMap;
import com.cqb.core.ContextPvd;
import com.cqb.core.Interceptor;
import com.cqb.core.InterceptorChain;


public class TestInterceptor implements Interceptor {

	@Override
	public String intercept(IActionInvocation actioninvocation, InterceptorChain chain) {
		System.out.println("start interceptor ...");
		System.out.println(actioninvocation.getActionName());
		
		ContextPvd ctx = ApplicationMap.getContextPvd(actioninvocation.getAction());
		System.out.println(ctx.getRequest().getRequestURL().toString());
		Object obj = ctx.getSessionAttr("admin");
		String result = null;
		if(obj == null){
			try {
				ctx.getResponse().sendRedirect("index.jsp");
				actioninvocation.setBREAK(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				result = chain.intercept(actioninvocation, chain);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("end interceptor ...");
		
		return result;
	}

}
