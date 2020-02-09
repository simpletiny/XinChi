package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class DataOrderCountDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = -823660482720303975L;

	private int order_cnt;
	private int people_cnt;
	private String confirm_date;
	private int month;
	private int dayofm;
	private int dayofw;
	private String create_user;
	private String create_user_number;

	private String user_name;
	private List<Integer> data_order_cnt;
	private List<Integer> data_people_cnt;

	// options
	private String horizontal;
	private String vertical;
	private String data_type;
	private String option_year;
	private String option_month;

	public String getUser_name() {
		return user_name;
	}

	public String getVertical() {
		return vertical;
	}

	public String getData_type() {
		return data_type;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public int getOrder_cnt() {
		return order_cnt;
	}

	public int getPeople_cnt() {
		return people_cnt;
	}

	public String getConfirm_date() {
		return confirm_date;
	}

	public int getMonth() {
		return month;
	}

	public int getDayofm() {
		return dayofm;
	}

	public int getDayofw() {
		return dayofw;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getCreate_user_number() {
		return create_user_number;
	}

	public void setOrder_cnt(int order_cnt) {
		this.order_cnt = order_cnt;
	}

	public void setPeople_cnt(int people_cnt) {
		this.people_cnt = people_cnt;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setDayofm(int dayofm) {
		this.dayofm = dayofm;
	}

	public void setDayofw(int dayofw) {
		this.dayofw = dayofw;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setCreate_user_number(String create_user_number) {
		this.create_user_number = create_user_number;
	}

	public String getOption_year() {
		return option_year;
	}

	public String getOption_month() {
		return option_month;
	}

	public void setOption_year(String option_year) {
		this.option_year = option_year;
	}

	public void setOption_month(String option_month) {
		this.option_month = option_month;
	}

	public String getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(String horizontal) {
		this.horizontal = horizontal;
	}

	public List<Integer> getData_order_cnt() {
		return data_order_cnt;
	}

	public List<Integer> getData_people_cnt() {
		return data_people_cnt;
	}

	public void setData_order_cnt(List<Integer> data_order_cnt) {
		this.data_order_cnt = data_order_cnt;
	}

	public void setData_people_cnt(List<Integer> data_people_cnt) {
		this.data_people_cnt = data_people_cnt;
	}
}
