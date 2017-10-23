package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class TravelAgencyBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String agency_name;

	private String credit_code;

	private String agency_provice;

	private String agency_city;

	private String is_exit;

	private String main_bussines;

	private String agency_type;

	private String comment;

	private String corporate_name;

	private String corporate_sex;

	private String corporate_cellphone;

	private String corporate_id;

	private String pk;

	private String update_user;

	private String create_user;

	private String is_cancel;

	private Integer agency_client_count;

	public String getAgency_name() {
		return agency_name;
	}

	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}

	public String getCredit_code() {
		return credit_code;
	}

	public void setCredit_code(String credit_code) {
		this.credit_code = credit_code;
	}

	public String getAgency_provice() {
		return agency_provice;
	}

	public void setAgency_provice(String agency_provice) {
		this.agency_provice = agency_provice;
	}

	public String getAgency_city() {
		return agency_city;
	}

	public void setAgency_city(String agency_city) {
		this.agency_city = agency_city;
	}

	public String getIs_exit() {
		return is_exit;
	}

	public void setIs_exit(String is_exit) {
		this.is_exit = is_exit;
	}

	public String getMain_bussines() {
		return main_bussines;
	}

	public void setMain_bussines(String main_bussines) {
		this.main_bussines = main_bussines;
	}

	public String getAgency_type() {
		return agency_type;
	}

	public void setAgency_type(String agency_type) {
		this.agency_type = agency_type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCorporate_name() {
		return corporate_name;
	}

	public void setCorporate_name(String corporate_name) {
		this.corporate_name = corporate_name;
	}

	public String getCorporate_sex() {
		return corporate_sex;
	}

	public void setCorporate_sex(String corporate_sex) {
		this.corporate_sex = corporate_sex;
	}

	public String getCorporate_cellphone() {
		return corporate_cellphone;
	}

	public void setCorporate_cellphone(String corporate_cellphone) {
		this.corporate_cellphone = corporate_cellphone;
	}

	public String getCorporate_id() {
		return corporate_id;
	}

	public void setCorporate_id(String corporate_id) {
		this.corporate_id = corporate_id;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getIs_cancel() {
		return is_cancel;
	}

	public void setIs_cancel(String is_cancel) {
		this.is_cancel = is_cancel;
	}

	public Integer getAgency_client_count() {
		return agency_client_count;
	}

	public void setAgency_client_count(Integer agency_client_count) {
		this.agency_client_count = agency_client_count;
	}

}
