package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class PaidDetailSummary extends SupperBO implements Serializable {

	private static final long serialVersionUID = -5474410767447181989L;
	private BigDecimal money;
	private String pay_user;
	private String apply_user;
	private String apply_date;
	private String approval_user;
	private String approval_date;

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getPay_user() {
		return pay_user;
	}

	public void setPay_user(String pay_user) {
		this.pay_user = pay_user;
	}

	public String getApply_user() {
		return apply_user;
	}

	public void setApply_user(String apply_user) {
		this.apply_user = apply_user;
	}

	public String getApply_date() {
		return apply_date;
	}

	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}

	public String getApproval_user() {
		return approval_user;
	}

	public void setApproval_user(String approval_user) {
		this.approval_user = approval_user;
	}

	public String getApproval_date() {
		return approval_date;
	}

	public void setApproval_date(String approval_date) {
		this.approval_date = approval_date;
	}
}
