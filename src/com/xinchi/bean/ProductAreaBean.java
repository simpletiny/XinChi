package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ProductAreaBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 5632262157836302898L;

	private int order_cnt;

	private java.math.BigDecimal score;

	private int sum_people;

	private String confirm_month;

	private String product_manager_number;

	private String product_manager_name;

	private String area;

	public int getOrder_cnt() {
		return order_cnt;
	}

	public java.math.BigDecimal getScore() {
		return score;
	}

	public int getSum_people() {
		return sum_people;
	}

	public String getConfirm_month() {
		return confirm_month;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public String getProduct_manager_name() {
		return product_manager_name;
	}

	public String getArea() {
		return area;
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

	public void setConfirm_month(String confirm_month) {
		this.confirm_month = confirm_month;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public void setProduct_manager_name(String product_manager_name) {
		this.product_manager_name = product_manager_name;
	}

	public void setArea(String area) {
		this.area = area;
	}

}
