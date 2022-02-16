package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class SaleOrderTicketInfoBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -633715824770728484L;

	private String team_number;

	private Integer ticket_index;

	private String ticket_date;

	private String from_city;

	private String to_city;

	private String order_pk;

	private String comment;

	public String getTeam_number() {
		return team_number;
	}

	public Integer getTicket_index() {
		return ticket_index;
	}

	public String getTicket_date() {
		return ticket_date;
	}

	public String getFrom_city() {
		return from_city;
	}

	public String getTo_city() {
		return to_city;
	}

	public String getOrder_pk() {
		return order_pk;
	}

	public String getComment() {
		return comment;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setTicket_index(Integer ticket_index) {
		this.ticket_index = ticket_index;
	}

	public void setTicket_date(String ticket_date) {
		this.ticket_date = ticket_date;
	}

	public void setFrom_city(String from_city) {
		this.from_city = from_city;
	}

	public void setTo_city(String to_city) {
		this.to_city = to_city;
	}

	public void setOrder_pk(String order_pk) {
		this.order_pk = order_pk;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
