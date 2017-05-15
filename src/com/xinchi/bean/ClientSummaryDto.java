package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ClientSummaryDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = 3367229860285338357L;

	private String level;
	private String client_count;
	private String month_order_count;
	private String month_visit_count;
	private String week_visit_count;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMonth_order_count() {
		return month_order_count;
	}

	public void setMonth_order_count(String month_order_count) {
		this.month_order_count = month_order_count;
	}

	public String getMonth_visit_count() {
		return month_visit_count;
	}

	public void setMonth_visit_count(String month_visit_count) {
		this.month_visit_count = month_visit_count;
	}

	public String getWeek_visit_count() {
		return week_visit_count;
	}

	public void setWeek_visit_count(String week_visit_count) {
		this.week_visit_count = week_visit_count;
	}

	public String getClient_count() {
		return client_count;
	}

	public void setClient_count(String client_count) {
		this.client_count = client_count;
	}
}
