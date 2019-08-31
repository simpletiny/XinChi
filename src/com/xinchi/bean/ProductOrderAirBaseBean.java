package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class ProductOrderAirBaseBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -4101063634519817218L;

	private String pk;

	private String create_user;

	private String update_user;

	private String name;

	private String number;

	private java.math.BigDecimal adult_price;

	private java.math.BigDecimal child_price;

	private String comment;

	private String team_number;

	private List<ProductOrderAirInfoBean> infos;

	public String getPk() {
		return pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

	public java.math.BigDecimal getAdult_price() {
		return adult_price;
	}

	public java.math.BigDecimal getChild_price() {
		return child_price;
	}

	public String getComment() {
		return comment;
	}

	public String getTeam_number() {
		return team_number;
	}

	public List<ProductOrderAirInfoBean> getInfos() {
		return infos;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setAdult_price(java.math.BigDecimal adult_price) {
		this.adult_price = adult_price;
	}

	public void setChild_price(java.math.BigDecimal child_price) {
		this.child_price = child_price;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setInfos(List<ProductOrderAirInfoBean> infos) {
		this.infos = infos;
	}

}
