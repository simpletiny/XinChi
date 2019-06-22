package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class PassengerAllotDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = -1333952894489998725L;

	private String passenger_pk;

	private String id;

	private String name;

	private String date;

	private String from_city;

	private String to_city;

	private String ticket_order_pk;

	private String is_allot;

	public String getPassenger_pk() {
		return passenger_pk;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return date;
	}

	public String getFrom_city() {
		return from_city;
	}

	public String getTo_city() {
		return to_city;
	}

	public String getTicket_order_pk() {
		return ticket_order_pk;
	}

	public String getIs_allot() {
		return is_allot;
	}

	public void setPassenger_pk(String passenger_pk) {
		this.passenger_pk = passenger_pk;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setFrom_city(String from_city) {
		this.from_city = from_city;
	}

	public void setTo_city(String to_city) {
		this.to_city = to_city;
	}

	public void setTicket_order_pk(String ticket_order_pk) {
		this.ticket_order_pk = ticket_order_pk;
	}

	public void setIs_allot(String is_allot) {
		this.is_allot = is_allot;
	}

}
