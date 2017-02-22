package com.xinchi.backend.user.dao;

import java.util.List;

import com.xinchi.bean.UserGroupRelatedBean;

public interface UserGroupRelatedDAO {

	public String insert(UserGroupRelatedBean bo);

	public void update(UserGroupRelatedBean bo);

	public void delete(String id);

	public UserGroupRelatedBean selectByPrimaryKey(String id);

	public List<UserGroupRelatedBean> getAllByParam(UserGroupRelatedBean bo);
}
