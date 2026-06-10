package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class OrderReportDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = -5328689954534388968L;

	private String team_number;
	private String product_order_number;
	private String order_type;
	private String client_employee_pk;
	private String client_employee_name;
	private String departure_date;
	private String product_name;
	private String product_line;
	private String people_count;
	private BigDecimal receivable = BigDecimal.ZERO;
	private BigDecimal air_ticket_cost = BigDecimal.ZERO;
	private BigDecimal train_ticket_cost = BigDecimal.ZERO;
	private BigDecimal product_cost = BigDecimal.ZERO;
	private BigDecimal other_cost = BigDecimal.ZERO;
	private BigDecimal fy = BigDecimal.ZERO;
	private BigDecimal gross_profit = BigDecimal.ZERO;
	private BigDecimal per_profit = BigDecimal.ZERO;
	private String confirm_date;
	private String sale_name;
	private String sale_number;
	private BigDecimal team_other_pay = BigDecimal.ZERO;
	private BigDecimal team_other_receive = BigDecimal.ZERO;
	private BigDecimal air_deduct = BigDecimal.ZERO;
	private BigDecimal exchange = BigDecimal.ZERO;
	
	private int order_count = 0;

	private String discount_flg;
	private BigDecimal discount_receivable = BigDecimal.ZERO;
	private BigDecimal sale_cost = BigDecimal.ZERO;
	private BigDecimal sys_cost = BigDecimal.ZERO;
	private String approved;

	private String departure_date_from;
	private String departure_date_to;
	private String confirm_date_from;
	private String confirm_date_to;

	private String independent_flg;

	private String product_manager_number;

	private BigDecimal cost_adjustment = BigDecimal.ZERO;
	private BigDecimal allocable_money = BigDecimal.ZERO;
	private BigDecimal allocable_applied = BigDecimal.ZERO;
	private BigDecimal allocable_balance = BigDecimal.ZERO;
	private List<String> order_types;

	private List<String> report_statuses;

	private String product_final_flg;

	private String confirm_month;

	private int adult_count = 0;
	private int special_count = 0;
	private BigDecimal tail98 = BigDecimal.ZERO;
	private BigDecimal other_pay = BigDecimal.ZERO;
	private BigDecimal other_receive = BigDecimal.ZERO;
	
	
	private BigDecimal no_b_pay = BigDecimal.ZERO;;
	private BigDecimal no_b_receive = BigDecimal.ZERO;

	private BigDecimal recon_pay = BigDecimal.ZERO;
	private BigDecimal recon_receive = BigDecimal.ZERO;

	private List<String> standard_flgs;
	private List<String> independent_flgs;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getProduct_order_number() {
		return product_order_number;
	}

	public void setProduct_order_number(String product_order_number) {
		this.product_order_number = product_order_number;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public String getClient_employee_name() {
		return client_employee_name;
	}

	public void setClient_employee_name(String client_employee_name) {
		this.client_employee_name = client_employee_name;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_line() {
		return product_line;
	}

	public void setProduct_line(String product_line) {
		this.product_line = product_line;
	}

	public String getPeople_count() {
		return people_count;
	}

	public void setPeople_count(String people_count) {
		this.people_count = people_count;
	}

	public BigDecimal getReceivable() {
		return receivable;
	}

	public void setReceivable(BigDecimal receivable) {
		this.receivable = receivable;
	}

	public BigDecimal getAir_ticket_cost() {
		return air_ticket_cost;
	}

	public void setAir_ticket_cost(BigDecimal air_ticket_cost) {
		this.air_ticket_cost = air_ticket_cost;
	}

	public BigDecimal getTrain_ticket_cost() {
		return train_ticket_cost;
	}

	public void setTrain_ticket_cost(BigDecimal train_ticket_cost) {
		this.train_ticket_cost = train_ticket_cost;
	}

	public BigDecimal getProduct_cost() {
		return product_cost;
	}

	public void setProduct_cost(BigDecimal product_cost) {
		this.product_cost = product_cost;
	}

	public BigDecimal getOther_cost() {
		return other_cost;
	}

	public void setOther_cost(BigDecimal other_cost) {
		this.other_cost = other_cost;
	}

	public BigDecimal getFy() {
		return fy;
	}

	public void setFy(BigDecimal fy) {
		this.fy = fy;
	}

	public BigDecimal getGross_profit() {
		return gross_profit;
	}

	public void setGross_profit(BigDecimal gross_profit) {
		this.gross_profit = gross_profit;
	}

	public BigDecimal getPer_profit() {
		return per_profit;
	}

	public void setPer_profit(BigDecimal per_profit) {
		this.per_profit = per_profit;
	}

	public String getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

	public String getSale_name() {
		return sale_name;
	}

	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}

	public String getSale_number() {
		return sale_number;
	}

	public void setSale_number(String sale_number) {
		this.sale_number = sale_number;
	}

	public BigDecimal getAir_deduct() {
		return air_deduct;
	}

	public void setAir_deduct(BigDecimal air_deduct) {
		this.air_deduct = air_deduct;
	}

	public BigDecimal getExchange() {
		return exchange;
	}

	public void setExchange(BigDecimal exchange) {
		this.exchange = exchange;
	}

	public String getDiscount_flg() {
		return discount_flg;
	}

	public void setDiscount_flg(String discount_flg) {
		this.discount_flg = discount_flg;
	}

	public BigDecimal getDiscount_receivable() {
		return discount_receivable;
	}

	public void setDiscount_receivable(BigDecimal discount_receivable) {
		this.discount_receivable = discount_receivable;
	}

	public BigDecimal getSale_cost() {
		return sale_cost;
	}

	public void setSale_cost(BigDecimal sale_cost) {
		this.sale_cost = sale_cost;
	}

	public BigDecimal getSys_cost() {
		return sys_cost;
	}

	public void setSys_cost(BigDecimal sys_cost) {
		this.sys_cost = sys_cost;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
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

	public String getIndependent_flg() {
		return independent_flg;
	}

	public void setIndependent_flg(String independent_flg) {
		this.independent_flg = independent_flg;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public BigDecimal getCost_adjustment() {
		return cost_adjustment;
	}

	public void setCost_adjustment(BigDecimal cost_adjustment) {
		this.cost_adjustment = cost_adjustment;
	}

	public BigDecimal getAllocable_money() {
		return allocable_money;
	}

	public void setAllocable_money(BigDecimal allocable_money) {
		this.allocable_money = allocable_money;
	}

	public BigDecimal getAllocable_applied() {
		return allocable_applied;
	}

	public void setAllocable_applied(BigDecimal allocable_applied) {
		this.allocable_applied = allocable_applied;
	}

	public BigDecimal getAllocable_balance() {
		return allocable_balance;
	}

	public void setAllocable_balance(BigDecimal allocable_balance) {
		this.allocable_balance = allocable_balance;
	}

	public List<String> getOrder_types() {
		return order_types;
	}

	public void setOrder_types(List<String> order_types) {
		this.order_types = order_types;
	}

	public List<String> getReport_statuses() {
		return report_statuses;
	}

	public void setReport_statuses(List<String> report_statuses) {
		this.report_statuses = report_statuses;
	}

	public String getProduct_final_flg() {
		return product_final_flg;
	}

	public void setProduct_final_flg(String product_final_flg) {
		this.product_final_flg = product_final_flg;
	}

	public String getConfirm_month() {
		return confirm_month;
	}

	public void setConfirm_month(String confirm_month) {
		this.confirm_month = confirm_month;
	}

	public int getAdult_count() {
		return adult_count;
	}

	public void setAdult_count(int adult_count) {
		this.adult_count = adult_count;
	}

	public int getSpecial_count() {
		return special_count;
	}

	public void setSpecial_count(int special_count) {
		this.special_count = special_count;
	}

	public BigDecimal getTail98() {
		return tail98;
	}

	public void setTail98(BigDecimal tail98) {
		this.tail98 = tail98;
	}

	public BigDecimal getOther_pay() {
		return other_pay;
	}

	public void setOther_pay(BigDecimal other_pay) {
		this.other_pay = other_pay;
	}

	public BigDecimal getOther_receive() {
		return other_receive;
	}

	public void setOther_receive(BigDecimal other_receive) {
		this.other_receive = other_receive;
	}

	public List<String> getStandard_flgs() {
		return standard_flgs;
	}

	public void setStandard_flgs(List<String> standard_flgs) {
		this.standard_flgs = standard_flgs;
	}

	public BigDecimal getTeam_other_pay() {
		return team_other_pay;
	}

	public void setTeam_other_pay(BigDecimal team_other_pay) {
		this.team_other_pay = team_other_pay;
	}

	public BigDecimal getTeam_other_receive() {
		return team_other_receive;
	}

	public void setTeam_other_receive(BigDecimal team_other_receive) {
		this.team_other_receive = team_other_receive;
	}

	public BigDecimal getNo_b_pay() {
		return no_b_pay;
	}

	public void setNo_b_pay(BigDecimal no_b_pay) {
		this.no_b_pay = no_b_pay;
	}

	public BigDecimal getNo_b_receive() {
		return no_b_receive;
	}

	public void setNo_b_receive(BigDecimal no_b_receive) {
		this.no_b_receive = no_b_receive;
	}

	public BigDecimal getRecon_pay() {
		return recon_pay;
	}

	public void setRecon_pay(BigDecimal recon_pay) {
		this.recon_pay = recon_pay;
	}

	public BigDecimal getRecon_receive() {
		return recon_receive;
	}

	public void setRecon_receive(BigDecimal recon_receive) {
		this.recon_receive = recon_receive;
	}

	public int getOrder_count() {
		return order_count;
	}

	public void setOrder_count(int order_count) {
		this.order_count = order_count;
	}

	public List<String> getIndependent_flgs() {
		return independent_flgs;
	}

	public void setIndependent_flgs(List<String> independent_flgs) {
		this.independent_flgs = independent_flgs;
	}

}
