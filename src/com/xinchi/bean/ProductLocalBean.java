package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ProductLocalBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 8204090631609044704L;

	private String product_pk;

	private String service_type;

	private String service_name;

	private String supplier_pk;

	private java.math.BigDecimal cost;

	private java.math.BigDecimal adult_cost;

	private java.math.BigDecimal child_cost;

	private String service_comment;

	private String tourist_info;

	private String create_user;

	private String update_user;

	private String supplier_name;

	public String getProduct_pk() {
		return product_pk;
	}

	public String getService_type() {
		return service_type;
	}

	public String getService_name() {
		return service_name;
	}

	public String getSupplier_pk() {
		return supplier_pk;
	}

	public java.math.BigDecimal getCost() {
		return cost;
	}

	public java.math.BigDecimal getAdult_cost() {
		return adult_cost;
	}

	public java.math.BigDecimal getChild_cost() {
		return child_cost;
	}

	public String getService_comment() {
		return service_comment;
	}

	public String getTourist_info() {
		return tourist_info;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public void setSupplier_pk(String supplier_pk) {
		this.supplier_pk = supplier_pk;
	}

	public void setCost(java.math.BigDecimal cost) {
		this.cost = cost;
	}

	public void setAdult_cost(java.math.BigDecimal adult_cost) {
		this.adult_cost = adult_cost;
	}

	public void setChild_cost(java.math.BigDecimal child_cost) {
		this.child_cost = child_cost;
	}

	public void setService_comment(String service_comment) {
		this.service_comment = service_comment;
	}

	public void setTourist_info(String tourist_info) {
		this.tourist_info = tourist_info;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

}
