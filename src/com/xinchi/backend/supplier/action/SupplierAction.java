package com.xinchi.backend.supplier.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.supplier.service.SupplierService;
import com.xinchi.bean.SupplierBean;
import com.xinchi.bean.SupplierFileBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope("prototype")
public class SupplierAction extends BaseAction {
	private static final long serialVersionUID = -5405111196840939221L;
	private SupplierBean supplier;
	@Autowired
	private SupplierService supplierService;
	private SupplierFileBean supplierFile;

	public String createSupplier() throws IOException {
		supplierService.createSupplier(supplier);

		String supplier_pk = supplier.getPk();
		supplierFile.setSupplier_pk(supplier_pk);
		supplierService.saveSupplierFile(supplierFile);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	public String updateSupplier() throws IOException {
		supplierService.updateSupplier(supplier);

		String supplier_pk = supplier.getPk();
		supplierFile.setSupplier_pk(supplier_pk);
		supplierService.updateSupplierFile(supplierFile);
		resultStr = SUCCESS;

		return SUCCESS;
	}

	public String blockSupplier() {
		resultStr = supplierService.blockSupplier(supplier_pk);
		return SUCCESS;
	}

	private List<SupplierBean> suppliers;

	public String searchSupplier() {
		suppliers = supplierService.getAllCompaniesByParam(null);
		return SUCCESS;
	}

	public String searchSupplierByPage() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", supplier);
		page.setParams(params);
		suppliers = supplierService.getAllCompaniesByPage(page);
		return SUCCESS;
	}

	private String supplier_pk;

	public String searchSupplierByPk() {
		supplier = supplierService.selectByPrimaryKey(supplier_pk);
		return SUCCESS;
	}

	private List<SupplierFileBean> supplierFiles;

	public String searchSupplierFiles() {
		supplierFiles = supplierService.searchSupplierFilesBySupplierPk(supplier_pk);
		return SUCCESS;
	}

	private String file_name;

	public String deleteSupplierFile() {
		supplierService.deleteSupplierFile(file_name, supplier_pk);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	public SupplierBean getSupplier() {
		return supplier;
	}

	public void setSupplier(SupplierBean supplier) {
		this.supplier = supplier;
	}

	public List<SupplierBean> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<SupplierBean> suppliers) {
		this.suppliers = suppliers;
	}

	public String getSupplier_pk() {
		return supplier_pk;
	}

	public void setSupplier_pk(String supplier_pk) {
		this.supplier_pk = supplier_pk;
	}

	public SupplierFileBean getSupplierFile() {
		return supplierFile;
	}

	public void setSupplierFile(SupplierFileBean supplierFile) {
		this.supplierFile = supplierFile;
	}

	public List<SupplierFileBean> getSupplierFiles() {
		return supplierFiles;
	}

	public void setSupplierFiles(List<SupplierFileBean> supplierFiles) {
		this.supplierFiles = supplierFiles;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

}