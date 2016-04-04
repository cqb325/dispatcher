package com.cqb.core;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ContextPvdImpl implements ContextPvd {
	
	private HttpServletRequest request;
	
	@SuppressWarnings("unused")
	private HttpServletResponse response;
	
	public ContextPvdImpl(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	public String getAppRealPath(String path) {
		return getServletContext().getRealPath(path);
	}

	private ServletContext getServletContext() {
		return getRequest().getSession().getServletContext();
	}

	public String getAppRoot() {
		return getAppRealPath("/");
	}

	public String getAppCxtPath() {
		return getRequest().getContextPath();
	}

	public int getServerPort() {
		return getRequest().getServerPort();
	}

	public void logout() {
		HttpSession session = getRequest().getSession(
				false);
		if (session != null) {
			session.invalidate();
		}
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Object getRequestAttr(String key) {
		return getRequest().getAttribute(key);
	}

	public void setRequestAttr(String key, Object value) {
		getRequest().setAttribute(key, value);
	}

	public Object getSessionAttr(String key) {
		HttpSession session = getRequest().getSession(
				false);
		if (session == null) {
			return null;
		} else {
			return session.getAttribute(key);
		}
	}

	public void setSessionAttr(String key, Object value) {
		HttpSession session = getRequest().getSession();
		session.setAttribute(key, value);
	}

	public void removeAttribute(String key) {
		HttpSession session = getRequest().getSession();
		session.removeAttribute(key);
	}

	public Object getApplicationAttr(String key) {
		return getServletContext().getAttribute(key);
	}

	public void setApplicationAttr(String key, Object value) {
		getServletContext().setAttribute(key, value);
	}

	public String getSessionId(boolean isCreate) {
		HttpSession session = getRequest().getSession(
				isCreate);
		if (session == null) {
			return null;
		} else {
			return session.getId();
		}
	}

	public String getRemoteIp() {
		return getRequest().getRemoteAddr();
	}

	public int getRemotePort() {
		return getRequest().getRemotePort();
	}

	public String getRequestURL() {
		return getRequest().getRequestURL().toString();
	}

	public String getRequestBrowser() {
		String userAgent = getRequestUserAgent();
		String[] agents = userAgent.split(";");
		if (agents.length > 1) {
			return agents[1].trim();
		} else {
			return null;
		}
	}

	public String getRequestOs() {
		String userAgent = getRequestUserAgent();
		String[] agents = userAgent.split(";");
		if (agents.length > 2) {
			return agents[2].trim();
		} else {
			return null;
		}
	}

	public String getRequestUserAgent() {
		HttpServletRequest req = getRequest();
		String userAgent = req.getHeader("user-agent");
		return userAgent;
	}

	public void addCookie(Cookie cookie) {
		getResponse().addCookie(cookie);
	}

	public Cookie getCookie(String name) {
		Cookie[] cookies = getRequest().getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}

	public boolean isMethodPost() {
		String method = getRequest().getMethod();
		if ("post".equalsIgnoreCase(method)) {
			return true;
		} else {
			return false;
		}
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getRequestParam(String key) {
		return getRequest().getParameter(key);
	}
}
