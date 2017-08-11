package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.ProductAirTicketBean;
import com.xinchi.common.BaseService;

public interface ProductAirTicketService extends BaseService{
	
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

	public void deleteByProductPk(String product_pk);

	public List<ProductAirTicketBean> selectByProductPk(String product_pk);
}