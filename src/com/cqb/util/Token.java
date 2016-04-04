package com.cqb.util;

import java.util.UUID;

public class Token {
	
	public static String generateUniqueToken(){
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}
}
