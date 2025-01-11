package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class SaleOrderBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -2697302146149234224L;

	private String team_number;

	private String client_employee_pk;

	private String product_pk;

	private String product_name;

	private String departure_date;

	private Integer days;

	private BigDecimal receivable;

	private String receivable_comment;

	private Integer adult_count;

	private BigDecimal adult_cost;

	private Integer special_count;

	private BigDecimal special_cost;

	private BigDecimal fy;

	private BigDecimal other_cost;

	private String other_cost_comment;

	private String comment;

	private String passenger_captain;

	private String independent_flg;

	private String confirm_flg;

	private String confirm_date;

	private String budget_confirm_file;

	private BigDecimal air_ticket_cost;

	private String operate_flg;

	private BigDecimal product_cost;

	private String treat_comment;

	private String product_value;

	private String lock_flg;

	private String name_confirm_status;

	private String receivable_first_flg;

	private String cancel_flg;

	private String do_confirm_date;

	private String as_adult_flg;

	private String product_model;

	private String assistant_number;

	private String sale;

	private String hotel_comment;

	private String final_type;

	private BigDecimal raise_money;

	private String raise_comment;

	private BigDecimal reduce_money;

	private String reduce_comment;

	private BigDecimal complain_money;

	private String complain_reason;

	private String complain_solution;

	private String final_status;

	private String final_confirm_file;

	private String final_voucher_file;

	private String product_manager;

	private String standard_flg;

	private BigDecimal final_receivable;
	private String final_receivable_comment;
	// DTO
	private String client_employee_name;

	// search option
	private String passenger_name;
	private String passenger_cellphone;
	private String radio_date;
	private String confirm_period;
	private List<String> order_statuses;

	private String confirm_year;

	private String sale_number;
	private String sale_name;

	private BigDecimal budget_receivable;

	private List<SaleOrderNameListBean> name_list;

	private List<String> confirm_flgs;
	private String departure_date_from;
	private String departure_date_to;
	private String confirm_date_from;
	private String confirm_date_to;
	// product operation dto
	private BigDecimal payable;
	private List<String> team_numbers;
	private String supplier_employee_pk;
	private String final_comment;

	public String getTeam_number() {
		return team_number;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public String getProduct_pk() {
		return product_pk;
	}

	public String getProduct_name() {
		return product_name;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public Integer getDays() {
		return days;
	}

	public BigDecimal getReceivable() {
		return receivable;
	}

	public String getReceivable_comment() {
		return receivable_comment;
	}

	public Integer getAdult_count() {
		return adult_count;
	}

	public BigDecimal getAdult_cost() {
		return adult_cost;
	}

	public Integer getSpecial_count() {
		return special_count;
	}

	public BigDecimal getSpecial_cost() {
		return special_cost;
	}

	public BigDecimal getFy() {
		return fy;
	}

	public BigDecimal getOther_cost() {
		return other_cost;
	}

	public String getOther_cost_comment() {
		return other_cost_comment;
	}

	public String getComment() {
		return comment;
	}

	public String getPassenger_captain() {
		return passenger_captain;
	}

	public String getIndependent_flg() {
		return independent_flg;
	}

	public String getConfirm_flg() {
		return confirm_flg;
	}

	public String getConfirm_date() {
		return confirm_date;
	}

	public String getBudget_confirm_file() {
		return budget_confirm_file;
	}

	public BigDecimal getAir_ticket_cost() {
		return air_ticket_cost;
	}

	public String getOperate_flg() {
		return operate_flg;
	}

	public BigDecimal getProduct_cost() {
		return product_cost;
	}

	public String getTreat_comment() {
		return treat_comment;
	}

	public String getProduct_value() {
		return product_value;
	}

	public String getLock_flg() {
		return lock_flg;
	}

	public String getName_confirm_status() {
		return name_confirm_status;
	}

	public String getReceivable_first_flg() {
		return receivable_first_flg;
	}

	public String getCancel_flg() {
		return cancel_flg;
	}

	public String getDo_confirm_date() {
		return do_confirm_date;
	}

	public String getAs_adult_flg() {
		return as_adult_flg;
	}

	public String getProduct_model() {
		return product_model;
	}

	public String getAssistant_number() {
		return assistant_number;
	}

	public String getSale() {
		return sale;
	}

	public String getHotel_comment() {
		return hotel_comment;
	}

	public String getFinal_type() {
		return final_type;
	}

	public BigDecimal getRaise_money() {
		return raise_money;
	}

	public String getRaise_comment() {
		return raise_comment;
	}

	public BigDecimal getReduce_money() {
		return reduce_money;
	}

	public String getReduce_comment() {
		return reduce_comment;
	}

	public BigDecimal getComplain_money() {
		return complain_money;
	}

	public String getComplain_reason() {
		return complain_reason;
	}

	public String getComplain_solution() {
		return complain_solution;
	}

	public String getFinal_status() {
		return final_status;
	}

	public String getFinal_confirm_file() {
		return final_confirm_file;
	}

	public String getFinal_voucher_file() {
		return final_voucher_file;
	}

	public String getProduct_manager() {
		return product_manager;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public void setReceivable(BigDecimal receivable) {
		this.receivable = receivable;
	}

	public void setReceivable_comment(String receivable_comment) {
		this.receivable_comment = receivable_comment;
	}

	public void setAdult_count(Integer adult_count) {
		this.adult_count = adult_count;
	}

	public void setAdult_cost(BigDecimal adult_cost) {
		this.adult_cost = adult_cost;
	}

	public void setSpecial_count(Integer special_count) {
		this.special_count = special_count;
	}

	public void setSpecial_cost(BigDecimal special_cost) {
		this.special_cost = special_cost;
	}

	public void setFy(BigDecimal fy) {
		this.fy = fy;
	}

	public void setOther_cost(BigDecimal other_cost) {
		this.other_cost = other_cost;
	}

	public void setOther_cost_comment(String other_cost_comment) {
		this.other_cost_comment = other_cost_comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setPassenger_captain(String passenger_captain) {
		this.passenger_captain = passenger_captain;
	}

	public void setIndependent_flg(String independent_flg) {
		this.independent_flg = independent_flg;
	}

	public void setConfirm_flg(String confirm_flg) {
		this.confirm_flg = confirm_flg;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

	public void setBudget_confirm_file(String budget_confirm_file) {
		this.budget_confirm_file = budget_confirm_file;
	}

	public void setAir_ticket_cost(BigDecimal air_ticket_cost) {
		this.air_ticket_cost = air_ticket_cost;
	}

	public void setOperate_flg(String operate_flg) {
		this.operate_flg = operate_flg;
	}

	public void setProduct_cost(BigDecimal product_cost) {
		this.product_cost = product_cost;
	}

	public void setTreat_comment(String treat_comment) {
		this.treat_comment = treat_comment;
	}

	public void setProduct_value(String product_value) {
		this.product_value = product_value;
	}

	public void setLock_flg(String lock_flg) {
		this.lock_flg = lock_flg;
	}

	public void setName_confirm_status(String name_confirm_status) {
		this.name_confirm_status = name_confirm_status;
	}

	public void setReceivable_first_flg(String receivable_first_flg) {
		this.receivable_first_flg = receivable_first_flg;
	}

	public void setCancel_flg(String cancel_flg) {
		this.cancel_flg = cancel_flg;
	}

	public void setDo_confirm_date(String do_confirm_date) {
		this.do_confirm_date = do_confirm_date;
	}

	public void setAs_adult_flg(String as_adult_flg) {
		this.as_adult_flg = as_adult_flg;
	}

	public void setProduct_model(String product_model) {
		this.product_model = product_model;
	}

	public void setAssistant_number(String assistant_number) {
		this.assistant_number = assistant_number;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	public void setHotel_comment(String hotel_comment) {
		this.hotel_comment = hotel_comment;
	}

	public void setFinal_type(String final_type) {
		this.final_type = final_type;
	}

	public void setRaise_money(BigDecimal raise_money) {
		this.raise_money = raise_money;
	}

	public void setRaise_comment(String raise_comment) {
		this.raise_comment = raise_comment;
	}

	public void setReduce_money(BigDecimal reduce_money) {
		this.reduce_money = reduce_money;
	}

	public void setReduce_comment(String reduce_comment) {
		this.reduce_comment = reduce_comment;
	}

	public void setComplain_money(BigDecimal complain_money) {
		this.complain_money = complain_money;
	}

	public void setComplain_reason(String complain_reason) {
		this.complain_reason = complain_reason;
	}

	public void setComplain_solution(String complain_solution) {
		this.complain_solution = complain_solution;
	}

	public void setFinal_status(String final_status) {
		this.final_status = final_status;
	}

	public void setFinal_confirm_file(String final_confirm_file) {
		this.final_confirm_file = final_confirm_file;
	}

	public void setFinal_voucher_file(String final_voucher_file) {
		this.final_voucher_file = final_voucher_file;
	}

	public void setProduct_manager(String product_manager) {
		this.product_manager = product_manager;
	}

	public String getStandard_flg() {
		return standard_flg;
	}

	public void setStandard_flg(String standard_flg) {
		this.standard_flg = standard_flg;
	}

	public String getPassenger_name() {
		return passenger_name;
	}

	public String getPassenger_cellphone() {
		return passenger_cellphone;
	}

	public String getRadio_date() {
		return radio_date;
	}

	public String getConfirm_period() {
		return confirm_period;
	}

	public List<String> getOrder_statuses() {
		return order_statuses;
	}

	public String getConfirm_year() {
		return confirm_year;
	}

	public String getSale_number() {
		return sale_number;
	}

	public String getSale_name() {
		return sale_name;
	}

	public BigDecimal getBudget_receivable() {
		return budget_receivable;
	}

	public BigDecimal getFinal_receivable() {
		return final_receivable;
	}

	public List<SaleOrderNameListBean> getName_list() {
		return name_list;
	}

	public List<String> getConfirm_flgs() {
		return confirm_flgs;
	}

	public BigDecimal getPayable() {
		return payable;
	}

	public List<String> getTeam_numbers() {
		return team_numbers;
	}

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public String getFinal_comment() {
		return final_comment;
	}

	public void setPassenger_name(String passenger_name) {
		this.passenger_name = passenger_name;
	}

	public void setPassenger_cellphone(String passenger_cellphone) {
		this.passenger_cellphone = passenger_cellphone;
	}

	public void setRadio_date(String radio_date) {
		this.radio_date = radio_date;
	}

	public void setConfirm_period(String confirm_period) {
		this.confirm_period = confirm_period;
	}

	public void setOrder_statuses(List<String> order_statuses) {
		this.order_statuses = order_statuses;
	}

	public void setConfirm_year(String confirm_year) {
		this.confirm_year = confirm_year;
	}

	public void setSale_number(String sale_number) {
		this.sale_number = sale_number;
	}

	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}

	public void setBudget_receivable(BigDecimal budget_receivable) {
		this.budget_receivable = budget_receivable;
	}

	public void setFinal_receivable(BigDecimal final_receivable) {
		this.final_receivable = final_receivable;
	}

	public void setName_list(List<SaleOrderNameListBean> name_list) {
		this.name_list = name_list;
	}

	public void setConfirm_flgs(List<String> confirm_flgs) {
		this.confirm_flgs = confirm_flgs;
	}

	public void setPayable(BigDecimal payable) {
		this.payable = payable;
	}

	public void setTeam_numbers(List<String> team_numbers) {
		this.team_numbers = team_numbers;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}

	public void setFinal_comment(String final_comment) {
		this.final_comment = final_comment;
	}

	public String getDeparture_date_from() {
		return departure_date_from;
	}

	public String getDeparture_date_to() {
		return departure_date_to;
	}

	public String getConfirm_date_from() {
		return confirm_date_from;
	}

	public String getConfirm_date_to() {
		return confirm_date_to;
	}

	public void setDeparture_date_from(String departure_date_from) {
		this.departure_date_from = departure_date_from;
	}

	public void setDeparture_date_to(String departure_date_to) {
		this.departure_date_to = departure_date_to;
	}

	public void setConfirm_date_from(String confirm_date_from) {
		this.confirm_date_from = confirm_date_from;
	}

	public void setConfirm_date_to(String confirm_date_to) {
		this.confirm_date_to = confirm_date_to;
	}

	public String getClient_employee_name() {
		return client_employee_name;
	}

	public void setClient_employee_name(String client_employee_name) {
		this.client_employee_name = client_employee_name;
	}

	public String getFinal_receivable_comment() {
		return final_receivable_comment;
	}

	public void setFinal_receivable_comment(String final_receivable_comment) {
		this.final_receivable_comment = final_receivable_comment;
	}

}
