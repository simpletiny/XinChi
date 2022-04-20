package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ReceivedMatchBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String detail_pk;

	private String received_pk;

	private String from_where;

	private String pk;

	private String create_user;

	private String update_user;

	public String getDetail_pk() {
		return detail_pk;
	}

	public void setDetail_pk(String detail_pk) {
		this.detail_pk = detail_pk;
	}

	public String getReceived_pk() {
		return received_pk;
	}

	public void setReceived_pk(String received_pk) {
		this.received_pk = received_pk;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getFrom_where() {
		return from_where;
	}

	public void setFrom_where(String from_where) {
		this.from_where = from_where;
	}

}
