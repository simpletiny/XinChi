package com.xinchi.bean;

import java.io.Serializable;

public class AccurateSaleDto implements Serializable {

	private static final long serialVersionUID = -5066254146579624723L;

	private int all_accurate;
	private int core_accurate;
	private int main_accurate;
	private int friend_accurate;
	private int market_accurate;
	private int try_accurate;
	private int ignore_accurate;
	private int new_accurate;

	public int getAll_accurate() {
		return all_accurate;
	}

	public int getCore_accurate() {
		return core_accurate;
	}

	public int getMain_accurate() {
		return main_accurate;
	}

	public int getFriend_accurate() {
		return friend_accurate;
	}

	public int getMarket_accurate() {
		return market_accurate;
	}

	public int getTry_accurate() {
		return try_accurate;
	}

	public int getIgnore_accurate() {
		return ignore_accurate;
	}

	public int getNew_accurate() {
		return new_accurate;
	}

	public void setAll_accurate(int all_accurate) {
		this.all_accurate = all_accurate;
	}

	public void setCore_accurate(int core_accurate) {
		this.core_accurate = core_accurate;
	}

	public void setMain_accurate(int main_accurate) {
		this.main_accurate = main_accurate;
	}

	public void setFriend_accurate(int friend_accurate) {
		this.friend_accurate = friend_accurate;
	}

	public void setMarket_accurate(int market_accurate) {
		this.market_accurate = market_accurate;
	}

	public void setTry_accurate(int try_accurate) {
		this.try_accurate = try_accurate;
	}

	public void setIgnore_accurate(int ignore_accurate) {
		this.ignore_accurate = ignore_accurate;
	}

	public void setNew_accurate(int new_accurate) {
		this.new_accurate = new_accurate;
	}

}
