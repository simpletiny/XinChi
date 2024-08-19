package com.xinchi.backend.product.dao;

import com.xinchi.bean.OrderSupplierSaleOrderNameInfoBean;

public interface ProductOrderSupplierSaleOrderNameInfoDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(OrderSupplierSaleOrderNameInfoBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(OrderSupplierSaleOrderNameInfoBean bean);

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
	public OrderSupplierSaleOrderNameInfoBean selectByPrimaryKey(String id);

	public void deleteByBasePk(String pk);
}