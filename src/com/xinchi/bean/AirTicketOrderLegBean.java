package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class AirTicketOrderLegBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -2532738482507677748L;

	private String pk;

	private String ticket_order_pk;

	private String date;

	private Integer sort_index;

	private String from_city;

	private String to_city;

	private String create_user;

	private String update_user;

	public String getPk() {
		return pk;
	}

	public String getTicket_order_pk() {
		return ticket_order_pk;
	}

	public String getDate() {
		return date;
	}

	public Integer getSort_index() {
		return sort_index;
	}

	public String getFrom_city() {
		return from_city;
	}

	public String getTo_city() {
		return to_city;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public void setTicket_order_pk(String ticket_order_pk) {
		this.ticket_order_pk = ticket_order_pk;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setSort_index(Integer sort_index) {
		this.sort_index = sort_index;
	}

	public void setFrom_city(String from_city) {
		this.from_city = from_city;
	}

	public void setTo_city(String to_city) {
		this.to_city = to_city;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

}
