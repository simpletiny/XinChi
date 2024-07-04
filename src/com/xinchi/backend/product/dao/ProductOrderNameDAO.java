package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductOrderNameBean;
import com.xinchi.tools.Page;

public interface ProductOrderNameDAO {
	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(ProductOrderNameBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(ProductOrderNameBean bean);

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
	public ProductOrderNameBean selectByPrimaryKey(String pk);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductOrderNameBean> selectByParam(ProductOrderNameBean bean);

	public List<ProductOrderNameBean> selectByPage(Page<ProductOrderNameBean> page);

	public List<ProductOrderNameBean> selectByPks(List<String> name_pks);

	public List<ProductOrderNameBean> selectByNeedPk(String need_pk);

	public List<ProductOrderNameBean> selectByTeamNumber(String team_number);

	public ProductOrderNameBean selectByNamePk(String name_pk);

	public void deleteByProductOrderNumber(String order_number);
}