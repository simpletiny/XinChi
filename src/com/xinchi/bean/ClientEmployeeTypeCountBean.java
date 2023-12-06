package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ClientEmployeeTypeCountBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 7270394593689644046L;

	private String month;
	private String user_pk;
	private Integer all_count;
	private Integer core_count;
	private Integer main_count;
	private Integer market_count;
	private Integer try_count;
	private Integer ignore_count;
	private Integer new_count;
	private String create_user;
	private String update_user;

	public String getMonth() {
		return month;
	}

	public String getUser_pk() {
		return user_pk;
	}

	public Integer getCore_count() {
		return core_count;
	}

	public Integer getMain_count() {
		return main_count;
	}

	public Integer getMarket_count() {
		return market_count;
	}

	public Integer getTry_count() {
		return try_count;
	}

	public Integer getIgnore_count() {
		return ignore_count;
	}

	public Integer getNew_count() {
		return new_count;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setUser_pk(String user_pk) {
		this.user_pk = user_pk;
	}

	public void setCore_count(Integer core_count) {
		this.core_count = core_count;
	}

	public void setMain_count(Integer main_count) {
		this.main_count = main_count;
	}

	public void setMarket_count(Integer market_count) {
		this.market_count = market_count;
	}

	public void setTry_count(Integer try_count) {
		this.try_count = try_count;
	}

	public void setIgnore_count(Integer ignore_count) {
		this.ignore_count = ignore_count;
	}

	public void setNew_count(Integer new_count) {
		this.new_count = new_count;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public Integer getAll_count() {
		return core_count + main_count + market_count + try_count + ignore_count;
	}

	public void setAll_count(Integer all_count) {
		this.all_count = all_count;
	}

}
