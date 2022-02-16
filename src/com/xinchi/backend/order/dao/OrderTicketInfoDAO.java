package com.xinchi.backend.order.dao;

import java.util.List;

import com.xinchi.bean.SaleOrderTicketInfoBean;

public interface OrderTicketInfoDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(SaleOrderTicketInfoBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(SaleOrderTicketInfoBean bean);

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
	public SaleOrderTicketInfoBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<SaleOrderTicketInfoBean> selectByParam(SaleOrderTicketInfoBean bean);

	public List<SaleOrderTicketInfoBean> selectByOrderPk(String order_pk);

	public void deleteByOrderPk(String order_pk);
}