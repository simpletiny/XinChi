package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.tools.Page;

public interface EmployeeService {

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
	public List<com.xinchi.bean.ClientEmployeeBean> getAllClientEmployeeByParam(
			com.xinchi.bean.ClientEmployeeBean bo);

	public String createEmployee(ClientEmployeeBean client);

	public String updateEmployee(ClientEmployeeBean client);

	public List<ClientEmployeeBean> getAllClientEmployeeByPage(Page<ClientEmployeeBean> page);
}