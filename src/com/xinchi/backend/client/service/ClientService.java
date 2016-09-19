package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.ClientBean;

public interface ClientService {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(com.xinchi.bean.ClientBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(com.xinchi.bean.ClientBean bo);

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
	public com.xinchi.bean.ClientBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	public List<com.xinchi.bean.ClientBean> getAllCompaniesByParam(
			com.xinchi.bean.ClientBean bo);

	public String createCompany(ClientBean client);
	
	public String updateCompany(ClientBean client);
}