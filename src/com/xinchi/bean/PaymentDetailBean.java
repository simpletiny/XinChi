package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class PaymentDetailBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 8505739134690461246L;

	private String account;

	private String time;

	private String type;

	private java.math.BigDecimal money;

	private java.math.BigDecimal balance;

	private String record_time;

	private String record_user;

	private String update_user;

	private String create_user;

	private String pk;
	private String comment;
	private String inner_flg;
	private String inner_pk;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public java.math.BigDecimal getMoney() {
		return money;
	}

	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}

	public java.math.BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(java.math.BigDecimal balance) {
		this.balance = balance;
	}

	public String getRecord_time() {
		return record_time;
	}

	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}

	public String getRecord_user() {
		return record_user;
	}

	public void setRecord_user(String record_user) {
		this.record_user = record_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
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

	public String getInner_flg() {
		return inner_flg;
	}

	public void setInner_flg(String inner_flg) {
		this.inner_flg = inner_flg;
	}

	public String getInner_pk() {
		return inner_pk;
	}

	public void setInner_pk(String inner_pk) {
		this.inner_pk = inner_pk;
	}

}
