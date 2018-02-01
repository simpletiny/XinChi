package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.ProductBean;
import com.xinchi.tools.Page;

public interface ProductService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public String insert(ProductBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public String update(ProductBean bean);

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
	public ProductBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductBean> getAllByParam(ProductBean bean);

	public List<ProductBean> selectByPage(Page<ProductBean> page);

	public String onSale(String product_pks, String sale_flg);

	public void updateStraight(ProductBean product);
}