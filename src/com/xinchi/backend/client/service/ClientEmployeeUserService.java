package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.ClientEmployeeUserBean;
import com.xinchi.common.BaseService;

public interface ClientEmployeeUserService extends BaseService {
	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(ClientEmployeeUserBean bo);

	/**
	 * 带创建日期的插入
	 * 
	 * @param bo
	 */
	public void insertWithCreateTime(ClientEmployeeUserBean bo);

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
	
	public List<ClientEmployeeUserBean> selectByParam(ClientEmployeeUserBean option);

}