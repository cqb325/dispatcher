package com.cqb.core;

import java.util.HashMap;
import java.util.Map;

public class ApplicationMap {
	
	private static ApplicationMap applicationMap;
	
	private static Map<Object,ContextPvd> actionmap = new HashMap<Object, ContextPvd>();
	
	private ApplicationMap(){}
	
	public synchronized static ApplicationMap getInstance(){
		if(applicationMap == null){
			applicationMap = new ApplicationMap();
		}
		return applicationMap;
	}
	
	public static void addAction(Object action, ContextPvd contextPvd){
		actionmap.put(action, contextPvd);
	}
	
	public static ContextPvd getContextPvd(Object action){
		return actionmap.get(action);
	}
	
	public static void pop(Object action){
		actionmap.remove(action);
	}
}
