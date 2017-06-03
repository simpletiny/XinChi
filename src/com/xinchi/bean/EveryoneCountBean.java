package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class EveryoneCountBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 680801366803633016L;

	private Integer count;

	private String type;

	private String pk;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

}
