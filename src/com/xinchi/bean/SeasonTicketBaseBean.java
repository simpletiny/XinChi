package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class SeasonTicketBaseBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 550869136575685550L;

	private String name;

	private String model;

	private String supplier_employee_pk;

	private String supplier_employee_name;

	private BigDecimal price;
	private BigDecimal special_price;

	private String comment;

	private String pk;

	private String create_user;

	private String update_user;

	private String city;

	private List<SeasonTicketInfoBean> infos;

	public String getName() {
		return name;
	}

	public String getModel() {
		return model;
	}

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getComment() {
		return comment;
	}

	public String getPk() {
		return pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public List<SeasonTicketInfoBean> getInfos() {
		return infos;
	}

	public void setInfos(List<SeasonTicketInfoBean> infos) {
		this.infos = infos;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSupplier_employee_name() {
		return supplier_employee_name;
	}

	public void setSupplier_employee_name(String supplier_employee_name) {
		this.supplier_employee_name = supplier_employee_name;
	}

	public BigDecimal getSpecial_price() {
		return special_price;
	}

	public void setSpecial_price(BigDecimal special_price) {
		this.special_price = special_price;
	}

}
