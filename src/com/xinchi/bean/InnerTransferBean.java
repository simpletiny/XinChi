package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class InnerTransferBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 3943391171290867047L;

	private String from_account;
	private String to_account;
	private BigDecimal money;
	private String exchange_account;
	private BigDecimal exchange_money;
	private String comment;
	private String time;

	public String getFrom_account() {
		return from_account;
	}

	public void setFrom_account(String from_account) {
		this.from_account = from_account;
	}

	public String getTo_account() {
		return to_account;
	}

	public void setTo_account(String to_account) {
		this.to_account = to_account;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getExchange_account() {
		return exchange_account;
	}

	public void setExchange_account(String exchange_account) {
		this.exchange_account = exchange_account;
	}

	public BigDecimal getExchange_money() {
		return exchange_money;
	}

	public void setExchange_money(BigDecimal exchange_money) {
		this.exchange_money = exchange_money;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
