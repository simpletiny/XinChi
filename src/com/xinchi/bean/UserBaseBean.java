package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class UserBaseBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private java.lang.String login_name;

	private java.lang.String password;

	private java.lang.String user_number;

	private java.lang.String id;

	private java.lang.String user_name;

	private java.lang.String sex;

	private java.lang.String delete_flg;

	private java.lang.String user_status;

	public java.lang.String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(java.lang.String login_name) {
		this.login_name = login_name;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public java.lang.String getUser_number() {
		return user_number;
	}

	public void setUser_number(java.lang.String user_number) {
		this.user_number = user_number;
	}

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getUser_name() {
		return user_name;
	}

	public void setUser_name(java.lang.String user_name) {
		this.user_name = user_name;
	}

	public java.lang.String getSex() {
		return sex;
	}

	public void setSex(java.lang.String sex) {
		this.sex = sex;
	}

	public java.lang.String getDelete_flg() {
		return delete_flg;
	}

	public void setDelete_flg(java.lang.String delete_flg) {
		this.delete_flg = delete_flg;
	}

	public java.lang.String getUser_status() {
		return user_status;
	}

	public void setUser_status(java.lang.String user_status) {
		this.user_status = user_status;
	}

}
