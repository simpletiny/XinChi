package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ProductOrderNameAirNeedBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 8982629725515536022L;

	private String need_pk;

	private String name_pk;

	public String getNeed_pk() {
		return need_pk;
	}

	public String getName_pk() {
		return name_pk;
	}

	public void setNeed_pk(String need_pk) {
		this.need_pk = need_pk;
	}

	public void setName_pk(String name_pk) {
		this.name_pk = name_pk;
	}

}
