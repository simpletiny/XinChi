package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class AirTicketPayableBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 3229299236344812819L;

	private String supplier_employee_pk;

	private String supplier_employee_name;

	private String financial_body_name;

	private java.math.BigDecimal payable;

	private String PNR;

	private java.math.BigDecimal budget_payable;

	private java.math.BigDecimal final_payable;

	private java.math.BigDecimal budget_balance;

	private java.math.BigDecimal final_balance;

	private java.math.BigDecimal paid;

	private String pk;

	private String create_user;

	private String update_user;

	private String final_flg;

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}

	public java.math.BigDecimal getPayable() {
		return payable;
	}

	public void setPayable(java.math.BigDecimal payable) {
		this.payable = payable;
	}

	public String getPNR() {
		return PNR;
	}

	public void setPNR(String pNR) {
		PNR = pNR;
	}

	public java.math.BigDecimal getBudget_payable() {
		return budget_payable;
	}

	public void setBudget_payable(java.math.BigDecimal budget_payable) {
		this.budget_payable = budget_payable;
	}

	public java.math.BigDecimal getFinal_payable() {
		return final_payable;
	}

	public void setFinal_payable(java.math.BigDecimal final_payable) {
		this.final_payable = final_payable;
	}

	public java.math.BigDecimal getBudget_balance() {
		return budget_balance;
	}

	public void setBudget_balance(java.math.BigDecimal budget_balance) {
		this.budget_balance = budget_balance;
	}

	public java.math.BigDecimal getFinal_balance() {
		return final_balance;
	}

	public void setFinal_balance(java.math.BigDecimal final_balance) {
		this.final_balance = final_balance;
	}

	public java.math.BigDecimal getPaid() {
		return paid;
	}

	public void setPaid(java.math.BigDecimal paid) {
		this.paid = paid;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
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

	public String getSupplier_employee_name() {
		return supplier_employee_name;
	}

	public void setSupplier_employee_name(String supplier_employee_name) {
		this.supplier_employee_name = supplier_employee_name;
	}

	public String getFinancial_body_name() {
		return financial_body_name;
	}

	public void setFinancial_body_name(String financial_body_name) {
		this.financial_body_name = financial_body_name;
	}

	public String getFinal_flg() {
		return final_flg;
	}

	public void setFinal_flg(String final_flg) {
		this.final_flg = final_flg;
	}

}
