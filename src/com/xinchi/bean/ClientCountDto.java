package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ClientCountDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = 3725237971034239995L;

	private int client_one_year_count;
	private int client_more_year_count;
	private int client_30day_count;
	private int client_100day_count;

	public int getClient_one_year_count() {
		return client_one_year_count;
	}

	public int getClient_more_year_count() {
		return client_more_year_count;
	}

	public int getClient_30day_count() {
		return client_30day_count;
	}

	public int getClient_100day_count() {
		return client_100day_count;
	}

	public void setClient_one_year_count(int client_one_year_count) {
		this.client_one_year_count = client_one_year_count;
	}

	public void setClient_more_year_count(int client_more_year_count) {
		this.client_more_year_count = client_more_year_count;
	}

	public void setClient_30day_count(int client_30day_count) {
		this.client_30day_count = client_30day_count;
	}

	public void setClient_100day_count(int client_100day_count) {
		this.client_100day_count = client_100day_count;
	}

}
