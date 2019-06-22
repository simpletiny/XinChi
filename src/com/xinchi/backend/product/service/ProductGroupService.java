package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.ProductGroupBean;
import com.xinchi.bean.SupplierBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface ProductGroupService extends BaseService {

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
	public String update(ProductGroupBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	public String delete(String id);

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
	public List<ProductGroupBean> getAllByParam(ProductGroupBean bean);

	public List<ProductGroupBean> selectByPage(Page page);

	public List<SupplierBean> selectByGroupPk(String group_pk);

	public String delete(ProductGroupBean group);
}