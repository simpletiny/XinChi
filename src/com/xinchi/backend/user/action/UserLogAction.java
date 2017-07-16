package com.xinchi.backend.user.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.user.service.UserLogService;
import com.xinchi.bean.UserLogBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserLogAction extends BaseAction {
	private static final long serialVersionUID = -5968475399225349667L;

	@Autowired
	private UserLogService service;

	private List<UserLogBean> logs;

	private UserLogBean userLog;

	public String searchUserLogs() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", userLog);
		page.setParams(params);
		logs = service.selectByPage(page);
		return SUCCESS;
	}

	public List<UserLogBean> getLogs() {
		return logs;
	}

	public void setLogs(List<UserLogBean> logs) {
		this.logs = logs;
	}

	public UserLogBean getUserLog() {
		return userLog;
	}

	public void setUserLog(UserLogBean userLog) {
		this.userLog = userLog;
	}

}