package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccumulateBalanceBean implements Serializable {
	private static final long serialVersionUID = -2775866734776236362L;

	private String user_pk;

	private String user_number;

	private String date;

	private BigDecimal receivable;

	private BigDecimal receivable_balance;

	private BigDecimal payable;

	private BigDecimal payable_balance;

	private Integer pk;

	public String getUser_pk() {
		return user_pk;
	}

	public String getUser_number() {
		return user_number;
	}

	public String getDate() {
		return date;
	}

	public BigDecimal getReceivable() {
		return receivable;
	}

	public BigDecimal getReceivable_balance() {
		return receivable_balance;
	}

	public BigDecimal getPayable() {
		return payable;
	}

	public BigDecimal getPayable_balance() {
		return payable_balance;
	}

	public Integer getPk() {
		return pk;
	}

	public void setUser_pk(String user_pk) {
		this.user_pk = user_pk;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setReceivable(BigDecimal receivable) {
		this.receivable = receivable;
	}

	public void setReceivable_balance(BigDecimal receivable_balance) {
		this.receivable_balance = receivable_balance;
	}

	public void setPayable(BigDecimal payable) {
		this.payable = payable;
	}

	public void setPayable_balance(BigDecimal payable_balance) {
		this.payable_balance = payable_balance;
	}

	public void setPk(Integer pk) {
		this.pk = pk;
	}

}
