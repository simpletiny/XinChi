package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.ClientEmployeeUserBean;

public interface ClientEmployeeUserDAO {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(ClientEmployeeUserBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(ClientEmployeeUserBean bo);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String id);

	public void deleteByEmployeePk(String employee_pk);
	
	
	public List<ClientEmployeeUserBean> selectByEmployeePk(String employee_pk);

	public void insertWithCreateTime(ClientEmployeeUserBean bo);

	public List<ClientEmployeeUserBean> selectByParam(ClientEmployeeUserBean option);

	public void insertWithoutLogin(ClientEmployeeUserBean ceub);

}