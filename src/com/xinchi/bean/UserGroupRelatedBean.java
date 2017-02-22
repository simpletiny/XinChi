package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class UserGroupRelatedBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String user_pk;

	private String group_pk;

	private String update_user;

	private String create_user;

	private String pk;

	public String getUser_pk() {
		return user_pk;
	}

	public void setUser_pk(String user_pk) {
		this.user_pk = user_pk;
	}

	public String getGroup_pk() {
		return group_pk;
	}

	public void setGroup_pk(String group_pk) {
		this.group_pk = group_pk;
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

}
