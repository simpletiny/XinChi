package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class TeamNumberBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String team_number;

	private String user_pk;

	private String type;

	private String pk;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getUser_pk() {
		return user_pk;
	}

	public String getType() {
		return type;
	}

	public void setUser_pk(String user_pk) {
		this.user_pk = user_pk;
	}

	public void setType(String type) {
		this.type = type;
	}

}
