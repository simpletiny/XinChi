package com.xinchi.backend.util.action;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class STDateAction extends BaseAction {
	private static final long serialVersionUID = 5308125926604141873L;

	private int current_month;
	private String current_date;

	public String currentDate() {
		current_date = DateUtil.getDateStr("yyyy-MM-dd");
		return SUCCESS;
	}

	public int getCurrent_month() {
		return current_month;
	}

	public void setCurrent_month(int current_month) {
		this.current_month = current_month;
	}

	public String getCurrent_date() {
		return current_date;
	}

	public void setCurrent_date(String current_date) {
		this.current_date = current_date;
	}
}
