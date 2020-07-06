package com.xinchi.bean;

import java.io.Serializable;
import java.util.Comparator;

import com.xinchi.common.SupperBO;

public class PassengerAllotDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = -1333952894489998725L;

	private String passenger_pk;

	private String id;

	private String name;

	private String date;

	private String from_city;

	private String to_city;

	private String start_time;
	private String end_time;
	private String start_place;
	private String end_place;
	private String ticket_number;
	private String ticket_order_pk;

	private String is_allot;

	public static class Comparators {
		public static Comparator<PassengerAllotDto> DATE = new Comparator<PassengerAllotDto>() {
			@Override
			public int compare(PassengerAllotDto o1, PassengerAllotDto o2) {
				return o1.date.compareTo(o2.date);
			}
		};
	}

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

	public String getStart_time() {
		return start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public String getStart_place() {
		return start_place;
	}

	public String getEnd_place() {
		return end_place;
	}

	public String getTicket_number() {
		return ticket_number;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public void setStart_place(String start_place) {
		this.start_place = start_place;
	}

	public void setEnd_place(String end_place) {
		this.end_place = end_place;
	}

	public void setTicket_number(String ticket_number) {
		this.ticket_number = ticket_number;
	}

}
