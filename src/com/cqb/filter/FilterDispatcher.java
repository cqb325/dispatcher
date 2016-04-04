package com.cqb.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cqb.action.ActionChain;
import com.cqb.action.DispatcherAction;


public class FilterDispatcher implements Filter{
	
	//web.xml�����õ�Filter
	protected FilterConfig filterConfig = null;
	
	//�����action����
	private String actionname = null;
	
	//�����menthod����
	private String method = null;
	
	//action�ַ�����
	private ActionChain actionChain = null;
	
	private static boolean ISACTIONQUERY = true;
	
	public void destroy() {
		filterConfig = null;
		actionname = null;
		method = null;
		actionChain = null;
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httprequest = (HttpServletRequest)request;
		HttpServletResponse httpresponse = (HttpServletResponse)response;
		
		//�������url��ȡaction���ƺ�ִ�еķ�������
		parseUrl(httprequest);
		
		if(!ISACTIONQUERY){
			chain.doFilter(request, response);
		}else{
			
			String result = actionChain.doTask(httprequest, httpresponse, actionname, method, actionChain);
			
			if(result != null){
				httprequest.getRequestDispatcher(result).forward(httprequest, httpresponse);
			}
		}
	}
	
	/**
	 * ���������url��ȡaction���ƺ�ִ�еķ�������
	 * url����ʽΪaction!method.action
	 * @param httprequest
	 */
	private void parseUrl(HttpServletRequest httprequest) {
		String requesturl = httprequest.getRequestURL().toString();
		int index1 = requesturl.lastIndexOf("/")+1;
		int index2 = requesturl.lastIndexOf(".");
		
		if(index2 < index1){
			ISACTIONQUERY = false;
			return;
		}
		String actionmethod = requesturl.substring(index1, index2);
		int index3 = actionmethod.indexOf("!");
		if(index3 < 0){
			ISACTIONQUERY = false;
			return;
		}
		ISACTIONQUERY = true;
		actionname = actionmethod.substring(0,index3);
		method = actionmethod.substring(index3+1);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		actionChain = new DispatcherAction();
	}
}
