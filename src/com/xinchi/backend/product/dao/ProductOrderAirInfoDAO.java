package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductOrderAirInfoBean;

public interface ProductOrderAirInfoDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductOrderAirInfoBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductOrderAirInfoBean bean);

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
	public ProductOrderAirInfoBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductOrderAirInfoBean> selectByParam(ProductOrderAirInfoBean bean);

	public void deleteByBasePk(String base_pk);

	public List<ProductOrderAirInfoBean> selectByOrderNumber(String order_number);
}