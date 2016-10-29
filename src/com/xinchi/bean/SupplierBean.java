package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class SupplierBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String supplier_name;

	private String supplier_short_name;

	private String supplier_area;

	private String supplier_type;

	private String telephone;

	private String fax;

	private String address;

	private String body_name;

	private String body_sex;

	private String body_id;

	private String body_birth_year;

	private String body_wechat;

	private String body_cellphone;

	private String create_user;

	private String update_user;

	private String approve_user;

	private String update_reason;

	private String comment;

	private String pk;

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getSupplier_short_name() {
		return supplier_short_name;
	}

	public void setSupplier_short_name(String supplier_short_name) {
		this.supplier_short_name = supplier_short_name;
	}

	public String getSupplier_area() {
		return supplier_area;
	}

	public void setSupplier_area(String supplier_area) {
		this.supplier_area = supplier_area;
	}

	public String getSupplier_type() {
		return supplier_type;
	}

	public void setSupplier_type(String supplier_type) {
		this.supplier_type = supplier_type;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBody_name() {
		return body_name;
	}

	public void setBody_name(String body_name) {
		this.body_name = body_name;
	}

	public String getBody_sex() {
		return body_sex;
	}

	public void setBody_sex(String body_sex) {
		this.body_sex = body_sex;
	}

	public String getBody_id() {
		return body_id;
	}

	public void setBody_id(String body_id) {
		this.body_id = body_id;
	}

	public String getBody_birth_year() {
		return body_birth_year;
	}

	public void setBody_birth_year(String body_birth_year) {
		this.body_birth_year = body_birth_year;
	}

	public String getBody_wechat() {
		return body_wechat;
	}

	public void setBody_wechat(String body_wechat) {
		this.body_wechat = body_wechat;
	}

	public String getBody_cellphone() {
		return body_cellphone;
	}

	public void setBody_cellphone(String body_cellphone) {
		this.body_cellphone = body_cellphone;
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

	public String getApprove_user() {
		return approve_user;
	}

	public void setApprove_user(String approve_user) {
		this.approve_user = approve_user;
	}

	public String getUpdate_reason() {
		return update_reason;
	}

	public void setUpdate_reason(String update_reason) {
		this.update_reason = update_reason;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}


}
