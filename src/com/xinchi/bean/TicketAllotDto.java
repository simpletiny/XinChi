package com.xinchi.bean;

import java.util.ArrayList;
import java.util.List;

public class TicketAllotDto {

	private String ticket_source;
	private String ticket_source_pk;
	private List<AirTicketNameListBean> passengers = new ArrayList<AirTicketNameListBean>();

	private List<PassengerAllotDto> airLegs = new ArrayList<PassengerAllotDto>();

	public String getTicket_source() {
		return ticket_source;
	}

	public void setTicket_source(String ticket_source) {
		this.ticket_source = ticket_source;
	}

	public List<AirTicketNameListBean> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<AirTicketNameListBean> passengers) {
		this.passengers = passengers;
	}

	public String getTicket_source_pk() {
		return ticket_source_pk;
	}

	public void setTicket_source_pk(String ticket_source_pk) {
		this.ticket_source_pk = ticket_source_pk;
	}

	public List<PassengerAllotDto> getAirLegs() {
		return airLegs;
	}

	public void setAirLegs(List<PassengerAllotDto> airLegs) {
		this.airLegs = airLegs;
	}
}
