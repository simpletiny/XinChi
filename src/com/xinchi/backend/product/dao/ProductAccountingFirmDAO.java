package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductAccountingFirmBean;

public interface ProductAccountingFirmDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductAccountingFirmBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductAccountingFirmBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param pk
	 */
	public ProductAccountingFirmBean selectByPrimaryKey(String pk);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductAccountingFirmBean> selectByParam(ProductAccountingFirmBean bean);
}