package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class ProductOrderBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 4480068348898244330L;

	private String order_number;

	private String single_flg;

	private String status;

	private String product_name;

	private String product_model;

	private String departure_date;

	private Integer adult_count;

	private Integer special_count;

	private String air_comment;

	private String comment;

	private String product_manager_number;
	private String product_manager;

	private String update_user;

	private String create_user;

	private String pk;

	private String passenger_captain;

	private List<String> statuses;

	private String standard_flg;
	private String product_pk;

	private int days;

	public String getOrder_number() {
		return order_number;
	}

	public String getSingle_flg() {
		return single_flg;
	}

	public String getStatus() {
		return status;
	}

	public String getProduct_name() {
		return product_name;
	}

	public String getProduct_model() {
		return product_model;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public Integer getAdult_count() {
		return adult_count;
	}

	public Integer getSpecial_count() {
		return special_count;
	}

	public String getAir_comment() {
		return air_comment;
	}

	public String getComment() {
		return comment;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getPk() {
		return pk;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public void setSingle_flg(String single_flg) {
		this.single_flg = single_flg;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public void setProduct_model(String product_model) {
		this.product_model = product_model;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public void setAdult_count(Integer adult_count) {
		this.adult_count = adult_count;
	}

	public void setSpecial_count(Integer special_count) {
		this.special_count = special_count;
	}

	public void setAir_comment(String air_comment) {
		this.air_comment = air_comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getPassenger_captain() {
		return passenger_captain;
	}

	public void setPassenger_captain(String passenger_captain) {
		this.passenger_captain = passenger_captain;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}

	public String getProduct_manager() {
		return product_manager;
	}

	public void setProduct_manager(String product_manager) {
		this.product_manager = product_manager;
	}

	public String getStandard_flg() {
		return standard_flg;
	}

	public void setStandard_flg(String standard_flg) {
		this.standard_flg = standard_flg;
	}

	public String getProduct_pk() {
		return product_pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

}
