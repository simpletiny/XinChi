package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductGroupBean;
import com.xinchi.tools.Page;

public interface ProductGroupDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(ProductGroupBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductGroupBean bean);

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
	public ProductGroupBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductGroupBean> selectByParam(ProductGroupBean bean);

	public List<ProductGroupBean> selectByPage(Page page);
}