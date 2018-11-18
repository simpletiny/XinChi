package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class BackPointDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = 2473089334205877663L;
	
	private String team_number;
	private String user_number;
	private int product_point;
	private String user_pk;
	private String confirm_date;

	public String getTeam_number() {
		return team_number;
	}

	public String getUser_number() {
		return user_number;
	}

	public int getProduct_point() {
		return product_point;
	}

	public String getUser_pk() {
		return user_pk;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public void setProduct_point(int product_point) {
		this.product_point = product_point;
	}

	public void setUser_pk(String user_pk) {
		this.user_pk = user_pk;
	}

	public String getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

}
