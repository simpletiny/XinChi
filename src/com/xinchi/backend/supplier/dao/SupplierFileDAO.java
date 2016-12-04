package com.xinchi.backend.supplier.dao;

import java.util.List;

import com.xinchi.bean.SupplierFileBean;

public interface SupplierFileDAO {
	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(SupplierFileBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(SupplierFileBean bo);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String id);

	public List<SupplierFileBean> selectSupplierFilesBySupplierPk(
			String supplier_pk);

	public  List<SupplierFileBean> selectByParam(SupplierFileBean sfb);

	public void deleteFileByParam(SupplierFileBean sfb);

}
