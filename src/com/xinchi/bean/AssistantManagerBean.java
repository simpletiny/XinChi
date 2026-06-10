package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class AssistantManagerBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 8765948816135747076L;

	private String assistant_number;

	private String manager_number;

	private String assistant_type;

	public String getAssistant_number() {
		return assistant_number;
	}

	public String getManager_number() {
		return manager_number;
	}

	public String getAssistant_type() {
		return assistant_type;
	}

	public void setAssistant_number(String assistant_number) {
		this.assistant_number = assistant_number;
	}

	public void setManager_number(String manager_number) {
		this.manager_number = manager_number;
	}

	public void setAssistant_type(String assistant_type) {
		this.assistant_type = assistant_type;
	}

}
