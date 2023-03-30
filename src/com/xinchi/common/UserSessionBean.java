package com.xinchi.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class UserSessionBean {
	private String id;
	private String user_number;

	private String pk;
	private String user_name;
	private String nick_name;
	private String cellphone;
	private String user_status;
	private String user_roles;

	private String login_time;

	private List<String> current_page;

	private String current_url;

	private BigDecimal credit_limit;
	private BigDecimal credit_balance;

	private Set<String> authorized_pages;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
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

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}

	public String getUser_number() {
		return user_number;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public String getUser_roles() {
		return user_roles;
	}

	public void setUser_roles(String user_roles) {
		this.user_roles = user_roles;
	}

	public String getLogin_time() {
		return login_time;
	}

	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}

	public List<String> getCurrent_page() {
		return current_page;
	}

	public void setCurrent_page(List<String> current_page) {
		this.current_page = current_page;
	}

	public String getCurrent_url() {
		return current_url;
	}

	public void setCurrent_url(String current_url) {
		this.current_url = current_url;
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

	public Set<String> getAuthorized_pages() {
		return authorized_pages;
	}

	public void setAuthorized_pages(Set<String> authorized_pages) {
		this.authorized_pages = authorized_pages;
	}

	// public boolean equals(Object obj) {
	// UserSessionBean user = (UserSessionBean) obj;
	// if (pk.equals(user.getPk())) {
	// return true;
	// }
	// return false;
	// }
}
