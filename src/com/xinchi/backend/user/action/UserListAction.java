package com.xinchi.backend.user.action;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserList;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserListAction extends BaseAction {
	private static final long serialVersionUID = 8172970461903661720L;

	private List<UserSessionBean> users;

	public String onlineUsers() {
		users = UserList.getInstance().getUserList();
		return SUCCESS;
	}

	private String current_url;

	public String updateUserCurrentPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		UserSessionBean user = UserList.getInstance().getUser(sessionBean.getPk());
		if (null != user) {
			user.setCurrent_url(current_url);
		}

		resultStr = SUCCESS;
		return SUCCESS;
	}

	public List<UserSessionBean> getUsers() {
		return users;
	}

	public void setUsers(List<UserSessionBean> users) {
		this.users = users;
	}

	public String getCurrent_url() {
		return current_url;
	}

	public void setCurrent_url(String current_url) {
		this.current_url = current_url;
	}

}
