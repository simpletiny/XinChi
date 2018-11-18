package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class PointDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = 6447707962385511978L;

	private int point;
	private String user_number;
	private String user_pk;
	private String date;

	public int getPoint() {
		return point;
	}

	public String getUser_number() {
		return user_number;
	}

	public String getDate() {
		return date;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUser_pk() {
		return user_pk;
	}

	public void setUser_pk(String user_pk) {
		this.user_pk = user_pk;
	}
}
