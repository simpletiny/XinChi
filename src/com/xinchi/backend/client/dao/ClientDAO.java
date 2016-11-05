package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.ClientBean;
import com.xinchi.tools.Page;


public interface ClientDAO{
	
	/**
	 * 新增
	 * @param bo
	 */
	public void insert(com.xinchi.bean.ClientBean bo);
	
	/**
	 * 修改
	 * @param bo
	 */
	public void update(com.xinchi.bean.ClientBean bo);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public com.xinchi.bean.ClientBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bo
	 */
	public List<com.xinchi.bean.ClientBean> getAllByParam(com.xinchi.bean.ClientBean bo);

	public List<ClientBean> getAllCompaniesByPage(Page<ClientBean> page);
}