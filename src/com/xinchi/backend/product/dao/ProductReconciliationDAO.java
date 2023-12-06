package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.AirServiceFeeDto;
import com.xinchi.bean.ProductReconciliationBean;
import com.xinchi.tools.Page;

public interface ProductReconciliationDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductReconciliationBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductReconciliationBean bean);

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
	public ProductReconciliationBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductReconciliationBean> selectByParam(ProductReconciliationBean bean);

	public List<ProductReconciliationBean> selectByPage(Page<ProductReconciliationBean> page);

	public List<ProductReconciliationBean> selectSumReconciliation(AirServiceFeeDto bean);
}