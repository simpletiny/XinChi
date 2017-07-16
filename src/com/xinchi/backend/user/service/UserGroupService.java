package com.xinchi.backend.user.service;

import java.util.List;

import com.xinchi.bean.UserGroupBean;
import com.xinchi.common.LogDescription;

@LogDescription(des = "用户组")
public interface UserGroupService {
	@LogDescription(des = "新建用户组")
	public String insert(UserGroupBean group);

	@LogDescription(ignore = true)
	public List<UserGroupBean> getAllGroups();

}
