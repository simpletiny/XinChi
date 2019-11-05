package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class OrderReportDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = -5328689954534388968L;

	private String team_number;
	private String order_type;
	private String client_employee_pk;
	private String client_employee_name;
	private String departure_date;
	private String product_name;
	private String people_count;
	private String receivable;
	private String air_ticket_cost;
	private String train_ticket_cost;
	private String product_cost;
	private String other_cost;
	private String fy;
	private String gross_profit;
	private String per_profit;
	private String confirm_date;
	private String sale_name;
	private String sale_number;

	private String departure_date_from;
	private String departure_date_to;
	private String confirm_date_from;
	private String confirm_date_to;

	private String product_manager_number;

	private List<String> order_types;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
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

	public String getPeople_count() {
		return people_count;
	}

	public void setPeople_count(String people_count) {
		this.people_count = people_count;
	}

	public String getReceivable() {
		return receivable;
	}

	public void setReceivable(String receivable) {
		this.receivable = receivable;
	}

	public String getAir_ticket_cost() {
		return air_ticket_cost;
	}

	public void setAir_ticket_cost(String air_ticket_cost) {
		this.air_ticket_cost = air_ticket_cost;
	}

	public String getTrain_ticket_cost() {
		return train_ticket_cost;
	}

	public void setTrain_ticket_cost(String train_ticket_cost) {
		this.train_ticket_cost = train_ticket_cost;
	}

	public String getProduct_cost() {
		return product_cost;
	}

	public void setProduct_cost(String product_cost) {
		this.product_cost = product_cost;
	}

	public String getOther_cost() {
		return other_cost;
	}

	public void setOther_cost(String other_cost) {
		this.other_cost = other_cost;
	}

	public String getFy() {
		return fy;
	}

	public void setFy(String fy) {
		this.fy = fy;
	}

	public String getGross_profit() {
		return gross_profit;
	}

	public void setGross_profit(String gross_profit) {
		this.gross_profit = gross_profit;
	}

	public String getPer_profit() {
		return per_profit;
	}

	public void setPer_profit(String per_profit) {
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

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public List<String> getOrder_types() {
		return order_types;
	}

	public void setOrder_types(List<String> order_types) {
		this.order_types = order_types;
	}
}
