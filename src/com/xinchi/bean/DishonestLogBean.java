package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class DishonestLogBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -3044009302396014738L;

	private String id;

	private String case_code;

	private String reg_date;

	private String publish_date;

	private String sign_flg;

	private String signal_rating;

	private String court_name;

	// DTO
	private String name;

	public String getId() {
		return id;
	}

	public String getCase_code() {
		return case_code;
	}

	public String getReg_date() {
		return reg_date;
	}

	public String getPublish_date() {
		return publish_date;
	}

	public String getSign_flg() {
		return sign_flg;
	}

	public String getSignal_rating() {
		return signal_rating;
	}

	public String getCourt_name() {
		return court_name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}

	public void setSign_flg(String sign_flg) {
		this.sign_flg = sign_flg;
	}

	public void setSignal_rating(String signal_rating) {
		this.signal_rating = signal_rating;
	}

	public void setCourt_name(String court_name) {
		this.court_name = court_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
