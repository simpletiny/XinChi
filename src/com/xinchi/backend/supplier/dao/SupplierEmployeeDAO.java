package com.xinchi.backend.supplier.dao;

import java.util.List;

public interface SupplierEmployeeDAO {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(com.xinchi.bean.SupplierEmployeeBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(com.xinchi.bean.SupplierEmployeeBean bo);

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
	public com.xinchi.bean.SupplierEmployeeBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	public List<com.xinchi.bean.SupplierEmployeeBean> getAllByParam(
			com.xinchi.bean.SupplierEmployeeBean bo);
}