package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductOrderTeamNumberBean;

public interface ProductOrderTeamNumberDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(ProductOrderTeamNumberBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductOrderTeamNumberBean bean);

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
	public ProductOrderTeamNumberBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductOrderTeamNumberBean> selectByParam(ProductOrderTeamNumberBean bean);

	public List<ProductOrderTeamNumberBean> selectByOrderNumber(String order_number);

	public List<String> selectTeamNumbersByOrderNumber(String order_number);

	public void deleteByOrderNumber(String order_number);

	public List<ProductOrderTeamNumberBean> selectByTeamNumber(String team_number);

}