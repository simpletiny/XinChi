package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductDelayBean;

public interface ProductDelayDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductDelayBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductDelayBean bean);

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
	public ProductDelayBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductDelayBean> selectByParam(ProductDelayBean bean);

	public ProductDelayBean selectByProductPk(String product_pk);

	public void deleteByProductPk(String product_pk);

	public void truncateTable();
}