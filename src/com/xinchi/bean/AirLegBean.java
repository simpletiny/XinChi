package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class AirLegBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -6887937135153258809L;

	private String from_city;

	private String from_place;

	private String to_city;

	private String to_place;

	private String create_user;

	private String update_user;

	private String hot_flg;

	private Integer sort_index;

	private String use_flg;

	// option
	private String city;
	private List<String> use_flgs;

	public String getFrom_city() {
		return from_city;
	}

	public String getTo_city() {
		return to_city;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public String getHot_flg() {
		return hot_flg;
	}

	public Integer getSort_index() {
		return sort_index;
	}

	public void setFrom_city(String from_city) {
		this.from_city = from_city;
	}

	public void setTo_city(String to_city) {
		this.to_city = to_city;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public void setHot_flg(String hot_flg) {
		this.hot_flg = hot_flg;
	}

	public void setSort_index(Integer sort_index) {
		this.sort_index = sort_index;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUse_flg() {
		return use_flg;
	}

	public void setUse_flg(String use_flg) {
		this.use_flg = use_flg;
	}

	public List<String> getUse_flgs() {
		return use_flgs;
	}

	public void setUse_flgs(List<String> use_flgs) {
		this.use_flgs = use_flgs;
	}

	public String getFrom_place() {
		return from_place;
	}

	public String getTo_place() {
		return to_place;
	}

	public void setFrom_place(String from_place) {
		this.from_place = from_place;
	}

	public void setTo_place(String to_place) {
		this.to_place = to_place;
	}

}
