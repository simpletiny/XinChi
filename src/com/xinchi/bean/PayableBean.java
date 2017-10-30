package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class PayableBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -3021363783752187948L;

	private String team_number;

	private String final_flg;

	private String supplier_employee_name;

	private String supplier_employee_pk;

	private String supplier_name;
	private String supplier_pk;

	private String departure_date;

	private String return_date;

	private String product;

	private Integer people_count;

	private BigDecimal budget_payable;

	private BigDecimal final_payable;

	private BigDecimal paid;

	private BigDecimal budget_balance;

	private BigDecimal final_balance;

	private String sales;

	private String sales_name;

	private String update_user;

	private String pk;

	private String create_user;

	private String back_days;
	// search condition
	private String departure_month;
	private String team_status;
	private String type;
	private String sort_type;
	private String departure_from;
	private String departure_to;
	private String return_to;
	private String zero_flg;

	private String pick_date;
	private String send_date;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getFinal_flg() {
		return final_flg;
	}

	public void setFinal_flg(String final_flg) {
		this.final_flg = final_flg;
	}

	public String getSupplier_employee_name() {
		return supplier_employee_name;
	}

	public void setSupplier_employee_name(String supplier_employee_name) {
		this.supplier_employee_name = supplier_employee_name;
	}

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public String getReturn_date() {
		return return_date;
	}

	public void setReturn_date(String return_date) {
		this.return_date = return_date;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Integer getPeople_count() {
		return people_count;
	}

	public void setPeople_count(Integer people_count) {
		this.people_count = people_count;
	}

	public BigDecimal getBudget_payable() {
		return budget_payable;
	}

	public void setBudget_payable(BigDecimal budget_payable) {
		this.budget_payable = budget_payable;
	}

	public BigDecimal getFinal_payable() {
		return final_payable;
	}

	public void setFinal_payable(BigDecimal final_payable) {
		this.final_payable = final_payable;
	}

	public BigDecimal getPaid() {
		return paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public BigDecimal getBudget_balance() {
		return budget_balance;
	}

	public void setBudget_balance(BigDecimal budget_balance) {
		this.budget_balance = budget_balance;
	}

	public BigDecimal getFinal_balance() {
		return final_balance;
	}

	public void setFinal_balance(BigDecimal final_balance) {
		this.final_balance = final_balance;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getSales_name() {
		return sales_name;
	}

	public void setSales_name(String sales_name) {
		this.sales_name = sales_name;
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

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getDeparture_month() {
		return departure_month;
	}

	public void setDeparture_month(String departure_month) {
		this.departure_month = departure_month;
	}

	public String getTeam_status() {
		return team_status;
	}

	public void setTeam_status(String team_status) {
		this.team_status = team_status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSort_type() {
		return sort_type;
	}

	public void setSort_type(String sort_type) {
		this.sort_type = sort_type;
	}

	public String getBack_days() {
		return back_days;
	}

	public void setBack_days(String back_days) {
		this.back_days = back_days;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getSupplier_pk() {
		return supplier_pk;
	}

	public void setSupplier_pk(String supplier_pk) {
		this.supplier_pk = supplier_pk;
	}

	public String getDeparture_from() {
		return departure_from;
	}

	public void setDeparture_from(String departure_from) {
		this.departure_from = departure_from;
	}

	public String getDeparture_to() {
		return departure_to;
	}

	public void setDeparture_to(String departure_to) {
		this.departure_to = departure_to;
	}

	public String getReturn_to() {
		return return_to;
	}

	public void setReturn_to(String return_to) {
		this.return_to = return_to;
	}

	public String getZero_flg() {
		return zero_flg;
	}

	public void setZero_flg(String zero_flg) {
		this.zero_flg = zero_flg;
	}

	public String getPick_date() {
		return pick_date;
	}

	public void setPick_date(String pick_date) {
		this.pick_date = pick_date;
	}

	public String getSend_date() {
		return send_date;
	}

	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}

}
