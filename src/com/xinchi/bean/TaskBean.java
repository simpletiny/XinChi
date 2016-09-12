package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class TaskBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private java.lang.String executor;

	private java.lang.String method;

	private java.lang.String parameters;

	private java.lang.String taskType;

	private java.sql.Timestamp executeTime;

	private java.lang.String isdone;

	public java.lang.String getExecutor() {
		return executor;
	}

	public void setExecutor(java.lang.String executor) {
		this.executor = executor;
	}

	public java.lang.String getMethod() {
		return method;
	}

	public void setMethod(java.lang.String method) {
		this.method = method;
	}

	public java.lang.String getParameters() {
		return parameters;
	}

	public void setParameters(java.lang.String parameters) {
		this.parameters = parameters;
	}

	public java.lang.String getTaskType() {
		return taskType;
	}

	public void setTaskType(java.lang.String taskType) {
		this.taskType = taskType;
	}

	public java.sql.Timestamp getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(java.sql.Timestamp executeTime) {
		this.executeTime = executeTime;
	}

	public java.lang.String getIsdone() {
		return isdone;
	}

	public void setIsdone(java.lang.String isdone) {
		this.isdone = isdone;
	}

}
