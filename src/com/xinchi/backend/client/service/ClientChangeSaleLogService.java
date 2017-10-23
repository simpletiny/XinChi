package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.ClientChangeSaleLogBean;
import com.xinchi.common.BaseService;

public interface ClientChangeSaleLogService extends BaseService{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(ClientChangeSaleLogBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(ClientChangeSaleLogBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public ClientChangeSaleLogBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<ClientChangeSaleLogBean> selectByParam(ClientChangeSaleLogBean bean);
}