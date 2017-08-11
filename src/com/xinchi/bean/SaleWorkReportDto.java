package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class SaleWorkReportDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = 5621575727111021030L;

	private String sale_name;
	private String sale_number;
	private String date;
	private int order_count;
	private String client_employee_pk;
	private String client_employee_name;
	private String client_pk;
	private String client_name;
	private String visit_target;
	private String visit_sub_target;
	private String visit_summary;
	private String accurate_sale_product;
	private String accurate_sale_summary;

	private List<ClientVisitBean> visits;
	private List<AccurateSaleBean> accurates;

	private String month;

	public String getSale_name() {
		return sale_name;
	}

	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}

	public String getSale_number() {
		return sale_number;
	}

	public void setSale_number(String sale_number) {
		this.sale_number = sale_number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getOrder_count() {
		return order_count;
	}

	public void setOrder_count(int order_count) {
		this.order_count = order_count;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public String getClient_employee_name() {
		return client_employee_name;
	}

	public void setClient_employee_name(String client_employee_name) {
		this.client_employee_name = client_employee_name;
	}

	public String getClient_pk() {
		return client_pk;
	}

	public void setClient_pk(String client_pk) {
		this.client_pk = client_pk;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getVisit_target() {
		return visit_target;
	}

	public void setVisit_target(String visit_target) {
		this.visit_target = visit_target;
	}

	public String getVisit_sub_target() {
		return visit_sub_target;
	}

	public void setVisit_sub_target(String visit_sub_target) {
		this.visit_sub_target = visit_sub_target;
	}

	public String getVisit_summary() {
		return visit_summary;
	}

	public void setVisit_summary(String visit_summary) {
		this.visit_summary = visit_summary;
	}

	public String getAccurate_sale_product() {
		return accurate_sale_product;
	}

	public void setAccurate_sale_product(String accurate_sale_product) {
		this.accurate_sale_product = accurate_sale_product;
	}

	public String getAccurate_sale_summary() {
		return accurate_sale_summary;
	}

	public void setAccurate_sale_summary(String accurate_sale_summary) {
		this.accurate_sale_summary = accurate_sale_summary;
	}

	public List<ClientVisitBean> getVisits() {
		return visits;
	}

	public void setVisits(List<ClientVisitBean> visits) {
		this.visits = visits;
	}

	public List<AccurateSaleBean> getAccurates() {
		return accurates;
	}

	public void setAccurates(List<AccurateSaleBean> accurates) {
		this.accurates = accurates;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}
