package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.ClientEmployeeTypeCountBean;

public interface ClientEmployeeTypeCountDAO {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(ClientEmployeeTypeCountBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(ClientEmployeeTypeCountBean bo);

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
	public ClientEmployeeTypeCountBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	public List<ClientEmployeeTypeCountBean> selectByParam(ClientEmployeeTypeCountBean bo);

	public ClientEmployeeTypeCountBean selectSumByParam(ClientEmployeeTypeCountBean bean);

}