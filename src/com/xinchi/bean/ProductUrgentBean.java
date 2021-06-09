package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ProductUrgentBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -3359673800280400176L;

	private String product_pk;

	private String user_number;

	private String record_date;

	private String pk;

	private String create_user;

	private String update_user;

	public String getProduct_pk() {
		return product_pk;
	}

	public String getUser_number() {
		return user_number;
	}

	public String getRecord_date() {
		return record_date;
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

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public void setRecord_date(String record_date) {
		this.record_date = record_date;
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
