package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class JobHoppingLogBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -7070604078403599606L;

	private String client_employee_pk;

	private String hop_date;

	private String pre_client_pk;

	private String now_client_pk;

	private String reason;

	private String pk;

	private String create_user;

	private String update_user;

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public String getHop_date() {
		return hop_date;
	}

	public String getPre_client_pk() {
		return pre_client_pk;
	}

	public String getNow_client_pk() {
		return now_client_pk;
	}

	public String getReason() {
		return reason;
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

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public void setHop_date(String hop_date) {
		this.hop_date = hop_date;
	}

	public void setPre_client_pk(String pre_client_pk) {
		this.pre_client_pk = pre_client_pk;
	}

	public void setNow_client_pk(String now_client_pk) {
		this.now_client_pk = now_client_pk;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

}
