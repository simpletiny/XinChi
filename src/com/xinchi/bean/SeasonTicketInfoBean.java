package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class SeasonTicketInfoBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -8090402997426578276L;

	private Integer ticket_index;

	private String index_leg;

	private Integer start_day;

	private String ticket_number;

	private String ticket_leg;

	private String start_time;

	private String end_time;

	private String add_day_flg;

	private String start_place;

	private String end_place;

	private String base_pk;

	private String pk;

	private String create_user;

	private String update_user;

	public Integer getTicket_index() {
		return ticket_index;
	}

	public String getIndex_leg() {
		return index_leg;
	}

	public Integer getStart_day() {
		return start_day;
	}

	public String getTicket_number() {
		return ticket_number;
	}

	public String getTicket_leg() {
		return ticket_leg;
	}

	public String getStart_time() {
		return start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public String getAdd_day_flg() {
		return add_day_flg;
	}

	public String getStart_place() {
		return start_place;
	}

	public String getEnd_place() {
		return end_place;
	}

	public String getBase_pk() {
		return base_pk;
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

	public void setTicket_index(Integer ticket_index) {
		this.ticket_index = ticket_index;
	}

	public void setIndex_leg(String index_leg) {
		this.index_leg = index_leg;
	}

	public void setStart_day(Integer start_day) {
		this.start_day = start_day;
	}

	public void setTicket_number(String ticket_number) {
		this.ticket_number = ticket_number;
	}

	public void setTicket_leg(String ticket_leg) {
		this.ticket_leg = ticket_leg;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public void setAdd_day_flg(String add_day_flg) {
		this.add_day_flg = add_day_flg;
	}

	public void setStart_place(String start_place) {
		this.start_place = start_place;
	}

	public void setEnd_place(String end_place) {
		this.end_place = end_place;
	}

	public void setBase_pk(String base_pk) {
		this.base_pk = base_pk;
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
