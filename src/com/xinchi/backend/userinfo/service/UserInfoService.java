package com.xinchi.backend.userinfo.service;

import java.util.List;




public interface UserInfoService{
	
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
}