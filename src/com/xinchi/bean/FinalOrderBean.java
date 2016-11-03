package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class FinalOrderBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String team_number;

	private String product;

	private String departure_date;

	private String return_date;

	private Integer days;

	private Integer people_count;

	private String client_employee_pk;

	private String client_employee_name;

	private java.math.BigDecimal receivable;

	private java.math.BigDecimal received;

	private java.math.BigDecimal client_debt;

	private String supplier_employee_pk;

	private String supplier_employee_name;

	private java.math.BigDecimal payable;

	private java.math.BigDecimal paid;

	private java.math.BigDecimal supplier_debt;

	private String create_user;

	private String update_user;

	private String pk;

	private String comment;
	private java.math.BigDecimal traffic_payment;
	private java.math.BigDecimal other_payment;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
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

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getPeople_count() {
		return people_count;
	}

	public void setPeople_count(Integer people_count) {
		this.people_count = people_count;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public String getClient_employee_name() {
		return client_employee_name;
	}

	public void setClient_employee_name(String client_employee_name) {
		this.client_employee_name = client_employee_name;
	}

	public java.math.BigDecimal getReceivable() {
		return receivable;
	}

	public void setReceivable(java.math.BigDecimal receivable) {
		this.receivable = receivable;
	}

	public java.math.BigDecimal getReceived() {
		return received;
	}

	public void setReceived(java.math.BigDecimal received) {
		this.received = received;
	}

	public java.math.BigDecimal getClient_debt() {
		return client_debt;
	}

	public void setClient_debt(java.math.BigDecimal client_debt) {
		this.client_debt = client_debt;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public java.math.BigDecimal getOther_payment() {
		return other_payment;
	}

	public void setOther_payment(java.math.BigDecimal other_payment) {
		this.other_payment = other_payment;
	}

	public java.math.BigDecimal getTraffic_payment() {
		return traffic_payment;
	}

	public void setTraffic_payment(java.math.BigDecimal traffic_payment) {
		this.traffic_payment = traffic_payment;
	}

}
