package com.xinchi.common;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

/**
 * 欣驰应用上下文
 * 
 * @author
 *
 */
public class XinChiApplicationContext {

	private static Map<Object, Object> context = new HashMap();

	public static Object getParameter(String key) {
		return context.get(key);
	}

	public static Object setParameter(String key, Object value) {
		return context.put(key, value);
	}

	public static Object removeParameter(String key) {
		return context.remove(key);
	}

	public static String getCurrentUser() {
		
		String pk = "";
		if (ServletActionContext.getContext() != null) {
			Map<String, Object> session = ServletActionContext.getContext().getSession();
			if (session.containsKey(ResourcesConstants.LOGIN_SESSION_KEY)) {
				pk = ((UserSessionBean) session.get(ResourcesConstants.LOGIN_SESSION_KEY)).getPk().toString();
			}
		}
		return pk;
	}

	public static void setSession(String key, Object value) {
		Map<String, Object> session = ServletActionContext.getContext().getSession();
		session.put(key, value);
	}

	public static Object getSession(String key) {
		Map<String, Object> session = ServletActionContext.getContext().getSession();
		if (session.containsKey(key)) {
			return session.get(key);
		}
		return null;
	}

	public static void removeSession(String key) {
		Map<String, Object> session = ServletActionContext.getContext().getSession();
		session.remove(key);
	}
}
