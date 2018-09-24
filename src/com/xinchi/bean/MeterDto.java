package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class MeterDto implements Serializable {

	private static final long serialVersionUID = 2570892410866318585L;

	private BigDecimal receivable = BigDecimal.ZERO;
	private BigDecimal warning = BigDecimal.ZERO;
	private BigDecimal sum_reimbursement = BigDecimal.ZERO;
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
}
