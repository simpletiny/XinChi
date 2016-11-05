package com.xinchi.backend.supplier.service;

import java.util.List;

import com.xinchi.bean.SupplierEmployeeBean;
import com.xinchi.tools.Page;

public interface SupplierEmployeeService {

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
	public List<com.xinchi.bean.SupplierEmployeeBean> getAllSupplierEmployeeByParam(
			com.xinchi.bean.SupplierEmployeeBean bo);

	public String createEmployee(SupplierEmployeeBean supplier);

	public String updateEmployee(SupplierEmployeeBean supplier);

	public List<SupplierEmployeeBean> getAllSupplierEmployeeByPage(Page<SupplierEmployeeBean> page);
}