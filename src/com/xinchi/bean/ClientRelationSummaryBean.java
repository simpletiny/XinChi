package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ClientRelationSummaryBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String client_employee_name;

	private String client_employee_pk;

	private String sales;

	private String sales_name;

	private int year_order_count;

	private int month_order_count;

	private int week_order_count;

	private String last_confirm_date;

	private Integer last_order_period;

	private int visit_count;

	private Integer last_visit_period;

	private int chat_count;

	private Integer last_chat_period;

	private java.math.BigDecimal receivable;

	private Integer last_receivable_period;

	public String getClient_employee_name() {
		return client_employee_name;
	}

	public void setClient_employee_name(String client_employee_name) {
		this.client_employee_name = client_employee_name;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getSales_name() {
		return sales_name;
	}

	public void setSales_name(String sales_name) {
		this.sales_name = sales_name;
	}

	public int getYear_order_count() {
		return year_order_count;
	}

	public void setYear_order_count(int year_order_count) {
		this.year_order_count = year_order_count;
	}

	public int getMonth_order_count() {
		return month_order_count;
	}

	public void setMonth_order_count(int month_order_count) {
		this.month_order_count = month_order_count;
	}

	public int getWeek_order_count() {
		return week_order_count;
	}

	public void setWeek_order_count(int week_order_count) {
		this.week_order_count = week_order_count;
	}

	public String getLast_confirm_date() {
		return last_confirm_date;
	}

	public void setLast_confirm_date(String last_confirm_date) {
		this.last_confirm_date = last_confirm_date;
	}

	public Integer getLast_order_period() {
		return last_order_period;
	}

	public void setLast_order_period(Integer last_order_period) {
		this.last_order_period = last_order_period;
	}

	public int getVisit_count() {
		return visit_count;
	}

	public void setVisit_count(int visit_count) {
		this.visit_count = visit_count;
	}

	public Integer getLast_visit_period() {
		return last_visit_period;
	}

	public void setLast_visit_period(Integer last_visit_period) {
		this.last_visit_period = last_visit_period;
	}

	public int getChat_count() {
		return chat_count;
	}

	public void setChat_count(int chat_count) {
		this.chat_count = chat_count;
	}

	public Integer getLast_chat_period() {
		return last_chat_period;
	}

	public void setLast_chat_period(Integer last_chat_period) {
		this.last_chat_period = last_chat_period;
	}

	public java.math.BigDecimal getReceivable() {
		return receivable;
	}

	public void setReceivable(java.math.BigDecimal receivable) {
		this.receivable = receivable;
	}

	public Integer getLast_receivable_period() {
		return last_receivable_period;
	}

	public void setLast_receivable_period(Integer last_receivable_period) {
		this.last_receivable_period = last_receivable_period;
	}

}
