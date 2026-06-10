package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class TicketNameTempletBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 8060622176269933571L;

	private String templet_name;

	private String file_name;

	private Integer used_count;

	private String last_used_time;

	// DTO
	private String create_user_name;
	private String update_user_name;

	public String getTemplet_name() {
		return templet_name;
	}

	public String getFile_name() {
		return file_name;
	}

	public Integer getUsed_count() {
		return used_count;
	}

	public String getLast_used_time() {
		return last_used_time;
	}

	public void setTemplet_name(String templet_name) {
		this.templet_name = templet_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public void setUsed_count(Integer used_count) {
		this.used_count = used_count;
	}

	public void setLast_used_time(String last_used_time) {
		this.last_used_time = last_used_time;
	}

	public String getCreate_user_name() {
		return create_user_name;
	}

	public String getUpdate_user_name() {
		return update_user_name;
	}

	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}

	public void setUpdate_user_name(String update_user_name) {
		this.update_user_name = update_user_name;
	}

}
