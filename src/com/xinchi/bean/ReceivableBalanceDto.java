package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class ReceivableBalanceDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = -8484133369429845155L;

	private String user_pk;
	private String user_name;
	private String user_number;
	private int all_count;
	private BigDecimal all_budget_balance;
	private BigDecimal all_final_balance;
	private BigDecimal all_balance;

	public String getUser_pk() {
		return user_pk;
	}

	public String getUser_name() {
		return user_name;
	}

	public String getUser_number() {
		return user_number;
	}

	public int getAll_count() {
		return all_count;
	}

	public BigDecimal getAll_budget_balance() {
		return all_budget_balance;
	}

	public BigDecimal getAll_final_balance() {
		return all_final_balance;
	}

	public BigDecimal getAll_balance() {
		return all_balance;
	}

	public void setUser_pk(String user_pk) {
		this.user_pk = user_pk;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public void setAll_count(int all_count) {
		this.all_count = all_count;
	}

	public void setAll_budget_balance(BigDecimal all_budget_balance) {
		this.all_budget_balance = all_budget_balance;
	}

	public void setAll_final_balance(BigDecimal all_final_balance) {
		this.all_final_balance = all_final_balance;
	}

	public void setAll_balance(BigDecimal all_balance) {
		this.all_balance = all_balance;
	}

}
