package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class AirServiceFeeDto implements Serializable {
	private static final long serialVersionUID = 3962832028908478874L;
	private String payable_pk;
	private String first_month;
	private BigDecimal payable;
	private BigDecimal paid;
	private String product_manager_number;
	private String product_manager_name;
	private int ticket_count;
	private String supplier_employee_name;
	private String supplier_name;
	private String from_to_city;
	private int change_cnt;
	private BigDecimal sum_fee;
	private BigDecimal paid_fee;

	private BigDecimal deduct_money;

	private int people_count;

	private String sale_number;
	private String sale_name;

	// option
	private String first_year;

	public String getPayable_pk() {
		return payable_pk;
	}

	public String getFirst_month() {
		return first_month;
	}

	public BigDecimal getPaid() {
		return paid;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public String getProduct_manager_name() {
		return product_manager_name;
	}

	public int getPeople_count() {
		return people_count;
	}

	public String getSale_number() {
		return sale_number;
	}

	public String getSale_name() {
		return sale_name;
	}

	public void setPayable_pk(String payable_pk) {
		this.payable_pk = payable_pk;
	}

	public void setFirst_month(String first_month) {
		this.first_month = first_month;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public void setProduct_manager_name(String product_manager_name) {
		this.product_manager_name = product_manager_name;
	}

	public void setPeople_count(int people_count) {
		this.people_count = people_count;
	}

	public void setSale_number(String sale_number) {
		this.sale_number = sale_number;
	}

	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}

	public BigDecimal getPayable() {
		return payable;
	}

	public void setPayable(BigDecimal payable) {
		this.payable = payable;
	}

	public int getTicket_count() {
		return ticket_count;
	}

	public String getSupplier_employee_name() {
		return supplier_employee_name;
	}

	public String getFrom_to_city() {
		return from_to_city;
	}

	public int getChange_cnt() {
		return change_cnt;
	}

	public BigDecimal getSum_fee() {
		return sum_fee;
	}

	public BigDecimal getPaid_fee() {
		return paid_fee;
	}

	public void setTicket_count(int ticket_count) {
		this.ticket_count = ticket_count;
	}

	public void setSupplier_employee_name(String supplier_employee_name) {
		this.supplier_employee_name = supplier_employee_name;
	}

	public void setFrom_to_city(String from_to_city) {
		this.from_to_city = from_to_city;
	}

	public void setChange_cnt(int change_cnt) {
		this.change_cnt = change_cnt;
	}

	public void setSum_fee(BigDecimal sum_fee) {
		this.sum_fee = sum_fee;
	}

	public void setPaid_fee(BigDecimal paid_fee) {
		this.paid_fee = paid_fee;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public BigDecimal getDeduct_money() {
		return deduct_money;
	}

	public void setDeduct_money(BigDecimal deduct_money) {
		this.deduct_money = deduct_money;
	}

	public String getFirst_year() {
		return first_year;
	}

	public void setFirst_year(String first_year) {
		this.first_year = first_year;
	}

}
