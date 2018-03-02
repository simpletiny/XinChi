package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

/**
 * 产品订单
 * 
 * @author simpletiny
 * 
 */
public class ProductOrderDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = -7962499125160752763L;

	private String product_number;
	private String product_pk;
	private String product_name;
	private String status;
	private String team_number;
	private String departure_date;
	private String adult_count;
	private String special_count;
	private String sale_number;
	private String sale_name;
	private String product_manager;
	private String product_manager_number;
	private String treat_comment;
	private String air_info;

	private String date_from;
	private String date_to;

	private String operate_flg;

	private List<String> operate_flgs;

	private String passenger;

	public String getProduct_number() {
		return product_number;
	}

	public void setProduct_number(String product_number) {
		this.product_number = product_number;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public String getAdult_count() {
		return adult_count;
	}

	public void setAdult_count(String adult_count) {
		this.adult_count = adult_count;
	}

	public String getSpecial_count() {
		return special_count;
	}

	public void setSpecial_count(String special_count) {
		this.special_count = special_count;
	}

	public String getSale_number() {
		return sale_number;
	}

	public void setSale_number(String sale_number) {
		this.sale_number = sale_number;
	}

	public String getSale_name() {
		return sale_name;
	}

	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}

	public String getProduct_manager() {
		return product_manager;
	}

	public void setProduct_manager(String product_manager) {
		this.product_manager = product_manager;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public String getDate_from() {
		return date_from;
	}

	public void setDate_from(String date_from) {
		this.date_from = date_from;
	}

	public String getDate_to() {
		return date_to;
	}

	public void setDate_to(String date_to) {
		this.date_to = date_to;
	}

	public String getOperate_flg() {
		return operate_flg;
	}

	public void setOperate_flg(String operate_flg) {
		this.operate_flg = operate_flg;
	}

	public List<String> getOperate_flgs() {
		return operate_flgs;
	}

	public void setOperate_flgs(List<String> operate_flgs) {
		this.operate_flgs = operate_flgs;
	}

	public String getProduct_pk() {
		return product_pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public String getPassenger() {
		return passenger;
	}

	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}

	public String getTreat_comment() {
		return treat_comment;
	}

	public void setTreat_comment(String treat_comment) {
		this.treat_comment = treat_comment;
	}

	public String getAir_info() {
		return air_info;
	}

	public void setAir_info(String air_info) {
		this.air_info = air_info;
	}

}
