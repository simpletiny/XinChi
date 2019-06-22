package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class OrderAirInfoBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -8740427219248082678L;
	private String team_number;
	private String air_leg;
	private String from_to_city;
	private int info_index;
	private int air_index;
	private int day_index;
	private String from_to_place;
	private String product_manager_number;
	private String standard_flg;
	private String departure_date;
	private String operate_flg;
	private String confirm_date;
	private String confirm_flg;

	// dto
	private String air_date;

	public String getTeam_number() {
		return team_number;
	}

	public String getFrom_to_city() {
		return from_to_city;
	}

	public int getInfo_index() {
		return info_index;
	}

	public int getAir_index() {
		return air_index;
	}

	public int getDay_index() {
		return day_index;
	}

	public String getFrom_to_place() {
		return from_to_place;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public String getStandard_flg() {
		return standard_flg;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public String getOperate_flg() {
		return operate_flg;
	}

	public String getConfirm_date() {
		return confirm_date;
	}

	public String getConfirm_flg() {
		return confirm_flg;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setFrom_to_city(String from_to_city) {
		this.from_to_city = from_to_city;
	}

	public void setInfo_index(int info_index) {
		this.info_index = info_index;
	}

	public void setAir_index(int air_index) {
		this.air_index = air_index;
	}

	public void setDay_index(int day_index) {
		this.day_index = day_index;
	}

	public void setFrom_to_place(String from_to_place) {
		this.from_to_place = from_to_place;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public void setStandard_flg(String standard_flg) {
		this.standard_flg = standard_flg;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public void setOperate_flg(String operate_flg) {
		this.operate_flg = operate_flg;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

	public void setConfirm_flg(String confirm_flg) {
		this.confirm_flg = confirm_flg;
	}

	public String getAir_leg() {
		return air_leg;
	}

	public void setAir_leg(String air_leg) {
		this.air_leg = air_leg;
	}

	public String getAir_date() {
		return air_date;
	}

	public void setAir_date(String air_date) {
		this.air_date = air_date;
	}

}
