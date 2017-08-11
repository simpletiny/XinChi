package com.xinchi.bean;

import java.util.ArrayList;
import java.util.List;

public class TicketAllotDto {

	private String ticket_source;
	private List<AirTicketNameListBean> passengers = new ArrayList<AirTicketNameListBean>();

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
}
