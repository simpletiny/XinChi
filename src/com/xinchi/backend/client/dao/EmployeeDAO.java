package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.RelationLevelDto;
import com.xinchi.tools.Page;

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

	public List<ClientEmployeeBean> getAllByPage(Page<ClientEmployeeBean> page);

	public List<String> getBodyPksByEmployeePks(String[] employee_pks);

	public void deleteClientEmployeeByPks(List<String> employee_pks);
	
	public void recoveryClientEmployeeByPks(List<String> employee_pks);
	
	public RelationLevelDto selectRelationCntBySales(String sales);

	public void publicClientEmployee(List<String> employee_pks);

}