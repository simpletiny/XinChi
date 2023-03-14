package com.xinchi.backend.supplier.service;

import java.io.IOException;
import java.util.List;

import com.xinchi.bean.SupplierBean;
import com.xinchi.bean.SupplierFileBean;
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "供应商财务主体")
public interface SupplierService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	@LogDescription(ignore = true)
	public void insert(com.xinchi.bean.SupplierBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	@LogDescription(ignore = true)
	public void update(com.xinchi.bean.SupplierBean bo);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	@LogDescription(ignore = true)
	public void delete(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	@LogDescription(ignore = true)
	public com.xinchi.bean.SupplierBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bo
	 */
	@LogDescription(ignore = true)
	public List<com.xinchi.bean.SupplierBean> getAllCompaniesByParam(com.xinchi.bean.SupplierBean bo);

	@LogDescription(des = "新建供应商财务主体")
	public String createSupplier(SupplierBean supplier);

	@LogDescription(des = "修改供应商财务主体")
	public String updateSupplier(SupplierBean supplier);

	@LogDescription(des = "搜索供应商财务主体")
	public List<SupplierBean> getAllCompaniesByPage(Page<SupplierBean> page);

	@LogDescription(des = "上传供应商财务主体相关文件")
	public void saveSupplierFile(SupplierFileBean supplierFile) throws IOException;

	@LogDescription(ignore = true)
	public List<SupplierFileBean> searchSupplierFilesBySupplierPk(String supplier_pk);

	@LogDescription(ignore = true)
	public void updateSupplierFile(SupplierFileBean supplierFile) throws IOException;

	@LogDescription(des = "删除供应商财务主体相关文件")
	public void deleteSupplierFile(String file_name, String supplier_pk);

	public String blockSupplier(String supplier_pk);
}