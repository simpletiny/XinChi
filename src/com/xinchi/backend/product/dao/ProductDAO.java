package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductBean;
import com.xinchi.tools.Page;


public interface ProductDAO{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(ProductBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(ProductBean bean);
	
	public void sysUpdate(ProductBean bean);
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public ProductBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<ProductBean> selectByParam(ProductBean bean);

	public List<ProductBean> selectByPage(Page<ProductBean> page);

	public List<ProductBean> selectByPks(String[] pks);
}