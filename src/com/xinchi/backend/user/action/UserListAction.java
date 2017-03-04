package com.xinchi.backend.user.action;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.common.BaseAction;
import com.xinchi.common.UserList;
import com.xinchi.common.UserSessionBean;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserListAction extends BaseAction {
	private static final long serialVersionUID = 8172970461903661720L;

	private List<UserSessionBean> users;

	public String onlineUsers() {
		users = UserList.getInstance().getUserList();
		return SUCCESS;
	}

	public List<UserSessionBean> getUsers() {
		return users;
	}

	public void setUsers(List<UserSessionBean> users) {
		this.users = users;
	}

}
