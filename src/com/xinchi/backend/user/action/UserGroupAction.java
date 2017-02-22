package com.xinchi.backend.user.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.user.service.UserGroupService;
import com.xinchi.bean.UserGroupBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope("prototype")
public class UserGroupAction extends BaseAction {

	private static final long serialVersionUID = 4558937304989407716L;
	private UserGroupBean group;

	@Autowired
	private UserGroupService groupService;

	public String createGroup() {
		resultStr = groupService.insert(group);
		return SUCCESS;
	}

	private List<UserGroupBean> groups;

	public String searchAllGroups() {
		groups = groupService.getAllGroups();
		return SUCCESS;
	}

	public UserGroupBean getGroup() {
		return group;
	}

	public void setGroup(UserGroupBean group) {
		this.group = group;
	}

	public List<UserGroupBean> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroupBean> groups) {
		this.groups = groups;
	}
}