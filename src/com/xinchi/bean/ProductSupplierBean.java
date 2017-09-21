package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class ProductSupplierBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -2782918360440152184L;

	private String product_pk;

	private String supplier_employee_pk;

	private Integer supplier_index;

	private String supplier_product_name;
	private BigDecimal supplier_cost = BigDecimal.ZERO;

	private Integer land_day;

	private String pick_type;

	private String picker;

	private String picker_cellphone;

	private Integer off_day;
	private String send_type;

	private String pk;

	private String supplier_employee_name;

	private String create_user;

	private String update_user;

	private BigDecimal sum_cost;

	public String getProduct_pk() {
		return product_pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}

	public Integer getSupplier_index() {
		return supplier_index;
	}

	public void setSupplier_index(Integer supplier_index) {
		this.supplier_index = supplier_index;
	}

	public Integer getLand_day() {
		return land_day;
	}

	public void setLand_day(Integer land_day) {
		this.land_day = land_day;
	}

	public String getPick_type() {
		return pick_type;
	}

	public void setPick_type(String pick_type) {
		this.pick_type = pick_type;
	}

	public String getPicker() {
		return picker;
	}

	public void setPicker(String picker) {
		this.picker = picker;
	}

	public String getPicker_cellphone() {
		return picker_cellphone;
	}

	public void setPicker_cellphone(String picker_cellphone) {
		this.picker_cellphone = picker_cellphone;
	}

	public Integer getOff_day() {
		return off_day;
	}

	public void setOff_day(Integer off_day) {
		this.off_day = off_day;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getSupplier_employee_name() {
		return supplier_employee_name;
	}

	public void setSupplier_employee_name(String supplier_employee_name) {
		this.supplier_employee_name = supplier_employee_name;
	}

	public BigDecimal getSupplier_cost() {
		return supplier_cost;
	}

	public void setSupplier_cost(BigDecimal supplier_cost) {
		this.supplier_cost = supplier_cost;
	}

	public String getSend_type() {
		return send_type;
	}

	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}

	public String getSupplier_product_name() {
		return supplier_product_name;
	}

	public void setSupplier_product_name(String supplier_product_name) {
		this.supplier_product_name = supplier_product_name;
	}

	public BigDecimal getSum_cost() {
		return sum_cost;
	}

	public void setSum_cost(BigDecimal sum_cost) {
		this.sum_cost = sum_cost;
	}

}
