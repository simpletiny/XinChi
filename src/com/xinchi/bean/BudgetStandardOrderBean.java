package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class BudgetStandardOrderBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String team_number;

	private String client_employee_pk;

	private String product_pk;

	private String product_name;

	private String departure_date;

	private Integer days;

	private java.math.BigDecimal receivable;

	private Integer adult_count;

	private java.math.BigDecimal adult_cost;

	private Integer special_count;

	private java.math.BigDecimal special_cost;

	private java.math.BigDecimal fy;

	private java.math.BigDecimal other_cost;

	private String other_cost_comment;

	private String comment;

	private String update_user;

	private String pk;
	private String lock_flg;
	private String cancel_flg;
	private String name_confirm_status;
	private String independent_flg;

	private String confirm_flg;
	private String confirm_date;
	private String do_confirm_date;
	private String confirm_file;
	private String standard_flg = "Y";

	private String name_list;

	private BigDecimal air_ticket_cost;

	private BigDecimal product_cost;

	private String passenger_captain;
	private String product_value;
	// 名单是否被锁定
	private String name_list_lock = "0";

	private String operate_flg;
	private String treat_comment;
	private String receivable_comment;

	private String receivable_first_flg;

	// 航段信息
	// private String ticket_number;
	// private String start_city;
	// private String start_airport;
	// private String end_city;
	// private String end_airport;
	// private String off_time;
	// private String land_time;
	// private int next_day;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public String getProduct_pk() {
		return product_pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public java.math.BigDecimal getReceivable() {
		return receivable;
	}

	public void setReceivable(java.math.BigDecimal receivable) {
		this.receivable = receivable;
	}

	public Integer getAdult_count() {
		return adult_count;
	}

	public void setAdult_count(Integer adult_count) {
		this.adult_count = adult_count;
	}

	public java.math.BigDecimal getAdult_cost() {
		return adult_cost;
	}

	public void setAdult_cost(java.math.BigDecimal adult_cost) {
		this.adult_cost = adult_cost;
	}

	public Integer getSpecial_count() {
		return special_count;
	}

	public void setSpecial_count(Integer special_count) {
		this.special_count = special_count;
	}

	public java.math.BigDecimal getSpecial_cost() {
		return special_cost;
	}

	public void setSpecial_cost(java.math.BigDecimal special_cost) {
		this.special_cost = special_cost;
	}

	public java.math.BigDecimal getFy() {
		return fy;
	}

	public void setFy(java.math.BigDecimal fy) {
		this.fy = fy;
	}

	public java.math.BigDecimal getOther_cost() {
		return other_cost;
	}

	public void setOther_cost(java.math.BigDecimal other_cost) {
		this.other_cost = other_cost;
	}

	public String getOther_cost_comment() {
		return other_cost_comment;
	}

	public void setOther_cost_comment(String other_cost_comment) {
		this.other_cost_comment = other_cost_comment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getIndependent_flg() {
		return independent_flg;
	}

	public void setIndependent_flg(String independent_flg) {
		this.independent_flg = independent_flg;
	}

	public String getConfirm_flg() {
		return confirm_flg;
	}

	public void setConfirm_flg(String confirm_flg) {
		this.confirm_flg = confirm_flg;
	}

	public String getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

	public String getConfirm_file() {
		return confirm_file;
	}

	public void setConfirm_file(String confirm_file) {
		this.confirm_file = confirm_file;
	}

	public String getStandard_flg() {
		return standard_flg;
	}

	public void setStandard_flg(String standard_flg) {
		this.standard_flg = standard_flg;
	}

	public String getName_list() {
		return name_list;
	}

	public void setName_list(String name_list) {
		this.name_list = name_list;
	}

	public BigDecimal getAir_ticket_cost() {
		return air_ticket_cost;
	}

	public void setAir_ticket_cost(BigDecimal air_ticket_cost) {
		this.air_ticket_cost = air_ticket_cost;
	}

	public String getName_list_lock() {
		return name_list_lock;
	}

	public void setName_list_lock(String name_list_lock) {
		this.name_list_lock = name_list_lock;
	}

	public String getOperate_flg() {
		return operate_flg;
	}

	public void setOperate_flg(String operate_flg) {
		this.operate_flg = operate_flg;
	}

	public BigDecimal getProduct_cost() {
		return product_cost;
	}

	public void setProduct_cost(BigDecimal product_cost) {
		this.product_cost = product_cost;
	}

	public String getTreat_comment() {
		return treat_comment;
	}

	public void setTreat_comment(String treat_comment) {
		this.treat_comment = treat_comment;
	}

	public String getReceivable_comment() {
		return receivable_comment;
	}

	public void setReceivable_comment(String receivable_comment) {
		this.receivable_comment = receivable_comment;
	}

	public String getPassenger_captain() {
		return passenger_captain;
	}

	public void setPassenger_captain(String passenger_captain) {
		this.passenger_captain = passenger_captain;
	}

	public String getLock_flg() {
		return lock_flg;
	}

	public void setLock_flg(String lock_flg) {
		this.lock_flg = lock_flg;
	}

	public String getName_confirm_status() {
		return name_confirm_status;
	}

	public void setName_confirm_status(String name_confirm_status) {
		this.name_confirm_status = name_confirm_status;
	}

	public String getReceivable_first_flg() {
		return receivable_first_flg;
	}

	public void setReceivable_first_flg(String receivable_first_flg) {
		this.receivable_first_flg = receivable_first_flg;
	}

	public String getProduct_value() {
		return product_value;
	}

	public void setProduct_value(String product_value) {
		this.product_value = product_value;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getCancel_flg() {
		return cancel_flg;
	}

	public void setCancel_flg(String cancel_flg) {
		this.cancel_flg = cancel_flg;
	}

	public String getDo_confirm_date() {
		return do_confirm_date;
	}

	public void setDo_confirm_date(String do_confirm_date) {
		this.do_confirm_date = do_confirm_date;
	}
}
