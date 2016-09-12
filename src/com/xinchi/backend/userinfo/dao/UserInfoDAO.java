package com.xinchi.backend.userinfo.dao;

import java.util.List;

import com.xinchi.bean.UserInfoBean;


public interface UserInfoDAO{
	
	/**
	 * 新增
	 * @param bo
	 */
	public void insert(com.xinchi.bean.UserInfoBean bo);
	
	/**
	 * 修改
	 * @param bo
	 */
	public void update(com.xinchi.bean.UserInfoBean bo);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public com.xinchi.bean.UserInfoBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bo
	 */
	public List<com.xinchi.bean.UserInfoBean> getAllByParam(com.xinchi.bean.UserInfoBean bo);

	public UserInfoBean selectByUserId(String id);
}