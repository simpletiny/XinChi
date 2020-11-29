package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class DropOffBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 5058387930524352686L;

	private String first_ticket_date;

	private String client_number;

	private String name;

	private String id;

	private String ticket_number;

	private String from_to_time;

	private String from_to_city;

	private String first_start_city;
	private String first_end_city;
	private String from_airport;
	private String to_airport;
	private String client_name;

	// search options
	private String from_city;

	public String getFirst_ticket_date() {
		return first_ticket_date;
	}

	public String getClient_number() {
		return client_number;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
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

}
