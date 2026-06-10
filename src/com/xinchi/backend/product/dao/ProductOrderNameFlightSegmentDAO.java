package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductOrderNameFlightSegmentBean;

public interface ProductOrderNameFlightSegmentDAO {
	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductOrderNameFlightSegmentBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductOrderNameFlightSegmentBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String pk);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	public ProductOrderNameFlightSegmentBean selectByPrimaryKey(String pk);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductOrderNameFlightSegmentBean> selectByParam(ProductOrderNameFlightSegmentBean bean);

	public List<ProductOrderNameFlightSegmentBean> selectByNeedPk(String need_pk);

	public void deleteByNeedPk(String need_pk);
}