package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ProductOrderTeamNumberBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -8946952271477520251L;

	private String product_order_number;

	private String team_number;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getProduct_order_number() {
		return product_order_number;
	}

	public void setProduct_order_number(String product_order_number) {
		this.product_order_number = product_order_number;
	}

}
