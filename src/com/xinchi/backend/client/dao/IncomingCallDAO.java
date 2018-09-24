package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.IncomingCallBean;

public interface IncomingCallDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(IncomingCallBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(IncomingCallBean bean);

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
	public IncomingCallBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<IncomingCallBean> selectByParam(IncomingCallBean bean);
}