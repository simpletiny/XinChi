package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ProductAirTicketBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -159950603439286168L;

	private String product_pk;

	private String ticket_charge;

	private Integer ticket_index;

	private Integer start_day;

	private String start_city;

	private Integer end_day;

	private String end_city;

	private String ticket_number;

	private String update_user;

	private String create_user;

	private String pk;

	public String getTicket_charge() {
		return ticket_charge;
	}

	public void setTicket_charge(String ticket_charge) {
		this.ticket_charge = ticket_charge;
	}

	public Integer getTicket_index() {
		return ticket_index;
	}

	public void setTicket_index(Integer ticket_index) {
		this.ticket_index = ticket_index;
	}

	public Integer getStart_day() {
		return start_day;
	}

	public void setStart_day(Integer start_day) {
		this.start_day = start_day;
	}

	public String getStart_city() {
		return start_city;
	}

	public void setStart_city(String start_city) {
		this.start_city = start_city;
	}

	public Integer getEnd_day() {
		return end_day;
	}

	public void setEnd_day(Integer end_day) {
		this.end_day = end_day;
	}

	public String getEnd_city() {
		return end_city;
	}

	public void setEnd_city(String end_city) {
		this.end_city = end_city;
	}

	public String getTicket_number() {
		return ticket_number;
	}

	public void setTicket_number(String ticket_number) {
		this.ticket_number = ticket_number;
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

	public String getProduct_pk() {
		return product_pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

}
