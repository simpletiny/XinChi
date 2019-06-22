package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductSupplierInfoBean;

public interface ProductSupplierInfoDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(ProductSupplierInfoBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductSupplierInfoBean bean);

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
	public ProductSupplierInfoBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductSupplierInfoBean> selectByParam(ProductSupplierInfoBean bean);

	public List<ProductSupplierInfoBean> selectByProductSupplierPk(String pk);

}