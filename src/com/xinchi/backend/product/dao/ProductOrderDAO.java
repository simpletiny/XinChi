package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductOrderBean;
import com.xinchi.tools.Page;

public interface ProductOrderDAO {
	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductOrderBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductOrderBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String pk);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	public ProductOrderBean selectByPrimaryKey(String pk);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductOrderBean> selectByParam(ProductOrderBean bean);

	public List<ProductOrderBean> selectByPage(Page<ProductOrderBean> page);

	public ProductOrderBean selectByOrderNumber(String order_number);
}