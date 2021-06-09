package com.xinchi.backend.supplier.dao;

import java.util.List;

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

	public List<DepositTicketPaidBean> selectByRelatedPk(String related_pk);

	public void deleteByRelatedPk(String related_pk);

	public List<DepositTicketPaidBean> selectByDepositPk(String deposit_pk);

}