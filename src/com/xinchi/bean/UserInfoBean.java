package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class UserInfoBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 8678384957718129327L;

	private java.lang.String id;

	private java.lang.String id_file_name;

	private java.lang.String user_right;

	private java.lang.String pk;

	private java.lang.String nick_name;

	private java.lang.String cellphone;

	private String user_role;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getUser_right() {
		return user_right;
	}

	public void setUser_right(java.lang.String user_right) {
		this.user_right = user_right;
	}

	public java.lang.String getPk() {
		return pk;
	}

	public void setPk(java.lang.String pk) {
		this.pk = pk;
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

	public String getUser_role() {
		return user_role;
	}

	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}

}
