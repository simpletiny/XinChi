package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ConnectDto  extends SupperBO  implements Serializable{
	private static final long serialVersionUID = 8193582185844547579L;
	private String connect_date;
	private String client_employee_pk;
	private String extra_info;
	private String type;
	private String create_user;

	public String getConnect_date() {
		return connect_date;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public String getExtra_info() {
		return extra_info;
	}

	public String getType() {
		return type;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setConnect_date(String connect_date) {
		this.connect_date = connect_date;
	}

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public void setExtra_info(String extra_info) {
		this.extra_info = extra_info;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
}
