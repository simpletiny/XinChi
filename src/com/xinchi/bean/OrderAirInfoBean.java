package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class OrderAirInfoBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -8740427219248082678L;
	private String team_number;
	private String air_leg;
	private String from_to_city;
	private int info_index;
	private int air_index;
	private int day_index;
	private String from_to_place;
	private String ticket_client_number;
	private String departure_date;

	private String product_order_number;
	private String need_pk;
	// dto
	private String air_date;

	public String getTeam_number() {
		return team_number;
	}

	public String getAir_leg() {
		return air_leg;
	}

	public String getFrom_to_city() {
		return from_to_city;
	}

	public int getInfo_index() {
		return info_index;
	}

	public int getAir_index() {
		return air_index;
	}

	public int getDay_index() {
		return day_index;
	}

	public String getFrom_to_place() {
		return from_to_place;
	}

	public String getTicket_client_number() {
		return ticket_client_number;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public String getNeed_pk() {
		return need_pk;
	}

	public String getAir_date() {
		return air_date;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setAir_leg(String air_leg) {
		this.air_leg = air_leg;
	}

	public void setFrom_to_city(String from_to_city) {
		this.from_to_city = from_to_city;
	}

	public void setInfo_index(int info_index) {
		this.info_index = info_index;
	}

	public void setAir_index(int air_index) {
		this.air_index = air_index;
	}

	public void setDay_index(int day_index) {
		this.day_index = day_index;
	}

	public void setFrom_to_place(String from_to_place) {
		this.from_to_place = from_to_place;
	}

	public void setTicket_client_number(String ticket_client_number) {
		this.ticket_client_number = ticket_client_number;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public void setNeed_pk(String need_pk) {
		this.need_pk = need_pk;
	}

	public void setAir_date(String air_date) {
		this.air_date = air_date;
	}

	public String getProduct_order_number() {
		return product_order_number;
	}

	public void setProduct_order_number(String product_order_number) {
		this.product_order_number = product_order_number;
	}

}
