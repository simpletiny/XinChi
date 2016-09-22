package com.xinchi.backend.client.dao;

import java.util.List;

public interface EmployeeDAO {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(com.xinchi.bean.ClientEmployeeBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(com.xinchi.bean.ClientEmployeeBean bo);

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
	public com.xinchi.bean.ClientEmployeeBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	public List<com.xinchi.bean.ClientEmployeeBean> getAllByParam(
			com.xinchi.bean.ClientEmployeeBean bo);
}