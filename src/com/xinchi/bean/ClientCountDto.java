package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class ClientCountDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = 3725237971034239995L;

	private int oneYearorderCnt;
	private int moreYearorderCnt;

	public int getOneYearorderCnt() {
		return oneYearorderCnt;
	}

	public void setOneYearorderCnt(int oneYearorderCnt) {
		this.oneYearorderCnt = oneYearorderCnt;
	}

	public int getMoreYearorderCnt() {
		return moreYearorderCnt;
	}

	public void setMoreYearorderCnt(int moreYearorderCnt) {
		this.moreYearorderCnt = moreYearorderCnt;
	}

}
