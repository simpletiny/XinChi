package com.xinchi.bean;

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class RelationLevelDto extends SupperBO implements Serializable {
	private static final long serialVersionUID = 8099028678515909603L;

	private String sales;

	private String sales_name;

	private int sum_cnt;

	private int strong_cnt;

	private int middle_cnt;

	private int weak_cnt;

	private int bad_cnt;

	private int unknow_cnt;

	public String getSales() {
		return sales;
	}

	public String getSales_name() {
		return sales_name;
	}

	public int getSum_cnt() {
		return sum_cnt;
	}

	public int getStrong_cnt() {
		return strong_cnt;
	}

	public int getMiddle_cnt() {
		return middle_cnt;
	}

	public int getWeak_cnt() {
		return weak_cnt;
	}

	public int getBad_cnt() {
		return bad_cnt;
	}

	public int getUnknow_cnt() {
		return unknow_cnt;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public void setSales_name(String sales_name) {
		this.sales_name = sales_name;
	}

	public void setSum_cnt(int sum_cnt) {
		this.sum_cnt = sum_cnt;
	}

	public void setStrong_cnt(int strong_cnt) {
		this.strong_cnt = strong_cnt;
	}

	public void setMiddle_cnt(int middle_cnt) {
		this.middle_cnt = middle_cnt;
	}

	public void setWeak_cnt(int weak_cnt) {
		this.weak_cnt = weak_cnt;
	}

	public void setBad_cnt(int bad_cnt) {
		this.bad_cnt = bad_cnt;
	}

	public void setUnknow_cnt(int unknow_cnt) {
		this.unknow_cnt = unknow_cnt;
	}

}
