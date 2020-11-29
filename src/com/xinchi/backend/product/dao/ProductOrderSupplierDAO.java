package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.OrderSupplierBean;

public interface ProductOrderSupplierDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(OrderSupplierBean bean);

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

	public void deleteByProductPk(String product_pk);

	public List<OrderSupplierBean> selectByProductPk(String product_pk);

	public List<OrderSupplierBean> selectByOrderPk(String order_pk);

	public void deleteByOrderPk(String order_pk);

	public List<OrderSupplierBean> selectByEmployeePk(String employee_pk);
}