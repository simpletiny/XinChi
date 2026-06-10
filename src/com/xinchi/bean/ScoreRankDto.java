package com.xinchi.bean;

import java.io.Serializable;

public class ScoreRankDto implements Serializable {

	private static final long serialVersionUID = -6973571180061713718L;
	private String sale_number;
	private String confirm_month;
	private int rank_first_score;
	private int rank_middle_score;
	private int rank_last_score;
	private int rank_score;

	public String getSale_number() {
		return sale_number;
	}

	public void setSale_number(String sale_number) {
		
		this.sale_number = sale_number;
	}

	public String getConfirm_month() {
		return confirm_month;
	}

	public void setConfirm_month(String confirm_month) {
		this.confirm_month = confirm_month;
	}

	public int getRank_first_score() {
		return rank_first_score;
	}

	public void setRank_first_score(int rank_first_score) {
		this.rank_first_score = rank_first_score;
	}

	public int getRank_middle_score() {
		return rank_middle_score;
	}

	public void setRank_middle_score(int rank_middle_score) {
		this.rank_middle_score = rank_middle_score;
	}

	public int getRank_last_score() {
		return rank_last_score;
	}

	public void setRank_last_score(int rank_last_score) {
		this.rank_last_score = rank_last_score;
	}

	public int getRank_score() {
		return rank_score;
	}

	public void setRank_score(int rank_score) {
		this.rank_score = rank_score;
	}
}