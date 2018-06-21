package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class DayPointDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = -4504912891544860281L;

	private BigDecimal point;
	private String sale_number;
	private String sale_name;
	private String day_date;

	public BigDecimal getPoint() {
		return point;
	}

	public String getSale_number() {
		return sale_number;
	}

	public String getSale_name() {
		return sale_name;
	}

	public String getDay_date() {
		return day_date;
	}

	public void setPoint(BigDecimal point) {
		this.point = point;
	}

	public void setSale_number(String sale_number) {
		this.sale_number = sale_number;
	}

	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}

	public void setDay_date(String day_date) {
		this.day_date = day_date;
	}
}
