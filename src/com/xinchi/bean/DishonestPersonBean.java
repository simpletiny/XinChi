package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class DishonestPersonBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1891564975677970507L;

	private String name;

	private String id;

	private String dishonest_flg;

	// DTO
	private String code;
	private String msg;
	private List<DishonestLogBean> cases;

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getDishonest_flg() {
		return dishonest_flg;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDishonest_flg(String dishonest_flg) {
		this.dishonest_flg = dishonest_flg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<DishonestLogBean> getCases() {
		return cases;
	}

	public void setCases(List<DishonestLogBean> cases) {
		this.cases = cases;
	}

}
