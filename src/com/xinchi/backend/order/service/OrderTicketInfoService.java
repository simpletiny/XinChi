package com.xinchi.backend.order.service;

import java.util.List;

import com.xinchi.bean.SaleOrderTicketInfoBean;
import com.xinchi.common.BaseService;

public interface OrderTicketInfoService extends BaseService {

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
}