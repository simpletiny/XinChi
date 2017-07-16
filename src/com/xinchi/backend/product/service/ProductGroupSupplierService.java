package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.ProductGroupSupplierBean;


public interface ProductGroupSupplierService{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(ProductGroupSupplierBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(ProductGroupSupplierBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public ProductGroupSupplierBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<ProductGroupSupplierBean> getAllByParam(ProductGroupSupplierBean bean);

	public List<ProductGroupSupplierBean> selectByGroupPk(String group_pk);
}