package com.xinchi.bean;

import com.xinchi.common.DateUtil;

public class TempBean implements Comparable<TempBean> {

	private String connect_date;
	private String type;

	@Override
	public int compareTo(TempBean o) {
		if (DateUtil.compare(this.connect_date, o.getConnect_date()) <= 1) {
			return 1;
		}
		return -1;
	}

	public String getConnect_date() {
		return connect_date;
	}

	public String getType() {
		return type;
	}

	public void setConnect_date(String connect_date) {
		this.connect_date = connect_date;
	}

	public void setType(String type) {
		this.type = type;
	}

}
