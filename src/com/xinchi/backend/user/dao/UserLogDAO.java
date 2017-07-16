package com.xinchi.backend.user.dao;

import java.util.List;

import com.xinchi.bean.UserLogBean;
import com.xinchi.tools.Page;


public interface UserLogDAO{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(UserLogBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(UserLogBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public UserLogBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<UserLogBean> getAllByParam(UserLogBean bean);

	public List<UserLogBean> selectByPage(Page page);
}