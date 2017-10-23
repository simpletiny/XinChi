package com.xinchi.common;

public class SimpletinyUser {

	public static UserSessionBean user = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

	public static String getId() {
		return user.getId();
	}

	public static String getUser_number() {
		return user.getUser_number();
	}

	public static String getPk() {
		return user.getPk();
	}

	public static String getUser_name() {
		return user.getUser_name();
	}

	public static String getNick_name() {
		return user.getNick_name();
	}

	public static String getCellphone() {
		return user.getCellphone();
	}

	public static String getUser_status() {
		return user.getUser_status();
	}

	public static String getUser_roles() {
		return user.getUser_roles();
	}
}
