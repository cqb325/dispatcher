package com.cqb.action;

import com.cqb.core.ApplicationMap;
import com.cqb.core.ContextPvd;
import com.ioc.annotation.Server;

@Server(serverName="testaction")
public class TestAction {
	private String name;
	private int num;
	
	private int test = 1;
	public String test(){
		ContextPvd ctx = ApplicationMap.getContextPvd(this);
		System.out.println(ctx.getRequest().getParameter("num")+">>>>");
		System.out.println(Thread.currentThread().getId());
//		System.out.println("name : "+name);
//		System.out.println("num : "+num);
//		System.out.println(Thread.currentThread().getId());
		System.out.println(++test);
		return "success";
	}
}
