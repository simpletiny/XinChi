package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class ProductGroupBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String group_name;

	private String group_leader;

	private String comment;

	private String pk;

	private String create_user;

	private String update_user;

	private List<String> supplier_pks;

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getGroup_leader() {
		return group_leader;
	}

	public void setGroup_leader(String group_leader) {
		this.group_leader = group_leader;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public List<String> getSupplier_pks() {
		return supplier_pks;
	}

	public void setSupplier_pks(List<String> supplier_pks) {
		this.supplier_pks = supplier_pks;
	}

}
