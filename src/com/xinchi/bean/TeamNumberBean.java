package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class TeamNumberBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String team_number;

	private String sale_pk;

	private String pk;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getSale_pk() {
		return sale_pk;
	}

	public void setSale_pk(String sale_pk) {
		this.sale_pk = sale_pk;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

}
