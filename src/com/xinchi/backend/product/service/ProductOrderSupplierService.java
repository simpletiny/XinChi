package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.OrderSupplierBean;
import com.xinchi.common.BaseService;

public interface ProductOrderSupplierService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(OrderSupplierBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(OrderSupplierBean bean);

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
	public OrderSupplierBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<OrderSupplierBean> selectByParam(OrderSupplierBean bean);

	public List<OrderSupplierBean> selectByProductPk(String product_pk);

	public void deleteByProductPk(String pk);
}