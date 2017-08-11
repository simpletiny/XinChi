package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductAirTicketBean;


public interface ProductAirTicketDAO{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(ProductAirTicketBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(ProductAirTicketBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public ProductAirTicketBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<ProductAirTicketBean> selectByParam(ProductAirTicketBean bean);

	public void deleteByProduct_pk(String product_pk);

	public List<ProductAirTicketBean> selectByProductPk(String product_pk);
}