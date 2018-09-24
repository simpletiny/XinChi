package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.ClientEmployeeQuitConnectLogBean;


public interface ClientEmployeeQuitConnectLogDAO{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(ClientEmployeeQuitConnectLogBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(ClientEmployeeQuitConnectLogBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public ClientEmployeeQuitConnectLogBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<ClientEmployeeQuitConnectLogBean> selectByParam(ClientEmployeeQuitConnectLogBean bean);
}