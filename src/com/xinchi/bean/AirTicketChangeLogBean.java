package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class AirTicketChangeLogBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -55017968811432177L;

	private String change_reason;

	private java.math.BigDecimal change_cost;

	private String comment;

	private String pk;

	private String create_user;

	private String update_user;

	private String first_date;
	private String from_to_city;
	private String captain;

	public String getChange_reason() {
		return change_reason;
	}

	public java.math.BigDecimal getChange_cost() {
		return change_cost;
	}

	public String getComment() {
		return comment;
	}

	public String getPk() {
		return pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setChange_reason(String change_reason) {
		this.change_reason = change_reason;
	}

	public void setChange_cost(java.math.BigDecimal change_cost) {
		this.change_cost = change_cost;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getFirst_date() {
		return first_date;
	}

	public String getFrom_to_city() {
		return from_to_city;
	}

	public String getCaptain() {
		return captain;
	}

	public void setFirst_date(String first_date) {
		this.first_date = first_date;
	}

	public void setFrom_to_city(String from_to_city) {
		this.from_to_city = from_to_city;
	}

	public void setCaptain(String captain) {
		this.captain = captain;
	}

}
