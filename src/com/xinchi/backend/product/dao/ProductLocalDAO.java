package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductLocalBean;

public interface ProductLocalDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(ProductLocalBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductLocalBean bean);

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
	public ProductLocalBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductLocalBean> selectByParam(ProductLocalBean bean);

	public List<ProductLocalBean> selectByProductPk(String product_pk);

	public void deleteByProductPk(String product_pk);

}