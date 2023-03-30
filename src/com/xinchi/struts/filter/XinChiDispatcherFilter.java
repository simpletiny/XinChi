package com.xinchi.struts.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

/**
 * 新道struts2DispatcherFilter
 * 
 * @author niushixing 2015-1-23 10:45:03
 * 
 *         此类用来处理一些Struts2之上的servlet，一些非Action
 *         servlet需要保持原有的Request而不需要struts2的封装和过滤
 */
public class XinChiDispatcherFilter extends StrutsPrepareAndExecuteFilter {
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		String URI = request.getRequestURI();

		// 静态资源处理
		if (URI.indexOf("static") >= 0) {
			chain.doFilter(req, res);
		}
		// 处理kindeditor的servlet请求
		// else if (URI.indexOf("kindeditor") >= 0) {
		// chain.doFilter(new StrutsRequestWrapper((HttpServletRequest) req),
		// res);
		// }
		else {
			super.doFilter(req, res, chain);
		}
	}
}
