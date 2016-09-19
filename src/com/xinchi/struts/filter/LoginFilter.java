package com.xinchi.struts.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xinchi.common.DictionaryEnum;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.tools.PropertiesUtil;

/**
 * 登陆过滤器
 * 
 * @author niushixing 2014-12-1 上午9:55:01
 * 
 */
public class LoginFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = 2781540410299414055L;
	private String[] escapeUrl;
	private String[] escapeSysUrl;

	@Override
	public void doFilter(ServletRequest srquest, ServletResponse sresponse, FilterChain fc) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) srquest;
		HttpServletResponse response = (HttpServletResponse) sresponse;
		HttpSession session = request.getSession();
		UserSessionBean sessionBean = (UserSessionBean) session.getAttribute(ResourcesConstants.LOGIN_SESSION_KEY);
		String url = request.getServletPath();
		String contextPath = request.getContextPath();
		
		if(url.indexOf("/admin/") >= 0 || url.indexOf("/sys/") >= 0 ){
			if(isEscapseForSys(url)){
				fc.doFilter(request, response);
			}else if (isNotControlUrl(url)) {
				fc.doFilter(request, response);
			}else{
				if(sessionBean == null || !DictionaryEnum.ADMIN_TYPE.getStringValue().equals(String.valueOf(sessionBean.getCellphone()))){
					response.sendRedirect(contextPath + "/admin/loginsys/adminlogin.jsp");
					return;
				}
				fc.doFilter(request, response);
			}
		}else{
			// 如果是XMLHttpRequest则为Ajax请求
//			String isAjax = request.getHeader("x-requested-with");
			if (isEscapse(url)) {
				fc.doFilter(request, response);
			} else if (isNotControlUrl(url)) {
				fc.doFilter(request, response);
			} else {
				if (sessionBean == null) {
					response.sendRedirect(contextPath + "/templates/users/login.jsp");
					return;
				}
				fc.doFilter(request, response);
			}
		}
		// fc.doFilter(srquest, sresponse);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		String escape = PropertiesUtil.getProperty("escapeUrl");
		String escapeSys = PropertiesUtil.getProperty("escapeSysUrl");
		if (null != escape && escape.trim().length() > 0) {
			this.escapeUrl = escape.split(";");
			this.escapeSysUrl = escapeSys.split(";");
		} else {
			this.escapeUrl = new String[0];
			this.escapeSysUrl = new String[0];
		}
	}

	/**
	 * 判断是否逃脱过滤
	 * 
	 * @param url
	 * @return
	 */
	private boolean isEscapse(String url) {
		if (url == null || url.trim().length() == 0)
			return false;
		for (String escape : this.escapeUrl) {
			if (url.indexOf(escape) != -1) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断是否逃脱过滤
	 * 
	 * @param url
	 * @return
	 */
	private boolean isEscapseForSys(String url) {
		if (url == null || url.trim().length() == 0)
			return false;
		for (String escape : this.escapeSysUrl) {
			if (url.indexOf(escape) != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否属于Action和jsp请求
	 * 
	 * @param url
	 * @return boolean true 不属于，false属于
	 */
	private boolean isNotControlUrl(String url) {
		int dot = url.lastIndexOf('.');
		String extension = "";
		if ((dot > -1) && (dot < (url.length()))) {
			extension = url.substring(dot, url.length());
		}
		if (extension.trim().equals("") || extension.trim().toLowerCase().equals(".jsp"))
			return false;
		return true;
	}
}
