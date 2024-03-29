package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class DropOffBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 5058387930524352686L;

	private String product_order_number;
	private String team_number;
	private String name_list;
	private String first_ticket_date;

	private String client_number;

	private String ticket_number;

	private String from_to_time;

	private String from_to_city;

	private String first_start_city;
	private String first_end_city;
	private String from_airport;
	private String to_airport;
	private String client_name;
	private String phones;

	// search options
	private String from_city;

	private String to_city;

	public String getFirst_ticket_date() {
		return first_ticket_date;
	}

	public String getClient_number() {
		return client_number;
	}

	public String getTicket_number() {
		return ticket_number;
	}

	public String getFrom_to_time() {
		return from_to_time;
	}

	public String getFrom_to_city() {
		return from_to_city;
	}

	public void setFirst_ticket_date(String first_ticket_date) {
		this.first_ticket_date = first_ticket_date;
	}

	public void setClient_number(String client_number) {
		this.client_number = client_number;
	}

	public void setTicket_number(String ticket_number) {
		this.ticket_number = ticket_number;
	}

	public void setFrom_to_time(String from_to_time) {
		this.from_to_time = from_to_time;
	}

	public void setFrom_to_city(String from_to_city) {
		this.from_to_city = from_to_city;
	}

	public String getFrom_city() {
		return from_city;
	}

	public void setFrom_city(String from_city) {
		this.from_city = from_city;
	}

	public String getFirst_start_city() {
		return first_start_city;
	}

	public String getFirst_end_city() {
		return first_end_city;
	}

	public String getFrom_airport() {
		return from_airport;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setFirst_start_city(String first_start_city) {
		this.first_start_city = first_start_city;
	}

	public void setFirst_end_city(String first_end_city) {
		this.first_end_city = first_end_city;
	}

	public void setFrom_airport(String from_airport) {
		this.from_airport = from_airport;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getTo_airport() {
		return to_airport;
	}

	public void setTo_airport(String to_airport) {
		this.to_airport = to_airport;
	}

	public String getProduct_order_number() {
		return product_order_number;
	}

	public String getTeam_number() {
		return team_number;
	}

	public String getName_list() {
		return name_list;
	}

	public void setProduct_order_number(String product_order_number) {
		this.product_order_number = product_order_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setName_list(String name_list) {
		this.name_list = name_list;
	}

	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	public String getTo_city() {
		return to_city;
	}

	public void setTo_city(String to_city) {
		this.to_city = to_city;
	}

}
