package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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

	private String inner_pk;
	private String from_time;
	private String to_time;
	private BigDecimal from_money;
	private BigDecimal to_money;
	private String exchange_type;

	// 搜索条件
	private String from_date;
	private String to_date;
	private String from_month;
	private String to_month;
	private List<String> exchange_types;

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

	public String getFrom_time() {
		return from_time;
	}

	public void setFrom_time(String from_time) {
		this.from_time = from_time;
	}

	public String getTo_time() {
		return to_time;
	}

	public void setTo_time(String to_time) {
		this.to_time = to_time;
	}

	public BigDecimal getFrom_money() {
		return from_money;
	}

	public void setFrom_money(BigDecimal from_money) {
		this.from_money = from_money;
	}

	public BigDecimal getTo_money() {
		return to_money;
	}

	public void setTo_money(BigDecimal to_money) {
		this.to_money = to_money;
	}

	public String getExchange_type() {
		return exchange_type;
	}

	public void setExchange_type(String exchange_type) {
		this.exchange_type = exchange_type;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public String getFrom_month() {
		return from_month;
	}

	public void setFrom_month(String from_month) {
		this.from_month = from_month;
	}

	public String getTo_month() {
		return to_month;
	}

	public void setTo_month(String to_month) {
		this.to_month = to_month;
	}

	public List<String> getExchange_types() {
		return exchange_types;
	}

	public void setExchange_types(List<String> exchange_types) {
		this.exchange_types = exchange_types;
	}

	public String getInner_pk() {
		return inner_pk;
	}

	public void setInner_pk(String inner_pk) {
		this.inner_pk = inner_pk;
	}

}
