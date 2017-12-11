package com.xinchi.common;

public class SimpletinyUser {

	private UserSessionBean user;

	public SimpletinyUser() {
		this.user = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

	}

	public UserSessionBean getUser() {
		return user;
	}

	public void setUser(UserSessionBean user) {
		this.user = user;
	}

}
