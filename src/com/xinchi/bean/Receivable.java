package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

/**
 * 应收款信息
 * 
 * @author simpletiny
 * 
 */
public class Receivable extends SupperBO implements Serializable {
	private static final long serialVersionUID = -3726795740247926997L;

	private String team_number;
	private int back_days;
	private String final_flg;
	private String client_employee_name;
	private String client_employee_pk;
	private String departure_date;
	private String product;
	private int people_count;
	private BigDecimal sum_receivable;
	private BigDecimal received;
	private BigDecimal balance;
	private String sales;
	private String sales_name;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public int getBack_days() {
		return back_days;
	}

	public void setBack_days(int back_days) {
		this.back_days = back_days;
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

	public int getPeople_count() {
		return people_count;
	}

	public void setPeople_count(int people_count) {
		this.people_count = people_count;
	}

	public BigDecimal getSum_receivable() {
		return sum_receivable;
	}

	public void setSum_receivable(BigDecimal sum_receivable) {
		this.sum_receivable = sum_receivable;
	}

	public BigDecimal getReceived() {
		return received;
	}

	public void setReceived(BigDecimal received) {
		this.received = received;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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

}
