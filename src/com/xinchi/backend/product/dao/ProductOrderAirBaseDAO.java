package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductOrderAirBaseBean;

public interface ProductOrderAirBaseDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(ProductOrderAirBaseBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductOrderAirBaseBean bean);

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
	public ProductOrderAirBaseBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductOrderAirBaseBean> selectByParam(ProductOrderAirBaseBean bean);

	public ProductOrderAirBaseBean selectByTeamNumber(String team_number);
}