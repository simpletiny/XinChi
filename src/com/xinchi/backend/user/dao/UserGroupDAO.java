package com.xinchi.backend.user.dao;

import java.util.List;

import com.xinchi.bean.UserGroupBean;

public interface UserGroupDAO {

	public String insert(UserGroupBean bo);

	public void update(UserGroupBean bo);

	public void delete(String id);

	public UserGroupBean selectByPrimaryKey(String id);

	public List<UserGroupBean> getAllByParam(UserGroupBean bo);
}
