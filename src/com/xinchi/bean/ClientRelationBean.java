package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ClientRelationBean extends SupperBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5619150926891066168L;

	private String client_employee_name;

	private String nick_name;

	private String client_employee_pk;

	private String sales;

	private String sales_name;

	private String delete_flg;

	private String relation_level;

	private String back_level;

	private String market_level;

	private Integer month_order_count;

	private Integer year_order_count;

	private String last_confirm_date;

	private Integer last_order_period;

	private java.math.BigDecimal receivable;

	private Integer last_receivable_period;

	private String connect_date;

	private String type;

	private String extra_info;

	private String pk;

	private String create_user;

	private String update_user;

	public String getClient_employee_name() {
		return client_employee_name;
	}

	public String getNick_name() {
		return nick_name;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public String getSales() {
		return sales;
	}

	public String getSales_name() {
		return sales_name;
	}

	public String getDelete_flg() {
		return delete_flg;
	}

	public String getRelation_level() {
		return relation_level;
	}

	public String getBack_level() {
		return back_level;
	}

	public String getMarket_level() {
		return market_level;
	}

	public Integer getMonth_order_count() {
		return month_order_count;
	}

	public Integer getYear_order_count() {
		return year_order_count;
	}

	public String getLast_confirm_date() {
		return last_confirm_date;
	}

	public Integer getLast_order_period() {
		return last_order_period;
	}

	public java.math.BigDecimal getReceivable() {
		return receivable;
	}

	public Integer getLast_receivable_period() {
		return last_receivable_period;
	}

	public String getConnect_date() {
		return connect_date;
	}

	public String getType() {
		return type;
	}

	public String getExtra_info() {
		return extra_info;
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

	public void setClient_employee_name(String client_employee_name) {
		this.client_employee_name = client_employee_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public void setSales_name(String sales_name) {
		this.sales_name = sales_name;
	}

	public void setDelete_flg(String delete_flg) {
		this.delete_flg = delete_flg;
	}

	public void setRelation_level(String relation_level) {
		this.relation_level = relation_level;
	}

	public void setBack_level(String back_level) {
		this.back_level = back_level;
	}

	public void setMarket_level(String market_level) {
		this.market_level = market_level;
	}

	public void setMonth_order_count(Integer month_order_count) {
		this.month_order_count = month_order_count;
	}

	public void setYear_order_count(Integer year_order_count) {
		this.year_order_count = year_order_count;
	}

	public void setLast_confirm_date(String last_confirm_date) {
		this.last_confirm_date = last_confirm_date;
	}

	public void setLast_order_period(Integer last_order_period) {
		this.last_order_period = last_order_period;
	}

	public void setReceivable(java.math.BigDecimal receivable) {
		this.receivable = receivable;
	}

	public void setLast_receivable_period(Integer last_receivable_period) {
		this.last_receivable_period = last_receivable_period;
	}

	public void setConnect_date(String connect_date) {
		this.connect_date = connect_date;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setExtra_info(String extra_info) {
		this.extra_info = extra_info;
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

}
