package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class AirTicketNeedBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -8836123001411020314L;

	private String confirm_flg;

	private String ticket_client_number;
	private String ticket_client_name;

	private String first_ticket_date;

	private String first_from_to;

	private int people_count;

	private String team_number;

	private String product_pk;
	private String sale_order_pk;

	private BigDecimal air_ticket_cost;
	private String standard_flg;

	private String passenger;
	private List<String> status;

	public String getConfirm_flg() {
		return confirm_flg;
	}

	public void setConfirm_flg(String confirm_flg) {
		this.confirm_flg = confirm_flg;
	}

	public String getTicket_client_number() {
		return ticket_client_number;
	}

	public void setTicket_client_number(String ticket_client_number) {
		this.ticket_client_number = ticket_client_number;
	}

	public String getTicket_client_name() {
		return ticket_client_name;
	}

	public void setTicket_client_name(String ticket_client_name) {
		this.ticket_client_name = ticket_client_name;
	}

	public String getFirst_ticket_date() {
		return first_ticket_date;
	}

	public void setFirst_ticket_date(String first_ticket_date) {
		this.first_ticket_date = first_ticket_date;
	}

	public String getFirst_from_to() {
		return first_from_to;
	}

	public void setFirst_from_to(String first_from_to) {
		this.first_from_to = first_from_to;
	}

	public int getPeople_count() {
		return people_count;
	}

	public void setPeople_count(int people_count) {
		this.people_count = people_count;
	}

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getProduct_pk() {
		return product_pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public String getSale_order_pk() {
		return sale_order_pk;
	}

	public void setSale_order_pk(String sale_order_pk) {
		this.sale_order_pk = sale_order_pk;
	}

	public BigDecimal getAir_ticket_cost() {
		return air_ticket_cost;
	}

	public void setAir_ticket_cost(BigDecimal air_ticket_cost) {
		this.air_ticket_cost = air_ticket_cost;
	}

	public String getStandard_flg() {
		return standard_flg;
	}

	public void setStandard_flg(String standard_flg) {
		this.standard_flg = standard_flg;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public String getPassenger() {
		return passenger;
	}

	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}

}
