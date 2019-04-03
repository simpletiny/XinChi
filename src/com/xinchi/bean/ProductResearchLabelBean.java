package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ProductResearchLabelBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1005190767346976751L;

	private String label_name;

	private String label_tag;

	private String pk;

	private String update_user;

	private String create_user;
	
	private int label_index;

	public String getLabel_name() {
		return label_name;
	}

	public String getLabel_tag() {
		return label_tag;
	}

	public String getPk() {
		return pk;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setLabel_name(String label_name) {
		this.label_name = label_name;
	}

	public void setLabel_tag(String label_tag) {
		this.label_tag = label_tag;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public int getLabel_index() {
		return label_index;
	}

	public void setLabel_index(int label_index) {
		this.label_index = label_index;
	}

}
