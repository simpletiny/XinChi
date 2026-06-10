package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class PayableOrderBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 2887604358947073723L;

	private String team_number;

	private String supplier_employee_pk;

	private String final_flg;

	private java.math.BigDecimal budget_payable;

	private java.math.BigDecimal final_payable;

	private String pk;

	private String create_user;

	private String update_user;

	// DTO
	private String supplier_employee_name;

	public String getTeam_number() {
		return team_number;
	}

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public String getFinal_flg() {
		return final_flg;
	}

	public java.math.BigDecimal getBudget_payable() {
		return budget_payable;
	}

	public java.math.BigDecimal getFinal_payable() {
		return final_payable;
	}

	public String getPk() {
		return pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}

	public void setFinal_flg(String final_flg) {
		this.final_flg = final_flg;
	}

	public void setBudget_payable(java.math.BigDecimal budget_payable) {
		this.budget_payable = budget_payable;
	}

	public void setFinal_payable(java.math.BigDecimal final_payable) {
		this.final_payable = final_payable;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getSupplier_employee_name() {
		return supplier_employee_name;
	}

	public void setSupplier_employee_name(String supplier_employee_name) {
		this.supplier_employee_name = supplier_employee_name;
	}

}
