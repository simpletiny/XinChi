package com.xinchi.backend.receivable.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.ReceivableSummaryBean;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReceivableAction extends BaseAction {
	private static final long serialVersionUID = 2614822711242785923L;

	private ReceivableSummaryBean summary;

	@Autowired
	private ReceivableService receivableService;
	@Autowired
	private UserDAO userDao;
	private String sales_name;

	public String searchReceivableSummary() {
		String user_number = "";
		if (!SimpletinyString.isEmpty(sales_name)) {
			UserBaseBean user = userDao.selectUserByName(sales_name);
			user_number = user.getUser_number();
		}

		summary = receivableService.searchReceivableSummary(user_number);
		if (null == summary)
			summary = new ReceivableSummaryBean();
		return SUCCESS;
	}

	private ReceivableBean receivable;
	private List<ReceivableBean> receivables;

	public String searchReceivableByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			receivable.setSales(sessionBean.getUser_number());
		}
		String team_status = receivable.getTeam_status();
		String departure_from = "";
		String departure_to = "";
		String return_to = "";
		if (!SimpletinyString.isEmpty(team_status)) {
			if (team_status.equals(ResourcesConstants.TEAM_STATUS_BEFORE)) {
				departure_from = DateUtil.getDateStr(DateUtil.YYYY_MM_DD);
			} else if (team_status.equals(ResourcesConstants.TEAM_STATUS_AFTER)) {
				departure_to = DateUtil.getDateStr(DateUtil.YYYY_MM_DD);
			} else if (team_status.equals(ResourcesConstants.TEAM_STATUS_RETURN)) {
				return_to = DateUtil.getDateStr(DateUtil.YYYY_MM_DD);
			}
		}
		receivable.setDeparture_from(departure_from);
		receivable.setDeparture_to(departure_to);
		receivable.setReturn_to(return_to);
		if (receivable.getSort_type().equals("倒序")) {
			receivable.setSort_type("D");
		} else if (receivable.getSort_type().equals("正序")) {
			receivable.setSort_type("Z");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", receivable);
		page.setParams(params);

		receivables = receivableService.searchReceivableByPage(page);
		return SUCCESS;
	}

	private String client_employee_pks;

	@Autowired
	private EmployeeService employeeService;

	// 判断是否为同一财务主体
	public String isSameFinancialBody() {
		String employee_pks[] = client_employee_pks.split(",");
		List<String> body_pks = employeeService.getBodyPksByEmployeePks(employee_pks);
		if (body_pks.size() > 1) {
			resultStr = "NOT";
		} else {
			resultStr = OK;
		}
		return SUCCESS;
	}

	public ReceivableSummaryBean getSummary() {
		return summary;
	}

	public void setSummary(ReceivableSummaryBean summary) {
		this.summary = summary;
	}

	public ReceivableBean getReceivable() {
		return receivable;
	}

	public void setReceivable(ReceivableBean receivable) {
		this.receivable = receivable;
	}

	public List<ReceivableBean> getReceivables() {
		return receivables;
	}

	public void setReceivables(List<ReceivableBean> receivables) {
		this.receivables = receivables;
	}

	public String getSales_name() {
		return sales_name;
	}

	public void setSales_name(String sales_name) {
		this.sales_name = sales_name;
	}

	public String getClient_employee_pks() {
		return client_employee_pks;
	}

	public void setClient_employee_pks(String client_employee_pks) {
		this.client_employee_pks = client_employee_pks;
	}
}
