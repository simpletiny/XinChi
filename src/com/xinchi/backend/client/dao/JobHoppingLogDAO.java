package com.xinchi.backend.client.dao;

import com.xinchi.bean.JobHoppingLogBean;

public interface JobHoppingLogDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(JobHoppingLogBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(JobHoppingLogBean bean);

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
	public JobHoppingLogBean selectByPrimaryKey(String id);
}