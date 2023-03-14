package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ProductSaleBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 6669662868643750767L;

	private int order_cnt;

	private java.math.BigDecimal score;

	private int sum_people;

	private String departure_month;

	private String sale_number;

	private String sale_name;

	private String product_manager_number;

	private String product_manager_name;

	public int getOrder_cnt() {
		return order_cnt;
	}

	public java.math.BigDecimal getScore() {
		return score;
	}

	public int getSum_people() {
		return sum_people;
	}

	public String getSale_number() {
		return sale_number;
	}

	public String getSale_name() {
		return sale_name;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public String getProduct_manager_name() {
		return product_manager_name;
	}

	public void setOrder_cnt(int order_cnt) {
		this.order_cnt = order_cnt;
	}

	public void setScore(java.math.BigDecimal score) {
		this.score = score;
	}

	public void setSum_people(int sum_people) {
		this.sum_people = sum_people;
	}

	public void setSale_number(String sale_number) {
		this.sale_number = sale_number;
	}

	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public void setProduct_manager_name(String product_manager_name) {
		this.product_manager_name = product_manager_name;
	}

	public String getDeparture_month() {
		return departure_month;
	}

	public void setDeparture_month(String departure_month) {
		this.departure_month = departure_month;
	}

}
