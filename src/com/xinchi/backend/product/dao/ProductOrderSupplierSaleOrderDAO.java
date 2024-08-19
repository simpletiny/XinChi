package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.OrderSupplierSaleOrderBean;

public interface ProductOrderSupplierSaleOrderDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(OrderSupplierSaleOrderBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(OrderSupplierSaleOrderBean bean);

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
	public OrderSupplierSaleOrderBean selectByPrimaryKey(String id);

	public List<OrderSupplierSaleOrderBean> selectByBasePk(String base_pk);

	public void deleteByBasePk(String base_pk);
}