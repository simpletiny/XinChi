package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ProductSupplierInfoBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 7986798999677247453L;

	private String product_supplier_pk;

	private Integer info_index;

	private String pick_type;

	private String pick_leg;

	private String pick_other;

	private Integer pick_day;

	private String pick_traffic;

	private String pick_time;

	private String pick_city;

	private String pick_place;

	private String send_type;

	private String send_leg;

	private String send_other;

	private Integer send_day;

	private String send_traffic;

	private String send_time;

	private String send_city;

	private String send_place;

	private String create_user;

	private String update_user;

	public String getProduct_supplier_pk() {
		return product_supplier_pk;
	}

	public Integer getInfo_index() {
		return info_index;
	}

	public String getPick_type() {
		return pick_type;
	}

	public String getPick_leg() {
		return pick_leg;
	}

	public String getPick_other() {
		return pick_other;
	}

	public Integer getPick_day() {
		return pick_day;
	}

	public String getPick_traffic() {
		return pick_traffic;
	}

	public String getPick_time() {
		return pick_time;
	}

	public String getPick_city() {
		return pick_city;
	}

	public String getPick_place() {
		return pick_place;
	}

	public String getSend_type() {
		return send_type;
	}

	public String getSend_leg() {
		return send_leg;
	}

	public String getSend_other() {
		return send_other;
	}

	public Integer getSend_day() {
		return send_day;
	}

	public String getSend_traffic() {
		return send_traffic;
	}

	public String getSend_time() {
		return send_time;
	}

	public String getSend_city() {
		return send_city;
	}

	public String getSend_place() {
		return send_place;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setProduct_supplier_pk(String product_supplier_pk) {
		this.product_supplier_pk = product_supplier_pk;
	}

	public void setInfo_index(Integer info_index) {
		this.info_index = info_index;
	}

	public void setPick_type(String pick_type) {
		this.pick_type = pick_type;
	}

	public void setPick_leg(String pick_leg) {
		this.pick_leg = pick_leg;
	}

	public void setPick_other(String pick_other) {
		this.pick_other = pick_other;
	}

	public void setPick_day(Integer pick_day) {
		this.pick_day = pick_day;
	}

	public void setPick_traffic(String pick_traffic) {
		this.pick_traffic = pick_traffic;
	}

	public void setPick_time(String pick_time) {
		this.pick_time = pick_time;
	}

	public void setPick_city(String pick_city) {
		this.pick_city = pick_city;
	}

	public void setPick_place(String pick_place) {
		this.pick_place = pick_place;
	}

	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}

	public void setSend_leg(String send_leg) {
		this.send_leg = send_leg;
	}

	public void setSend_other(String send_other) {
		this.send_other = send_other;
	}

	public void setSend_day(Integer send_day) {
		this.send_day = send_day;
	}

	public void setSend_traffic(String send_traffic) {
		this.send_traffic = send_traffic;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public void setSend_city(String send_city) {
		this.send_city = send_city;
	}

	public void setSend_place(String send_place) {
		this.send_place = send_place;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

}
