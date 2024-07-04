package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductOrderNameAirNeedBean;

public interface ProductOrderNameAirNeedDAO {
	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductOrderNameAirNeedBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductOrderNameAirNeedBean bean);

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
	public ProductOrderNameAirNeedBean selectByPrimaryKey(String pk);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductOrderNameAirNeedBean> selectByParam(ProductOrderNameAirNeedBean bean);

	public List<ProductOrderNameAirNeedBean> selectByNamePk(String name_pk);

	public List<ProductOrderNameAirNeedBean> selectByNamePks(List<String> name_pks);

	public void deleteByNeedPk(String need_pk);
}