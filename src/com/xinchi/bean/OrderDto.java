package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class OrderDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String team_number;

	private String client_employee_pk;

	private String client_employee_name;
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
	private String create_user_number;

	private String pk;

	private String independent_flg;

	private String confirm_flg;
	private String standard_flg;

	private String confirm_date;

	private BigDecimal air_ticket_cost;
	private BigDecimal product_cost;
	private String status;
	private String back_days;
	private String people_count;
	private String product_manager_number;
	private String product_manager;
	private String confirm_file;

	private String departure_date_from;
	private String departure_date_to;
	private String confirm_date_from;
	private String confirm_date_to;

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

	public String getClient_employee_name() {
		return client_employee_name;
	}

	public void setClient_employee_name(String client_employee_name) {
		this.client_employee_name = client_employee_name;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getCreate_user_number() {
		return create_user_number;
	}

	public void setCreate_user_number(String create_user_number) {
		this.create_user_number = create_user_number;
	}

	public String getStandard_flg() {
		return standard_flg;
	}

	public void setStandard_flg(String standard_flg) {
		this.standard_flg = standard_flg;
	}

	public String getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBack_days() {
		return back_days;
	}

	public void setBack_days(String back_days) {
		this.back_days = back_days;
	}

	public String getPeople_count() {
		return people_count;
	}

	public void setPeople_count(String people_count) {
		this.people_count = people_count;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public String getProduct_manager() {
		return product_manager;
	}

	public void setProduct_manager(String product_manager) {
		this.product_manager = product_manager;
	}

	public String getConfirm_file() {
		return confirm_file;
	}

	public void setConfirm_file(String confirm_file) {
		this.confirm_file = confirm_file;
	}

	public String getDeparture_date_from() {
		return departure_date_from;
	}

	public void setDeparture_date_from(String departure_date_from) {
		this.departure_date_from = departure_date_from;
	}

	public String getDeparture_date_to() {
		return departure_date_to;
	}

	public void setDeparture_date_to(String departure_date_to) {
		this.departure_date_to = departure_date_to;
	}

	public String getConfirm_date_from() {
		return confirm_date_from;
	}

	public void setConfirm_date_from(String confirm_date_from) {
		this.confirm_date_from = confirm_date_from;
	}

	public String getConfirm_date_to() {
		return confirm_date_to;
	}

	public void setConfirm_date_to(String confirm_date_to) {
		this.confirm_date_to = confirm_date_to;
	}

	public BigDecimal getAir_ticket_cost() {
		return air_ticket_cost;
	}

	public void setAir_ticket_cost(BigDecimal air_ticket_cost) {
		this.air_ticket_cost = air_ticket_cost;
	}

	public BigDecimal getProduct_cost() {
		return product_cost;
	}

	public void setProduct_cost(BigDecimal product_cost) {
		this.product_cost = product_cost;
	}

}
