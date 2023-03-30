package com.xinchi.bean;

import java.io.Serializable;

/**
 * 公用结果类
 * 
 * @author simpletiny
 *
 */
public class CommonResultDto implements Serializable {
	private static final long serialVersionUID = 2756824535951254589L;
	private String msg;
	private boolean is_done;

	public String getMsg() {
		return msg;
	}

	public boolean isIs_done() {
		return is_done;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setIs_done(boolean is_done) {
		this.is_done = is_done;
	}
}
