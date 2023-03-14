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

	private int first_people;
	private BigDecimal first_score;
	private int middle_people;
	private BigDecimal middle_score;
	private int last_people;
	private BigDecimal last_score;

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

	public int getFirst_people() {
		return first_people;
	}

	public BigDecimal getFirst_score() {
		return first_score;
	}

	public int getMiddle_people() {
		return middle_people;
	}

	public BigDecimal getMiddle_score() {
		return middle_score;
	}

	public int getLast_people() {
		return last_people;
	}

	public BigDecimal getLast_score() {
		return last_score;
	}

	public void setFirst_people(int first_people) {
		this.first_people = first_people;
	}

	public void setFirst_score(BigDecimal first_score) {
		this.first_score = first_score;
	}

	public void setMiddle_people(int middle_people) {
		this.middle_people = middle_people;
	}

	public void setMiddle_score(BigDecimal middle_score) {
		this.middle_score = middle_score;
	}

	public void setLast_people(int last_people) {
		this.last_people = last_people;
	}

	public void setLast_score(BigDecimal last_score) {
		this.last_score = last_score;
	}
}
