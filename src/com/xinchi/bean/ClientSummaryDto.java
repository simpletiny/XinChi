package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ClientSummaryDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = 3367229860285338357L;

	private String level;
	private Integer client_count;
	private Integer month_order_count;
	private Integer month_visit_count;
	private Integer week_order_count;
	private Integer week_visit_count;
	private Integer month_accurate_sale_count;
	private Integer week_accurate_sale_count;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getClient_count() {
		return client_count;
	}

	public void setClient_count(Integer client_count) {
		this.client_count = client_count;
	}

	public Integer getMonth_order_count() {
		return month_order_count;
	}

	public void setMonth_order_count(Integer month_order_count) {
		this.month_order_count = month_order_count;
	}

	public Integer getMonth_visit_count() {
		return month_visit_count;
	}

	public void setMonth_visit_count(Integer month_visit_count) {
		this.month_visit_count = month_visit_count;
	}

	public Integer getWeek_order_count() {
		return week_order_count;
	}

	public void setWeek_order_count(Integer week_order_count) {
		this.week_order_count = week_order_count;
	}

	public Integer getWeek_visit_count() {
		return week_visit_count;
	}

	public void setWeek_visit_count(Integer week_visit_count) {
		this.week_visit_count = week_visit_count;
	}

	public Integer getMonth_accurate_sale_count() {
		return month_accurate_sale_count;
	}

	public void setMonth_accurate_sale_count(Integer month_accurate_sale_count) {
		this.month_accurate_sale_count = month_accurate_sale_count;
	}

	public Integer getWeek_accurate_sale_count() {
		return week_accurate_sale_count;
	}

	public void setWeek_accurate_sale_count(Integer week_accurate_sale_count) {
		this.week_accurate_sale_count = week_accurate_sale_count;
	}

}
