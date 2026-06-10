package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class AirOtherPaymentDto implements Serializable {

	private static final long serialVersionUID = -4039509227898253863L;
	private BigDecimal money;
	private BigDecimal receive_money;
	private BigDecimal pay_money;
	private String product_manager_number;
	private String product_manager_name;

	private String belong_month;

	// option
	private String belong_year;

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public String getProduct_manager_name() {
		return product_manager_name;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public void setProduct_manager_name(String product_manager_name) {
		this.product_manager_name = product_manager_name;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getReceive_money() {
		return receive_money;
	}

	public BigDecimal getPay_money() {
		return pay_money;
	}

	public void setReceive_money(BigDecimal receive_money) {
		this.receive_money = receive_money;
	}

	public void setPay_money(BigDecimal pay_money) {
		this.pay_money = pay_money;
	}

	public String getBelong_month() {
		return belong_month;
	}

	public void setBelong_month(String belong_month) {
		this.belong_month = belong_month;
	}

	public String getBelong_year() {
		return belong_year;
	}

	public void setBelong_year(String belong_year) {
		this.belong_year = belong_year;
	}

}
