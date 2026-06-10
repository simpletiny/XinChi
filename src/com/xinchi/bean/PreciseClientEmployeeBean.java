package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class PreciseClientEmployeeBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 6174751205631892966L;

	private String name;
	
	private String is_verified;

	private String type;

	private String gender;

	private String wechat;

	private String wechat1;

	private String cellphone;

	private String cellphone1;

	private String id;

	private String financial_body_pk;

	private String head_photo;

	private String employee_area;

	private String employee_county;

	private String detail_address;

	private String comment;

	private String self_photo;

	private String other_photo;
	private String binding_status;
	private int binding_count;

	//search options
	private List<String> binding_statuses;
	private List<String> verified_statuses;
	
	// DTO
	private String financial_body_name;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getWechat1() {
		return wechat1;
	}

	public void setWechat1(String wechat1) {
		this.wechat1 = wechat1;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getCellphone1() {
		return cellphone1;
	}

	public void setCellphone1(String cellphone1) {
		this.cellphone1 = cellphone1;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFinancial_body_pk() {
		return financial_body_pk;
	}

	public void setFinancial_body_pk(String financial_body_pk) {
		this.financial_body_pk = financial_body_pk;
	}

	public String getHead_photo() {
		return head_photo;
	}

	public void setHead_photo(String head_photo) {
		this.head_photo = head_photo;
	}

	public String getEmployee_area() {
		return employee_area;
	}

	public void setEmployee_area(String employee_area) {
		this.employee_area = employee_area;
	}

	public String getEmployee_county() {
		return employee_county;
	}

	public void setEmployee_county(String employee_county) {
		this.employee_county = employee_county;
	}

	public String getDetail_address() {
		return detail_address;
	}

	public void setDetail_address(String detail_address) {
		this.detail_address = detail_address;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSelf_photo() {
		return self_photo;
	}

	public void setSelf_photo(String self_photo) {
		this.self_photo = self_photo;
	}

	public String getOther_photo() {
		return other_photo;
	}

	public void setOther_photo(String other_photo) {
		this.other_photo = other_photo;
	}

	public String getFinancial_body_name() {
		return financial_body_name;
	}

	public void setFinancial_body_name(String financial_body_name) {
		this.financial_body_name = financial_body_name;
	}

	public String getBinding_status() {
		return binding_status;
	}

	public void setBinding_status(String binding_status) {
		this.binding_status = binding_status;
	}

	public int getBinding_count() {
		return binding_count;
	}

	public void setBinding_count(int binding_count) {
		this.binding_count = binding_count;
	}

	public List<String> getBinding_statuses() {
		return binding_statuses;
	}

	public void setBinding_statuses(List<String> binding_statuses) {
		this.binding_statuses = binding_statuses;
	}

	public String getIs_verified() {
		return is_verified;
	}

	public void setIs_verified(String is_verified) {
		this.is_verified = is_verified;
	}

	public List<String> getVerified_statuses() {
		return verified_statuses;
	}

	public void setVerified_statuses(List<String> verified_statuses) {
		this.verified_statuses = verified_statuses;
	}

}
