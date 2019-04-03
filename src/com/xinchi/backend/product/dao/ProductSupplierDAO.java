package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductSupplierBean;

public interface ProductSupplierDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(ProductSupplierBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductSupplierBean bean);

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
	public ProductSupplierBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductSupplierBean> selectByParam(ProductSupplierBean bean);

	public void deleteByProductPk(String product_pk);

	public List<ProductSupplierBean> selectByProductPk(String product_pk);
}