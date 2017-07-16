package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class UserLogBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String method;
	private String method_des;
	private String class_name;
	private String class_des;
	private String target;

	private String parameter;

	private String user_number;
	private String user_name;

	private String time;

	private String pk;

	private String update_user;

	private String create_user;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getUser_number() {
		return user_number;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
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

	public String getMethod_des() {
		return method_des;
	}

	public void setMethod_des(String method_des) {
		this.method_des = method_des;
	}

	public String getClass_des() {
		return class_des;
	}

	public void setClass_des(String class_des) {
		this.class_des = class_des;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

}
