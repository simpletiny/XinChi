package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class MobileTouchBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -1386626459628331869L;

	private String client_employee_pk;

	private String date;

	private String touch_target;

	private String summary;

	private String create_user;

	private String update_user;

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public String getDate() {
		return date;
	}

	public String getTouch_target() {
		return touch_target;
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

	public void setTouch_target(String touch_target) {
		this.touch_target = touch_target;
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
