package com.xinchi.backend.user.service;

import java.util.List;

import com.xinchi.bean.UserGroupBean;

public interface UserGroupService {

	public String insert(UserGroupBean group);

	public List<UserGroupBean> getAllGroups();

}
