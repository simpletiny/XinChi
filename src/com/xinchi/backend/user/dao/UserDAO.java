package com.xinchi.backend.user.dao;

import java.util.List;

import com.xinchi.bean.UserBaseBean;
import com.xinchi.bean.UserCommonBean;


public interface UserDAO{
	
	/**
	 * 新增
	 * @param bo
	 */
	public void insert(UserBaseBean bo);
	
	/**
	 * 修改
	 * @param bo
	 */
	public void update(UserBaseBean bo);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public UserBaseBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bo
	 */
	public List<UserBaseBean> getAllByParam(UserBaseBean bo);
	
	public String getMaxUserNumber();
	
	
	public List<UserCommonBean> getAllUserCommonByParam(UserCommonBean bo);

	public List<UserCommonBean> getAllNewUsers();

	public List<UserCommonBean> getAllUsersByRole(String roles);
}