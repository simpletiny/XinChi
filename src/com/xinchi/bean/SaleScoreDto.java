package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class SaleScoreDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = -8643890019509523613L;

	private BigDecimal score;
	private String sale_number;
	private String sale_name;
	private String confirm_month;
	private int sum_people;

	private float discount;

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getSale_number() {
		return sale_number;
	}

	public void setSale_number(String sale_number) {
		this.sale_number = sale_number;
	}

	public String getSale_name() {
		return sale_name;
	}

	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}

	public String getConfirm_month() {
		return confirm_month;
	}

	public void setConfirm_month(String confirm_month) {
		this.confirm_month = confirm_month;
	}

	public int getSum_people() {
		return sum_people;
	}

	public void setSum_people(int sum_people) {
		this.sum_people = sum_people;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}
}
