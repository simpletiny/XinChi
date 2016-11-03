package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class FinalOrderSupplierBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String team_number;

	private String supplier_employee_pk;

	private String supplier_employee_name;

	private java.math.BigDecimal payable;

	private java.math.BigDecimal paid;

	private java.math.BigDecimal supplier_debt;

	private String create_user;

	private String update_user;

	private String pk;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}

	public String getSupplier_employee_name() {
		return supplier_employee_name;
	}

	public void setSupplier_employee_name(String supplier_employee_name) {
		this.supplier_employee_name = supplier_employee_name;
	}

	public java.math.BigDecimal getPayable() {
		return payable;
	}

	public void setPayable(java.math.BigDecimal payable) {
		this.payable = payable;
	}

	public java.math.BigDecimal getPaid() {
		return paid;
	}

	public void setPaid(java.math.BigDecimal paid) {
		this.paid = paid;
	}

	public java.math.BigDecimal getSupplier_debt() {
		return supplier_debt;
	}

	public void setSupplier_debt(java.math.BigDecimal supplier_debt) {
		this.supplier_debt = supplier_debt;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

}
