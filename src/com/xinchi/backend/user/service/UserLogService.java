package com.xinchi.backend.user.service;

import java.util.List;

import com.xinchi.bean.UserLogBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(ignore = true)
public interface UserLogService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(UserLogBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(UserLogBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	public UserLogBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<UserLogBean> getAllByParam(UserLogBean bean);

	public List<UserLogBean> selectByPage(Page page);
}