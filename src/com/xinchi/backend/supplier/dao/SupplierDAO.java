package com.xinchi.backend.supplier.dao;

import java.util.List;

import com.xinchi.bean.SupplierBean;
import com.xinchi.tools.Page;

public interface SupplierDAO {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(com.xinchi.bean.SupplierBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(com.xinchi.bean.SupplierBean bo);

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
	public com.xinchi.bean.SupplierBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	public List<com.xinchi.bean.SupplierBean> getAllByParam(com.xinchi.bean.SupplierBean bo);

	public List<SupplierBean> getAllByPage(Page<SupplierBean> page);

	public List<SupplierBean> selectByPks(List<String> supplier_pks);
}