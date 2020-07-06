package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class AirTicketNameListBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -2797127299579504835L;

	private String team_number;

	private String client_number;

	private String first_ticket_date;

	private String first_start_city;

	private String first_end_city;

	private String ticket_order_pk;

	private String name;

	private String id;

	private String sale_product_pk;

	private String create_user;

	private String pk;

	private String update_user;

	private String first_from_to;
	private String client_name;

	private String order_number;
	private String comment;

	private String status;

	private String name_confirm_status;

	private BigDecimal ticket_cost;

	private String change_pk;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getClient_number() {
		return client_number;
	}

	public void setClient_number(String client_number) {
		this.client_number = client_number;
	}

	public String getFirst_ticket_date() {
		return first_ticket_date;
	}

	public void setFirst_ticket_date(String first_ticket_date) {
		this.first_ticket_date = first_ticket_date;
	}

	public String getFirst_start_city() {
		return first_start_city;
	}

	public void setFirst_start_city(String first_start_city) {
		this.first_start_city = first_start_city;
	}

	public String getFirst_end_city() {
		return first_end_city;
	}

	public void setFirst_end_city(String first_end_city) {
		this.first_end_city = first_end_city;
	}

	public String getTicket_order_pk() {
		return ticket_order_pk;
	}

	public void setTicket_order_pk(String ticket_order_pk) {
		this.ticket_order_pk = ticket_order_pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSale_product_pk() {
		return sale_product_pk;
	}

	public void setSale_product_pk(String sale_product_pk) {
		this.sale_product_pk = sale_product_pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getFirst_from_to() {
		return first_from_to;
	}

	public void setFirst_from_to(String first_from_to) {
		this.first_from_to = first_from_to;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrder_number() {
		return order_number;
	}

	public String getComment() {
		return comment;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName_confirm_status() {
		return name_confirm_status;
	}

	public void setName_confirm_status(String name_confirm_status) {
		this.name_confirm_status = name_confirm_status;
	}

	public BigDecimal getTicket_cost() {
		return ticket_cost;
	}

	public void setTicket_cost(BigDecimal ticket_cost) {
		this.ticket_cost = ticket_cost;
	}

	public String getChange_pk() {
		return change_pk;
	}

	public void setChange_pk(String change_pk) {
		this.change_pk = change_pk;
	}

}
