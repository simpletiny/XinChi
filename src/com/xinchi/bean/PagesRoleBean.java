package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

/**
 * 页面权限表
 * 
 * @author simpletiny
 *
 */
public class PagesRoleBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 4990874607567418022L;

	private String role;

	private String page_pk;

	private String page_url;

	private String is_father;

	public String getRole() {
		return role;
	}

	public String getPage_pk() {
		return page_pk;
	}

	public String getPage_url() {
		return page_url;
	}

	public String getIs_father() {
		return is_father;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setPage_pk(String page_pk) {
		this.page_pk = page_pk;
	}

	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}

	public void setIs_father(String is_father) {
		this.is_father = is_father;
	}

}
