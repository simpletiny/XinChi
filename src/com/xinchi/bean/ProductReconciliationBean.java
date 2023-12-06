package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class ProductReconciliationBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 2286975938152274827L;

	private String type;

	private String belong_month;

	private BigDecimal money;

	private String product_manager_number;

	private String status;
	private String comment;
	// DTO
	private String create_user_name;
	private String product_manager_name;

	// options
	private List<String> statuses;

	private BigDecimal money_from;
	private BigDecimal money_to;

	public String getType() {
		return type;
	}

	public String getBelong_month() {
		return belong_month;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public String getComment() {
		return comment;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setBelong_month(String belong_month) {
		this.belong_month = belong_month;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
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

	public String getCreate_user_name() {
		return create_user_name;
	}

	public String getProduct_manager_name() {
		return product_manager_name;
	}

	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}

	public void setProduct_manager_name(String product_manager_name) {
		this.product_manager_name = product_manager_name;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}

	public BigDecimal getMoney_from() {
		return money_from;
	}

	public BigDecimal getMoney_to() {
		return money_to;
	}

	public void setMoney_from(BigDecimal money_from) {
		this.money_from = money_from;
	}

	public void setMoney_to(BigDecimal money_to) {
		this.money_to = money_to;
	}

}
