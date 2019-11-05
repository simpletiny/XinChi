package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class BaseDataBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 2139472754070877356L;

	private String name;

	private String code;

	private String type;

	private Integer order_index;

	private Integer level;

	private String father_level_pk;

	private String ext1;

	private String ext2;

	private String ext3;

	private String delete_flg;

	private String can_edit;

	private String create_user;

	private String update_user;

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public Integer getOrder_index() {
		return order_index;
	}

	public Integer getLevel() {
		return level;
	}

	public String getFather_level_pk() {
		return father_level_pk;
	}

	public String getExt1() {
		return ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public String getDelete_flg() {
		return delete_flg;
	}

	public String getCan_edit() {
		return can_edit;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setOrder_index(Integer order_index) {
		this.order_index = order_index;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setFather_level_pk(String father_level_pk) {
		this.father_level_pk = father_level_pk;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public void setDelete_flg(String delete_flg) {
		this.delete_flg = delete_flg;
	}

	public void setCan_edit(String can_edit) {
		this.can_edit = can_edit;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

}
