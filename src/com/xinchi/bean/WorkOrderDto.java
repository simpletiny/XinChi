package com.xinchi.bean;

import java.io.Serializable;

public class WorkOrderDto implements Serializable {
	private static final long serialVersionUID = -7532444889715248744L;

	private int all_order;
	private int core_order;
	private int main_order;
	private int friend_order;
	private int market_order;
	private int try_order;
	private int ignore_order;
	private int new_order;

	public int getAll_order() {
		return all_order;
	}

	public int getCore_order() {
		return core_order;
	}

	public int getMain_order() {
		return main_order;
	}

	public int getFriend_order() {
		return friend_order;
	}

	public int getMarket_order() {
		return market_order;
	}

	public int getTry_order() {
		return try_order;
	}

	public int getIgnore_order() {
		return ignore_order;
	}

	public int getNew_order() {
		return new_order;
	}

	public void setAll_order(int all_order) {
		this.all_order = all_order;
	}

	public void setCore_order(int core_order) {
		this.core_order = core_order;
	}

	public void setMain_order(int main_order) {
		this.main_order = main_order;
	}

	public void setFriend_order(int friend_order) {
		this.friend_order = friend_order;
	}

	public void setMarket_order(int market_order) {
		this.market_order = market_order;
	}

	public void setTry_order(int try_order) {
		this.try_order = try_order;
	}

	public void setIgnore_order(int ignore_order) {
		this.ignore_order = ignore_order;
	}

	public void setNew_order(int new_order) {
		this.new_order = new_order;
	}
}
