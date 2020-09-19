package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ReimbursementBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 5719354256244309970L;

	private String item;

	private String date;

	private String month;

	private java.math.BigDecimal money;

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

	private String update_user;

	private String pk;

	private String create_user;

	private String money_from;
	private String money_to;

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

	public java.math.BigDecimal getMoney() {
		return money;
	}

	public void setMoney(java.math.BigDecimal money) {
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

}
