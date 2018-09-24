package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ClientEmployeeQuitConnectLogBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -5825999766149225418L;

	private String pk;

	private String client_employee_pk;

	private String date;

	private String reason;

	private String comment;

	private String create_user;

	private String update_user;

	public String getPk() {
		return pk;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public String getDate() {
		return date;
	}

	public String getReason() {
		return reason;
	}

	public String getComment() {
		return comment;
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

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

}
