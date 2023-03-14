package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class OrderDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String team_number;

	private String client_employee_pk;

	private String client_employee_name;
	private String product_pk;
	private String product_name;
	private String product_model;

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

	private String lock_flg;

	private String name_confirm_status;

	private String departure_date_from;
	private String departure_date_to;
	private String confirm_date_from;
	private String confirm_date_to;
	private String passenger;

	private String passenger_captain;
	private String treat_comment;
	private String receivable_comment;
	private String client_name;
	private String ticket_number;
	private String start_city;
	private String start_airport;
	private String end_city;
	private String end_airport;
	private String off_time;
	private String land_time;
	private Integer next_day;
	private BigDecimal balance;
	private String product_value;

	private String receivable_first_flg;

	// about final order
	private String final_type;
	private BigDecimal raise_money;
	private String raise_comment;
	private BigDecimal reduce_money;
	private String reduce_comment;
	private BigDecimal complain_money;
	private String complain_reason;
	private String complain_solution;
	private String voucher_file;
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
	private BigDecimal final_receivable;

	private List<SaleOrderNameListBean> name_list;

	private List<String> confirm_flgs;

	// product operation dto
	private BigDecimal payable;
	private List<String> team_numbers;
	private String supplier_employee_pk;

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

	public String getPassenger() {
		return passenger;
	}

	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getTicket_number() {
		return ticket_number;
	}

	public void setTicket_number(String ticket_number) {
		this.ticket_number = ticket_number;
	}

	public String getStart_city() {
		return start_city;
	}

	public void setStart_city(String start_city) {
		this.start_city = start_city;
	}

	public String getStart_airport() {
		return start_airport;
	}

	public void setStart_airport(String start_airport) {
		this.start_airport = start_airport;
	}

	public String getEnd_city() {
		return end_city;
	}

	public void setEnd_city(String end_city) {
		this.end_city = end_city;
	}

	public String getEnd_airport() {
		return end_airport;
	}

	public void setEnd_airport(String end_airport) {
		this.end_airport = end_airport;
	}

	public String getOff_time() {
		return off_time;
	}

	public void setOff_time(String off_time) {
		this.off_time = off_time;
	}

	public String getLand_time() {
		return land_time;
	}

	public void setLand_time(String land_time) {
		this.land_time = land_time;
	}

	public Integer getNext_day() {
		return next_day;
	}

	public void setNext_day(Integer next_day) {
		this.next_day = next_day;
	}

	public String getPassenger_name() {
		return passenger_name;
	}

	public void setPassenger_name(String passenger_name) {
		this.passenger_name = passenger_name;
	}

	public String getPassenger_cellphone() {
		return passenger_cellphone;
	}

	public void setPassenger_cellphone(String passenger_cellphone) {
		this.passenger_cellphone = passenger_cellphone;
	}

	public String getRadio_date() {
		return radio_date;
	}

	public void setRadio_date(String radio_date) {
		this.radio_date = radio_date;
	}

	public String getConfirm_period() {
		return confirm_period;
	}

	public void setConfirm_period(String confirm_period) {
		this.confirm_period = confirm_period;
	}

	public List<String> getOrder_statuses() {
		return order_statuses;
	}

	public void setOrder_statuses(List<String> order_statuses) {
		this.order_statuses = order_statuses;
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

	public String getVoucher_file() {
		return voucher_file;
	}

	public void setVoucher_file(String voucher_file) {
		this.voucher_file = voucher_file;
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

	public BigDecimal getBudget_receivable() {
		return budget_receivable;
	}

	public void setBudget_receivable(BigDecimal budget_receivable) {
		this.budget_receivable = budget_receivable;
	}

	public BigDecimal getFinal_receivable() {
		return final_receivable;
	}

	public void setFinal_receivable(BigDecimal final_receivable) {
		this.final_receivable = final_receivable;
	}

	public String getConfirm_year() {
		return confirm_year;
	}

	public void setConfirm_year(String confirm_year) {
		this.confirm_year = confirm_year;
	}

	public String getLock_flg() {
		return lock_flg;
	}

	public void setLock_flg(String lock_flg) {
		this.lock_flg = lock_flg;
	}

	public List<SaleOrderNameListBean> getName_list() {
		return name_list;
	}

	public void setName_list(List<SaleOrderNameListBean> name_list) {
		this.name_list = name_list;
	}

	public String getName_confirm_status() {
		return name_confirm_status;
	}

	public void setName_confirm_status(String name_confirm_status) {
		this.name_confirm_status = name_confirm_status;
	}

	public String getProduct_model() {
		return product_model;
	}

	public void setProduct_model(String product_model) {
		this.product_model = product_model;
	}

	public List<String> getConfirm_flgs() {
		return confirm_flgs;
	}

	public void setConfirm_flgs(List<String> confirm_flgs) {
		this.confirm_flgs = confirm_flgs;
	}

	public BigDecimal getPayable() {
		return payable;
	}

	public void setPayable(BigDecimal payable) {
		this.payable = payable;
	}

	public List<String> getTeam_numbers() {
		return team_numbers;
	}

	public void setTeam_numbers(List<String> team_numbers) {
		this.team_numbers = team_numbers;
	}

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
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

}
