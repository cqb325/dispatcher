package com.cqb.action;

import com.cqb.annotation.AutoForm;
import com.ioc.annotation.Server;

@Server(serverName="formfieldaction")
public class FormFieldTestAction {
	
	@AutoForm
	private User user;
	
	private String name1;
	
	public String test(){
		System.out.println("------------------------------------------------------");
		System.out.println("name1: "+name1);
		System.out.println("num: "+user.getNum());
		return "success";
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}
}
