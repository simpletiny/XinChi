package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ClientEmployeeUserBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 7684806350922026197L;

	private String employee_pk;

	private String user_pk;
	private String user_name;

	private String pk;

	private String create_user;

	private String update_user;

	public String getUser_pk() {
		return user_pk;
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

	public void setUser_pk(String user_pk) {
		this.user_pk = user_pk;
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

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getEmployee_pk() {
		return employee_pk;
	}

	public void setEmployee_pk(String employee_pk) {
		this.employee_pk = employee_pk;
	}

}
