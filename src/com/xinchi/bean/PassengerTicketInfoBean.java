package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class PassengerTicketInfoBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -6767889459725737654L;

	private String ticket_source;

	private java.math.BigDecimal ticket_cost;

	private String PNR;

	private Integer ticket_index;

	private String ticket_date;

	private String ticket_number;

	private String from_to_time;

	private String from_to_city;

	private String from_airport;

	private String to_airport;

	private String terminal;

	private String update_user;

	private String create_user;

	private String pk;

	private String passenger_pk;

	public String getTicket_source() {
		return ticket_source;
	}

	public void setTicket_source(String ticket_source) {
		this.ticket_source = ticket_source;
	}

	public java.math.BigDecimal getTicket_cost() {
		return ticket_cost;
	}

	public void setTicket_cost(java.math.BigDecimal ticket_cost) {
		this.ticket_cost = ticket_cost;
	}

	public String getPNR() {
		return PNR;
	}

	public void setPNR(String pNR) {
		PNR = pNR;
	}

	public Integer getTicket_index() {
		return ticket_index;
	}

	public void setTicket_index(Integer ticket_index) {
		this.ticket_index = ticket_index;
	}

	public String getTicket_date() {
		return ticket_date;
	}

	public void setTicket_date(String ticket_date) {
		this.ticket_date = ticket_date;
	}

	public String getTicket_number() {
		return ticket_number;
	}

	public void setTicket_number(String ticket_number) {
		this.ticket_number = ticket_number;
	}

	public String getFrom_to_time() {
		return from_to_time;
	}

	public void setFrom_to_time(String from_to_time) {
		this.from_to_time = from_to_time;
	}

	public String getFrom_to_city() {
		return from_to_city;
	}

	public void setFrom_to_city(String from_to_city) {
		this.from_to_city = from_to_city;
	}

	public String getFrom_airport() {
		return from_airport;
	}

	public void setFrom_airport(String from_airport) {
		this.from_airport = from_airport;
	}

	public String getTo_airport() {
		return to_airport;
	}

	public void setTo_airport(String to_airport) {
		this.to_airport = to_airport;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
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

	public String getPassenger_pk() {
		return passenger_pk;
	}

	public void setPassenger_pk(String passenger_pk) {
		this.passenger_pk = passenger_pk;
	}

}
