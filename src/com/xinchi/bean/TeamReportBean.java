package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class TeamReportBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 4964467900496421592L;

	private String team_number;

	private BigDecimal sale_cost;

	private BigDecimal sys_cost;

	private String discount_flg;

	private java.math.BigDecimal discount_receivable;

	private String approved;

	private String update_user;

	private String create_user;

	private String create_time;
	private String update_time;

	public String getTeam_number() {
		return team_number;
	}

	public java.math.BigDecimal getSale_cost() {
		return sale_cost;
	}

	public java.math.BigDecimal getSys_cost() {
		return sys_cost;
	}

	public String getDiscount_flg() {
		return discount_flg;
	}

	public java.math.BigDecimal getDiscount_receivable() {
		return discount_receivable;
	}

	public String getApproved() {
		return approved;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setSale_cost(java.math.BigDecimal sale_cost) {
		this.sale_cost = sale_cost;
	}

	public void setSys_cost(java.math.BigDecimal sys_cost) {
		this.sys_cost = sys_cost;
	}

	public void setDiscount_flg(String discount_flg) {
		this.discount_flg = discount_flg;
	}

	public void setDiscount_receivable(java.math.BigDecimal discount_receivable) {
		this.discount_receivable = discount_receivable;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getCreate_time() {
		return create_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

}
