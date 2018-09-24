package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class IncomingCallBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 8373349589813315175L;

	private String client_employee_pk;

	private String date;

	private String type;

	private String summary;

	private String create_user;

	private String update_user;

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public String getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public String getSummary() {
		return summary;
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

	public void setDate(String date) {
		this.date = date;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

}
