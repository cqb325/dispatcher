/**
 * FileName:CharacterEncodingFilter.java
 * @author cqb
 */
package com.cqb.encoding;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter sets the character encoding to be used for current
 * request and response.
 * @author cqb
 *
 */
public class CharacterEncodingFilter implements Filter {
	
	//要设置的编码格式
	private String encoding = null;
	
	//web.xml中配置的Filter
	protected FilterConfig filterConfig = null;
	
	/**
	 * 销毁回收
	 */
	public void destroy() {
		this.encoding = null;
        this.filterConfig = null;
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		System.out.println("encoding: "+encoding);
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		getConfigEncode();
	}

	private void getConfigEncode() {
		if(filterConfig == null){
			return ;
		}
		String configencode = filterConfig.getInitParameter("encoding");
		System.out.println("encoding:"+configencode);
		if(configencode == null || configencode.equals("")){
			encoding = "UTF-8";
		}else{
			encoding = configencode;
		}
	}

}
