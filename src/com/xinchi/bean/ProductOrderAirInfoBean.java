package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ProductOrderAirInfoBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 9106100156079552056L;

	private String pk;

	private Integer flight_index;

	private String flight_leg;

	private Integer start_day;

	private String start_city;

	private Integer end_day;

	private String end_city;

	private String flight_number;

	private String base_pk;

	private String create_user;

	private String update_user;

	private String product_order_number;

	public String getPk() {
		return pk;
	}

	public Integer getFlight_index() {
		return flight_index;
	}

	public String getFlight_leg() {
		return flight_leg;
	}

	public Integer getStart_day() {
		return start_day;
	}

	public String getStart_city() {
		return start_city;
	}

	public Integer getEnd_day() {
		return end_day;
	}

	public String getEnd_city() {
		return end_city;
	}

	public String getFlight_number() {
		return flight_number;
	}

	public String getBase_pk() {
		return base_pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public void setFlight_index(Integer flight_index) {
		this.flight_index = flight_index;
	}

	public void setFlight_leg(String flight_leg) {
		this.flight_leg = flight_leg;
	}

	public void setStart_day(Integer start_day) {
		this.start_day = start_day;
	}

	public void setStart_city(String start_city) {
		this.start_city = start_city;
	}

	public void setEnd_day(Integer end_day) {
		this.end_day = end_day;
	}

	public void setEnd_city(String end_city) {
		this.end_city = end_city;
	}

	public void setFlight_number(String flight_number) {
		this.flight_number = flight_number;
	}

	public void setBase_pk(String base_pk) {
		this.base_pk = base_pk;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getProduct_order_number() {
		return product_order_number;
	}

	public void setProduct_order_number(String product_order_number) {
		this.product_order_number = product_order_number;
	}

}
