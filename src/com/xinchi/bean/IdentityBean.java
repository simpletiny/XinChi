package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class IdentityBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 2584403803267801834L;

	private String name;

	private String id;

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

}
