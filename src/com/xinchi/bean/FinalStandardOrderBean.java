package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class FinalStandardOrderBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 2522076947398198206L;

	private String team_number;

	private String client_employee_pk;

	private String product_pk;

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

	private String independent_flg;

	private String create_user;

	private String confirm_date;

	private String confirm_file;

	private String name_list;

	private java.math.BigDecimal air_ticket_cost;

	private String operate_flg;

	private java.math.BigDecimal product_cost;

	private String passenger_captain;

	private String treat_comment;
	private String receivable_comment;

	private String status;

	private String voucher_file;
	private String sale;

	// 变更信息
	private String final_type;
	private BigDecimal raise_money;
	private String raise_comment;
	private BigDecimal reduce_money;
	private String reduce_comment;
	private BigDecimal complain_money;
	private String complain_reason;
	private String complain_solution;

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

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
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

	public String getName_list() {
		return name_list;
	}

	public void setName_list(String name_list) {
		this.name_list = name_list;
	}

	public java.math.BigDecimal getAir_ticket_cost() {
		return air_ticket_cost;
	}

	public void setAir_ticket_cost(java.math.BigDecimal air_ticket_cost) {
		this.air_ticket_cost = air_ticket_cost;
	}

	public String getOperate_flg() {
		return operate_flg;
	}

	public void setOperate_flg(String operate_flg) {
		this.operate_flg = operate_flg;
	}

	public java.math.BigDecimal getProduct_cost() {
		return product_cost;
	}

	public void setProduct_cost(java.math.BigDecimal product_cost) {
		this.product_cost = product_cost;
	}

	public String getPassenger_captain() {
		return passenger_captain;
	}

	public void setPassenger_captain(String passenger_captain) {
		this.passenger_captain = passenger_captain;
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

	public String getFinal_type() {
		return final_type;
	}

	public void setFinal_type(String final_type) {
		this.final_type = final_type;
	}

	public BigDecimal getRaise_money() {
		return raise_money;
	}

	public void setRaise_money(BigDecimal raise_money) {
		this.raise_money = raise_money;
	}

	public String getRaise_comment() {
		return raise_comment;
	}

	public void setRaise_comment(String raise_comment) {
		this.raise_comment = raise_comment;
	}

	public BigDecimal getReduce_money() {
		return reduce_money;
	}

	public void setReduce_money(BigDecimal reduce_money) {
		this.reduce_money = reduce_money;
	}

	public String getReduce_comment() {
		return reduce_comment;
	}

	public void setReduce_comment(String reduce_comment) {
		this.reduce_comment = reduce_comment;
	}

	public BigDecimal getComplain_money() {
		return complain_money;
	}

	public void setComplain_money(BigDecimal complain_money) {
		this.complain_money = complain_money;
	}

	public String getComplain_reason() {
		return complain_reason;
	}

	public void setComplain_reason(String complain_reason) {
		this.complain_reason = complain_reason;
	}

	public String getComplain_solution() {
		return complain_solution;
	}

	public void setComplain_solution(String complain_solution) {
		this.complain_solution = complain_solution;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVoucher_file() {
		return voucher_file;
	}

	public void setVoucher_file(String voucher_file) {
		this.voucher_file = voucher_file;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

}
