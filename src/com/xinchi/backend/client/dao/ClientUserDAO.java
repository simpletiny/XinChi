package com.xinchi.backend.client.dao;

import com.xinchi.bean.ClientUserBean;

public interface ClientUserDAO {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(ClientUserBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(ClientUserBean bo);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String id);
	
	public void deleteByClientPk(String client_pk);

}