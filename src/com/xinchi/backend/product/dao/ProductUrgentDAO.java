package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductUrgentBean;

public interface ProductUrgentDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductUrgentBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductUrgentBean bean);

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
	public ProductUrgentBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductUrgentBean> selectByParam(ProductUrgentBean bean);
}