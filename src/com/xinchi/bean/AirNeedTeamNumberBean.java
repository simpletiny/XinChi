package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class AirNeedTeamNumberBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -3714511875572485899L;

	private String need_pk;

	private String team_number;

	private String pk;

	private String create_user;

	private String update_user;

	public String getNeed_pk() {
		return need_pk;
	}

	public String getTeam_number() {
		return team_number;
	}

	public String getPk() {
		return pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setNeed_pk(String need_pk) {
		this.need_pk = need_pk;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

}
