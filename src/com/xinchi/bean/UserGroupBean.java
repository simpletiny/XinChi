package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class UserGroupBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String group_name;

	private String group_leader;

	private String group_duty;

	private String higher_ups;

	private String update_user;

	private String create_user;

	private String pk;

	private String comment;

	private String user_pks;

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

	public String getGroup_duty() {
		return group_duty;
	}

	public void setGroup_duty(String group_duty) {
		this.group_duty = group_duty;
	}

	public String getHigher_ups() {
		return higher_ups;
	}

	public void setHigher_ups(String higher_ups) {
		this.higher_ups = higher_ups;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUser_pks() {
		return user_pks;
	}

	public void setUser_pks(String user_pks) {
		this.user_pks = user_pks;
	}

}
