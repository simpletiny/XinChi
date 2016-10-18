package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;
import java.math.BigDecimal;

public class CardBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String account;
	private String number;

	private String name;

	private String bank;

	private String type;

	private BigDecimal init_money;

	private BigDecimal balance;

	private String comment;

	private String pk;

	private String create_user;

	private String update_user;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public BigDecimal getInit_money() {
		return init_money;
	}

	public void setInit_money(BigDecimal init_money) {
		this.init_money = init_money;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
