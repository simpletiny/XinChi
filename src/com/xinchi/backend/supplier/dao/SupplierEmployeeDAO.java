package com.xinchi.backend.supplier.dao;

import java.util.List;

import com.xinchi.bean.SupplierEmployeeBean;
import com.xinchi.tools.Page;

public interface SupplierEmployeeDAO {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(SupplierEmployeeBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(SupplierEmployeeBean bo);

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
	public SupplierEmployeeBean selectByPrimaryKey(String id);

	public List<SupplierEmployeeBean> selectBySupplierPk(String supplier_pk);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	public List<SupplierEmployeeBean> getAllByParam(SupplierEmployeeBean bo);

	public List<SupplierEmployeeBean> getAllByPage(Page<SupplierEmployeeBean> page);

	public List<String> getBodyPksByEmployeePks(String[] employee_pks);
}