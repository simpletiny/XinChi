package com.xinchi.common;

public class SimpletinyUser {
	public static UserSessionBean user() {
		return (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
	}

	public static boolean isAdmin() {
		return user().getUser_roles().contains(ResourcesConstants.USER_ROLE_ADMIN);
	}
}
