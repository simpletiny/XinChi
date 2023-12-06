package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ProductAccountingFirmBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 9091653226272507978L;

	private String belong_month;

	private String product_manager_number;

	private String confirm_user;

	private String confirm_time;

	public String getBelong_month() {
		return belong_month;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public String getConfirm_user() {
		return confirm_user;
	}

	public String getConfirm_time() {
		return confirm_time;
	}

	public void setBelong_month(String belong_month) {
		this.belong_month = belong_month;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public void setConfirm_user(String confirm_user) {
		this.confirm_user = confirm_user;
	}

	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}

}
