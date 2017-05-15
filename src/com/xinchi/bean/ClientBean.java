package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ClientBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String client_name;

	private String client_short_name;

	private String client_area;

	private String client_type;

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
	
	private String delete_flg;
	private String agency_name;
	private String agency_pk;
	
	private String sales;
	private String sales_name;
	private String public_flg;
	private String relate_flg;
	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getClient_short_name() {
		return client_short_name;
	}

	public void setClient_short_name(String client_short_name) {
		this.client_short_name = client_short_name;
	}

	public String getClient_area() {
		return client_area;
	}

	public void setClient_area(String client_area) {
		this.client_area = client_area;
	}

	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
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

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getSales_name() {
		return sales_name;
	}

	public void setSales_name(String sales_name) {
		this.sales_name = sales_name;
	}

	public String getPublic_flg() {
		return public_flg;
	}

	public void setPublic_flg(String public_flg) {
		this.public_flg = public_flg;
	}

	public String getDelete_flg() {
		return delete_flg;
	}

	public void setDelete_flg(String delete_flg) {
		this.delete_flg = delete_flg;
	}

	public String getAgency_name() {
		return agency_name;
	}

	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}

	public String getAgency_pk() {
		return agency_pk;
	}

	public void setAgency_pk(String agency_pk) {
		this.agency_pk = agency_pk;
	}

	public String getRelate_flg() {
		return relate_flg;
	}

	public void setRelate_flg(String relate_flg) {
		this.relate_flg = relate_flg;
	}

}
