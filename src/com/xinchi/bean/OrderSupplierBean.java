package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class OrderSupplierBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 3724419480074644051L;

	private String order_pk;

	private String supplier_employee_pk;

	private Integer supplier_index;

	private String supplier_product_name;

	private java.math.BigDecimal supplier_cost;

	private Integer land_day;

	private String pick_type;

	private String picker;

	private String picker_cellphone;

	private Integer off_day;

	private String send_type;

	private String pk;

	private String create_user;

	private String update_user;

	private java.math.BigDecimal adult_cost;

	private java.math.BigDecimal child_cost;

	private Integer days;

	private String tourist_info;

	private String confirm_file_templet;

	public String getOrder_pk() {
		return order_pk;
	}

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public Integer getSupplier_index() {
		return supplier_index;
	}

	public String getSupplier_product_name() {
		return supplier_product_name;
	}

	public java.math.BigDecimal getSupplier_cost() {
		return supplier_cost;
	}

	public Integer getLand_day() {
		return land_day;
	}

	public String getPick_type() {
		return pick_type;
	}

	public String getPicker() {
		return picker;
	}

	public String getPicker_cellphone() {
		return picker_cellphone;
	}

	public Integer getOff_day() {
		return off_day;
	}

	public String getSend_type() {
		return send_type;
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

	public java.math.BigDecimal getAdult_cost() {
		return adult_cost;
	}

	public java.math.BigDecimal getChild_cost() {
		return child_cost;
	}

	public Integer getDays() {
		return days;
	}

	public String getTourist_info() {
		return tourist_info;
	}

	public String getConfirm_file_templet() {
		return confirm_file_templet;
	}

	public void setOrder_pk(String order_pk) {
		this.order_pk = order_pk;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}

	public void setSupplier_index(Integer supplier_index) {
		this.supplier_index = supplier_index;
	}

	public void setSupplier_product_name(String supplier_product_name) {
		this.supplier_product_name = supplier_product_name;
	}

	public void setSupplier_cost(java.math.BigDecimal supplier_cost) {
		this.supplier_cost = supplier_cost;
	}

	public void setLand_day(Integer land_day) {
		this.land_day = land_day;
	}

	public void setPick_type(String pick_type) {
		this.pick_type = pick_type;
	}

	public void setPicker(String picker) {
		this.picker = picker;
	}

	public void setPicker_cellphone(String picker_cellphone) {
		this.picker_cellphone = picker_cellphone;
	}

	public void setOff_day(Integer off_day) {
		this.off_day = off_day;
	}

	public void setSend_type(String send_type) {
		this.send_type = send_type;
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

	public void setAdult_cost(java.math.BigDecimal adult_cost) {
		this.adult_cost = adult_cost;
	}

	public void setChild_cost(java.math.BigDecimal child_cost) {
		this.child_cost = child_cost;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public void setTourist_info(String tourist_info) {
		this.tourist_info = tourist_info;
	}

	public void setConfirm_file_templet(String confirm_file_templet) {
		this.confirm_file_templet = confirm_file_templet;
	}

}
