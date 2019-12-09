package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class MeterDto implements Serializable {

	private static final long serialVersionUID = 2570892410866318585L;

	private BigDecimal receivable = BigDecimal.ZERO;
	private BigDecimal warning = BigDecimal.ZERO;
	private BigDecimal sum_reimbursement = BigDecimal.ZERO;
	private BigDecimal point_money_deduct = BigDecimal.ZERO;
	private BigDecimal month_reimbursement = BigDecimal.ZERO;
	private BigDecimal bad = BigDecimal.ZERO;
	private BigDecimal month_dead = BigDecimal.ZERO;
	private BigDecimal sum_dead = BigDecimal.ZERO;
	private BigDecimal day_hold = BigDecimal.ZERO;
	private BigDecimal bad_interest = BigDecimal.ZERO;

	private float back_score = 0;
	private int score = 0;
	private int month_score = 0;

	public BigDecimal getReceivable() {
		return receivable;
	}

	public BigDecimal getWarning() {
		return warning;
	}

	public int getScore() {
		return score;
	}

	public int getMonth_score() {
		return month_score;
	}

	public void setReceivable(BigDecimal receivable) {
		this.receivable = receivable;
	}

	public void setWarning(BigDecimal warning) {
		this.warning = warning;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setMonth_score(int month_score) {
		this.month_score = month_score;
	}

	public BigDecimal getSum_reimbursement() {
		return sum_reimbursement;
	}

	public void setSum_reimbursement(BigDecimal sum_reimbursement) {
		this.sum_reimbursement = sum_reimbursement;
	}

	public BigDecimal getPoint_money_deduct() {
		return point_money_deduct;
	}

	public void setPoint_money_deduct(BigDecimal point_money_deduct) {
		this.point_money_deduct = point_money_deduct;
	}

	public float getBack_score() {
		return back_score;
	}

	public void setBack_score(float back_score) {
		this.back_score = back_score;
	}

	public BigDecimal getMonth_reimbursement() {
		return month_reimbursement;
	}

	public void setMonth_reimbursement(BigDecimal month_reimbursement) {
		this.month_reimbursement = month_reimbursement;
	}

	public BigDecimal getBad() {
		return bad;
	}

	public BigDecimal getMonth_dead() {
		return month_dead;
	}

	public void setBad(BigDecimal bad) {
		this.bad = bad;
	}

	public void setMonth_dead(BigDecimal month_dead) {
		this.month_dead = month_dead;
	}

	public BigDecimal getSum_dead() {
		return sum_dead;
	}

	public void setSum_dead(BigDecimal sum_dead) {
		this.sum_dead = sum_dead;
	}

	public BigDecimal getDay_hold() {
		return day_hold;
	}

	public void setDay_hold(BigDecimal day_hold) {
		this.day_hold = day_hold;
	}

	public BigDecimal getBad_interest() {
		return bad_interest;
	}

	public void setBad_interest(BigDecimal bad_interest) {
		this.bad_interest = bad_interest;
	}

}