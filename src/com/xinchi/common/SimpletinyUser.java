package com.xinchi.common;

public class SimpletinyUser {
	public static UserSessionBean user() {
		return (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
	}
}
