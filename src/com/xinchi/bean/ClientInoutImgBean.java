package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ClientInoutImgBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 6578320468778555389L;
	public static String IMG_TYPE_OUT = "O";
	public static String IMG_TYPE_IN = "i";

	private String img_name;
	private String client_pk;
	private String img_type;

	public String getImg_name() {
		return img_name;
	}

	public String getClient_pk() {
		return client_pk;
	}

	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}

	public void setClient_pk(String client_pk) {
		this.client_pk = client_pk;
	}

	public String getImg_type() {
		return img_type;
	}

	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}

}
