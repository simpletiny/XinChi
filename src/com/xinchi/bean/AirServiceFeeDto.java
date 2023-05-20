package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class AirServiceFeeDto implements Serializable {
	private static final long serialVersionUID = 3962832028908478874L;
	private String payable_pk;
	private String first_month;
	private BigDecimal payable;
	private BigDecimal paid;
	private String product_manager_number;
	private String product_manager_name;

	private int people_count;

	private String sale_number;
	private String sale_name;

	public String getPayable_pk() {
		return payable_pk;
	}

	public String getFirst_month() {
		return first_month;
	}

	public BigDecimal getPaid() {
		return paid;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public String getProduct_manager_name() {
		return product_manager_name;
	}

	public int getPeople_count() {
		return people_count;
	}

	public String getSale_number() {
		return sale_number;
	}

	public String getSale_name() {
		return sale_name;
	}

	public void setPayable_pk(String payable_pk) {
		this.payable_pk = payable_pk;
	}

	public void setFirst_month(String first_month) {
		this.first_month = first_month;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public void setProduct_manager_name(String product_manager_name) {
		this.product_manager_name = product_manager_name;
	}

	public void setPeople_count(int people_count) {
		this.people_count = people_count;
	}

	public void setSale_number(String sale_number) {
		this.sale_number = sale_number;
	}

	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}

	public BigDecimal getPayable() {
		return payable;
	}

	public void setPayable(BigDecimal payable) {
		this.payable = payable;
	}

}
