package com.xinchi.backend.supplier.dao;

import com.xinchi.bean.DepositPaymentBean;

public interface DepositPaymentDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(DepositPaymentBean bean);

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
	public DepositPaymentBean selectByPrimaryKey(String id);

}