package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class UserCommonBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String login_name;

	private String password;

	private String user_number;

	private String id;

	private String user_name;

	private String sex;

	private String delete_flg;

	private String user_status;

	private String id_file_name;

	private String user_right;

	private String nick_name;

	private String cellphone;
	private String user_roles;

	private BigDecimal credit_limit;
	private BigDecimal credit_balance;

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser_number() {
		return user_number;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDelete_flg() {
		return delete_flg;
	}

	public void setDelete_flg(String delete_flg) {
		this.delete_flg = delete_flg;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}

	public String getUser_right() {
		return user_right;
	}

	public void setUser_right(String user_right) {
		this.user_right = user_right;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getId_file_name() {
		return id_file_name;
	}

	public void setId_file_name(String id_file_name) {
		this.id_file_name = id_file_name;
	}

	public String getUser_roles() {
		return user_roles;
	}

	public void setUser_roles(String user_roles) {
		this.user_roles = user_roles;
	}

	public BigDecimal getCredit_limit() {
		return credit_limit;
	}

	public BigDecimal getCredit_balance() {
		return credit_balance;
	}

	public void setCredit_limit(BigDecimal credit_limit) {
		this.credit_limit = credit_limit;
	}

	public void setCredit_balance(BigDecimal credit_balance) {
		this.credit_balance = credit_balance;
	}

}
