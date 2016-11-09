package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class TaskBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String executor;

	private String method;

	private String parameters;

	private String taskType;

	private java.sql.Timestamp executeTime;

	private String isdone;

	private String pk;

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public java.sql.Timestamp getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(java.sql.Timestamp executeTime) {
		this.executeTime = executeTime;
	}

	public String getIsdone() {
		return isdone;
	}

	public void setIsdone(String isdone) {
		this.isdone = isdone;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

}
