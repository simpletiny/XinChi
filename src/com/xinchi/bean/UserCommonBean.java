package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class UserCommonBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private java.lang.String login_name;

	private java.lang.String password;

	private java.lang.String user_number;

	private java.lang.String id;

	private java.lang.String user_name;

	private java.lang.String sex;

	private java.lang.String delete_flg;

	private java.lang.String user_status;

	private java.lang.String id_file_name;

	private java.lang.String user_right;

	private java.lang.String nick_name;

	private java.lang.String cellphone;

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

	public java.lang.String getUser_right() {
		return user_right;
	}

	public void setUser_right(java.lang.String user_right) {
		this.user_right = user_right;
	}

	public java.lang.String getNick_name() {
		return nick_name;
	}

	public void setNick_name(java.lang.String nick_name) {
		this.nick_name = nick_name;
	}

	public java.lang.String getCellphone() {
		return cellphone;
	}

	public void setCellphone(java.lang.String cellphone) {
		this.cellphone = cellphone;
	}

	public java.lang.String getId_file_name() {
		return id_file_name;
	}

	public void setId_file_name(java.lang.String id_file_name) {
		this.id_file_name = id_file_name;
	}

}
