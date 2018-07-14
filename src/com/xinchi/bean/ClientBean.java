package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class ClientBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String client_name;

	private String client_short_name;

	private String client_area;

	private String client_county;

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
	private Integer client_employee_count;
	private BigDecimal sum_balance;
	private Integer client_year_order_count;
	private String last_order_date;

	private String main_business;
	private String store_type;
	private String back_level;

	private String market_level;
	private String talk_level;

	private List<ClientUserBean> client_users;
	// search options
	
	private List<String> public_flgs;
	private List<String> statuses;
	private List<String> relate_flgs;
	private List<String> talk_levels;
	private List<String> main_businesses;

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

	public Integer getClient_employee_count() {
		return client_employee_count;
	}

	public void setClient_employee_count(Integer client_employee_count) {
		this.client_employee_count = client_employee_count;
	}

	public BigDecimal getSum_balance() {
		return sum_balance;
	}

	public void setSum_balance(BigDecimal sum_balance) {
		this.sum_balance = sum_balance;
	}

	public Integer getClient_year_order_count() {
		return client_year_order_count;
	}

	public void setClient_year_order_count(Integer client_year_order_count) {
		this.client_year_order_count = client_year_order_count;
	}

	public String getLast_order_date() {
		return last_order_date;
	}

	public void setLast_order_date(String last_order_date) {
		this.last_order_date = last_order_date;
	}

	public List<String> getRelate_flgs() {
		return relate_flgs;
	}

	public void setRelate_flgs(List<String> relate_flgs) {
		this.relate_flgs = relate_flgs;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}

	public String getMain_business() {
		return main_business;
	}

	public void setMain_business(String main_business) {
		this.main_business = main_business;
	}

	public String getStore_type() {
		return store_type;
	}

	public void setStore_type(String store_type) {
		this.store_type = store_type;
	}

	public String getBack_level() {
		return back_level;
	}

	public void setBack_level(String back_level) {
		this.back_level = back_level;
	}

	public String getMarket_level() {
		return market_level;
	}

	public void setMarket_level(String market_level) {
		this.market_level = market_level;
	}

	public String getTalk_level() {
		return talk_level;
	}

	public void setTalk_level(String talk_level) {
		this.talk_level = talk_level;
	}

	public List<String> getTalk_levels() {
		return talk_levels;
	}

	public void setTalk_levels(List<String> talk_levels) {
		this.talk_levels = talk_levels;
	}

	public List<String> getMain_businesses() {
		return main_businesses;
	}

	public void setMain_businesses(List<String> main_businesses) {
		this.main_businesses = main_businesses;
	}

	public String getClient_county() {
		return client_county;
	}

	public void setClient_county(String client_county) {
		this.client_county = client_county;
	}

	public List<ClientUserBean> getClient_users() {
		return client_users;
	}

	public void setClient_users(List<ClientUserBean> client_users) {
		this.client_users = client_users;
	}

	public List<String> getPublic_flgs() {
		return public_flgs;
	}

	public void setPublic_flgs(List<String> public_flgs) {
		this.public_flgs = public_flgs;
	}

}
