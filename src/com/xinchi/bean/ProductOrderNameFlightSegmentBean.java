package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ProductOrderNameFlightSegmentBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -5072185338849962202L;

	private Integer ticket_index;

	private String ticket_date;

	private String ticket_number;

	private String from_to_time;

	private String add_day_flg;

	private String from_to_city;

	private String from_to_airport;

	private String terminal;

	private String air_comment;

	private String need_pk;

	public Integer getTicket_index() {
		return ticket_index;
	}

	public String getTicket_date() {
		return ticket_date;
	}

	public String getTicket_number() {
		return ticket_number;
	}

	public String getFrom_to_time() {
		return from_to_time;
	}

	public String getAdd_day_flg() {
		return add_day_flg;
	}

	public String getFrom_to_city() {
		return from_to_city;
	}

	public String getFrom_to_airport() {
		return from_to_airport;
	}

	public String getTerminal() {
		return terminal;
	}

	public String getAir_comment() {
		return air_comment;
	}

	public String getNeed_pk() {
		return need_pk;
	}

	public void setTicket_index(Integer ticket_index) {
		this.ticket_index = ticket_index;
	}

	public void setTicket_date(String ticket_date) {
		this.ticket_date = ticket_date;
	}

	public void setTicket_number(String ticket_number) {
		this.ticket_number = ticket_number;
	}

	public void setFrom_to_time(String from_to_time) {
		this.from_to_time = from_to_time;
	}

	public void setAdd_day_flg(String add_day_flg) {
		this.add_day_flg = add_day_flg;
	}

	public void setFrom_to_city(String from_to_city) {
		this.from_to_city = from_to_city;
	}

	public void setFrom_to_airport(String from_to_airport) {
		this.from_to_airport = from_to_airport;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public void setAir_comment(String air_comment) {
		this.air_comment = air_comment;
	}

	public void setNeed_pk(String need_pk) {
		this.need_pk = need_pk;
	}

}
