package com.xinchi.backend.supplier.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.supplier.service.SupplierService;
import com.xinchi.bean.SupplierBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope("prototype")
public class SupplierAction extends BaseAction {
	private static final long serialVersionUID = -5405111196840939221L;
	private SupplierBean supplier;
	@Autowired
	private SupplierService supplierService;

	public String createSupplier() {
		resultStr = supplierService.createSupplier(supplier);
		return SUCCESS;
	}

	public String updateSupplier() {
		resultStr = supplierService.updateSupplier(supplier);
		return SUCCESS;
	}

	private List<SupplierBean> suppliers;

	public String searchSupplier() {
		suppliers = supplierService.getAllCompaniesByParam(null);
		return SUCCESS;
	}

	private String supplier_pk;

	public String searchSupplierByPk() {
		supplier = supplierService.selectByPrimaryKey(supplier_pk);
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

}