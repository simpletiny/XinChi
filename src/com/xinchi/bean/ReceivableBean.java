package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class ReceivableBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -8054012584061695464L;

	private String team_number;

	private String final_flg;

	private String client_employee_name;

	private String client_employee_pk;

	private String departure_date;

	private String return_date;

	private String product;

	private Integer people_count;

	private BigDecimal budget_receivable;

	private BigDecimal final_receivable;

	private BigDecimal received;

	private BigDecimal budget_balance;

	private BigDecimal final_balance;

	private String sales;

	private String sales_name;

	private String update_user;

	private String pk;

	private String create_user;
	
	private String back_days;
	
	private String financial_body_name;
	private String financial_body_pk;
	
	// search condition
	private String departure_month;
	private String team_status;
	private String type;

	private String sort_type;
	private String departure_from;
	private String departure_to;
	private String return_to;
	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getFinal_flg() {
		return final_flg;
	}

	public void setFinal_flg(String final_flg) {
		this.final_flg = final_flg;
	}

	public String getClient_employee_name() {
		return client_employee_name;
	}

	public void setClient_employee_name(String client_employee_name) {
		this.client_employee_name = client_employee_name;
	}

	public String getClient_employee_pk() {
		return client_employee_pk;
	}

	public void setClient_employee_pk(String client_employee_pk) {
		this.client_employee_pk = client_employee_pk;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Integer getPeople_count() {
		return people_count;
	}

	public void setPeople_count(Integer people_count) {
		this.people_count = people_count;
	}

	public BigDecimal getBudget_receivable() {
		return budget_receivable;
	}

	public void setBudget_receivable(BigDecimal budget_receivable) {
		this.budget_receivable = budget_receivable;
	}

	public BigDecimal getFinal_receivable() {
		return final_receivable;
	}

	public void setFinal_receivable(BigDecimal final_receivable) {
		this.final_receivable = final_receivable;
	}

	public BigDecimal getReceived() {
		return received;
	}

	public void setReceived(BigDecimal received) {
		this.received = received;
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

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
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

	public String getReturn_date() {
		return return_date;
	}

	public void setReturn_date(String return_date) {
		this.return_date = return_date;
	}

	public BigDecimal getBudget_balance() {
		return budget_balance;
	}

	public void setBudget_balance(BigDecimal budget_balance) {
		this.budget_balance = budget_balance;
	}

	public BigDecimal getFinal_balance() {
		return final_balance;
	}

	public void setFinal_balance(BigDecimal final_balance) {
		this.final_balance = final_balance;
	}

	public String getDeparture_month() {
		return departure_month;
	}

	public void setDeparture_month(String departure_month) {
		this.departure_month = departure_month;
	}

	public String getTeam_status() {
		return team_status;
	}

	public void setTeam_status(String team_status) {
		this.team_status = team_status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBack_days() {
		return back_days;
	}

	public void setBack_days(String back_days) {
		this.back_days = back_days;
	}

	public String getSort_type() {
		return sort_type;
	}

	public void setSort_type(String sort_type) {
		this.sort_type = sort_type;
	}

	public String getFinancial_body_name() {
		return financial_body_name;
	}

	public void setFinancial_body_name(String financial_body_name) {
		this.financial_body_name = financial_body_name;
	}

	public String getFinancial_body_pk() {
		return financial_body_pk;
	}

	public void setFinancial_body_pk(String financial_body_pk) {
		this.financial_body_pk = financial_body_pk;
	}

	public String getDeparture_from() {
		return departure_from;
	}

	public void setDeparture_from(String departure_from) {
		this.departure_from = departure_from;
	}

	public String getDeparture_to() {
		return departure_to;
	}

	public void setDeparture_to(String departure_to) {
		this.departure_to = departure_to;
	}

	public String getReturn_to() {
		return return_to;
	}

	public void setReturn_to(String return_to) {
		this.return_to = return_to;
	}

}
