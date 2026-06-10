package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class ReimbursementBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 5719354256244309970L;

	private String item;

	private String date;

	private String month;

	private BigDecimal money;

	private String comment;

	private String status;

	private String apply_user;

	private String approval_user;

	private String pay_user;
	private String apply_name;

	private String approval_name;

	private String pay_name;

	private String approval_time;

	private String pay_time;

	private String pay_number;
	// 如果是销售费用
	private String client_employee_pk;
	private String client_employee_name;

	private String delete_flg;

	private String belong_year;
	// option
	private List<String> items;
	private String money_from;
	private String money_to;

	// DTO
	private BigDecimal paid_money;
	private BigDecimal waiting_money;
	private BigDecimal suspense_money;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApply_user() {
		return apply_user;
	}

	public void setApply_user(String apply_user) {
		this.apply_user = apply_user;
	}

	public String getApproval_user() {
		return approval_user;
	}

	public void setApproval_user(String approval_user) {
		this.approval_user = approval_user;
	}

	public String getPay_user() {
		return pay_user;
	}

	public void setPay_user(String pay_user) {
		this.pay_user = pay_user;
	}

	public String getApproval_time() {
		return approval_time;
	}

	public void setApproval_time(String approval_time) {
		this.approval_time = approval_time;
	}

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public String getPay_number() {
		return pay_number;
	}

	public void setPay_number(String pay_number) {
		this.pay_number = pay_number;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getApply_name() {
		return apply_name;
	}

	public String getApproval_name() {
		return approval_name;
	}

	public String getPay_name() {
		return pay_name;
	}

	public void setApply_name(String apply_name) {
		this.apply_name = apply_name;
	}

	public void setApproval_name(String approval_name) {
		this.approval_name = approval_name;
	}

	public void setPay_name(String pay_name) {
		this.pay_name = pay_name;
	}

	public String getMoney_from() {
		return money_from;
	}

	public String getMoney_to() {
		return money_to;
	}

	public void setMoney_from(String money_from) {
		this.money_from = money_from;
	}

	public void setMoney_to(String money_to) {
		this.money_to = money_to;
	}

	public String getDelete_flg() {
		return delete_flg;
	}

	public void setDelete_flg(String delete_flg) {
		this.delete_flg = delete_flg;
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

	public String getBelong_year() {
		return belong_year;
	}

	public void setBelong_year(String belong_year) {
		this.belong_year = belong_year;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public BigDecimal getPaid_money() {
		return paid_money;
	}

	public void setPaid_money(BigDecimal paid_money) {
		this.paid_money = paid_money;
	}

	public BigDecimal getWaiting_money() {
		return waiting_money;
	}

	public void setWaiting_money(BigDecimal waiting_money) {
		this.waiting_money = waiting_money;
	}

	public BigDecimal getSuspense_money() {
		return suspense_money;
	}

	public void setSuspense_money(BigDecimal suspense_money) {
		this.suspense_money = suspense_money;
	}

}
