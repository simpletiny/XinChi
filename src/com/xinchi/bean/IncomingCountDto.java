package com.xinchi.bean;

import java.io.Serializable;

public class IncomingCountDto implements Serializable {

	private static final long serialVersionUID = -7632183192048915660L;
	private int all_wechat;
	private int core_wechat;
	private int main_wechat;
	private int market_wechat;
	private int try_wechat;
	private int ignore_wechat;
	private int new_wechat;

	private int all_tel;
	private int core_tel;
	private int main_tel;
	private int market_tel;
	private int try_tel;
	private int ignore_tel;
	private int new_tel;

	public int getAll_wechat() {
		return all_wechat;
	}

	public int getMain_wechat() {
		return main_wechat;
	}

	public int getMarket_wechat() {
		return market_wechat;
	}

	public int getTry_wechat() {
		return try_wechat;
	}

	public int getIgnore_wechat() {
		return ignore_wechat;
	}

	public int getNew_wechat() {
		return new_wechat;
	}

	public int getAll_tel() {
		return all_tel;
	}

	public int getMain_tel() {
		return main_tel;
	}

	public int getMarket_tel() {
		return market_tel;
	}

	public int getTry_tel() {
		return try_tel;
	}

	public int getIgnore_tel() {
		return ignore_tel;
	}

	public int getNew_tel() {
		return new_tel;
	}

	public void setAll_wechat(int all_wechat) {
		this.all_wechat = all_wechat;
	}

	public void setMain_wechat(int main_wechat) {
		this.main_wechat = main_wechat;
	}

	public void setMarket_wechat(int market_wechat) {
		this.market_wechat = market_wechat;
	}

	public void setTry_wechat(int try_wechat) {
		this.try_wechat = try_wechat;
	}

	public void setIgnore_wechat(int ignore_wechat) {
		this.ignore_wechat = ignore_wechat;
	}

	public void setNew_wechat(int new_wechat) {
		this.new_wechat = new_wechat;
	}

	public void setAll_tel(int all_tel) {
		this.all_tel = all_tel;
	}

	public void setMain_tel(int main_tel) {
		this.main_tel = main_tel;
	}

	public void setMarket_tel(int market_tel) {
		this.market_tel = market_tel;
	}

	public void setTry_tel(int try_tel) {
		this.try_tel = try_tel;
	}

	public void setIgnore_tel(int ignore_tel) {
		this.ignore_tel = ignore_tel;
	}

	public void setNew_tel(int new_tel) {
		this.new_tel = new_tel;
	}

	public int getCore_wechat() {
		return core_wechat;
	}

	public int getCore_tel() {
		return core_tel;
	}

	public void setCore_wechat(int core_wechat) {
		this.core_wechat = core_wechat;
	}

	public void setCore_tel(int core_tel) {
		this.core_tel = core_tel;
	}

}
