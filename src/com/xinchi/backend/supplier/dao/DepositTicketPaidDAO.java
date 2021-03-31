package com.xinchi.backend.supplier.dao;

import com.xinchi.bean.DepositTicketPaidBean;

public interface DepositTicketPaidDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(DepositTicketPaidBean bean);

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
	public DepositTicketPaidBean selectByPrimaryKey(String id);

}