package com.cqb.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionChain {
	/**
	 * 进行action的分发
	 * @param req
	 * @param resp
	 * @param action
	 * @param method
	 * @param chain
	 */
	public String doTask(HttpServletRequest req, HttpServletResponse resp, String action, String method, ActionChain chain);
}
