package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ProductGroupSupplierBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String group_pk;

	private String supplier_pk;

	private String pk;

	public String getGroup_pk() {
		return group_pk;
	}

	public void setGroup_pk(String group_pk) {
		this.group_pk = group_pk;
	}

	public String getSupplier_pk() {
		return supplier_pk;
	}

	public void setSupplier_pk(String supplier_pk) {
		this.supplier_pk = supplier_pk;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

}
