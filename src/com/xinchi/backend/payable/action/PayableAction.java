package com.xinchi.backend.payable.action;

import static com.xinchi.common.SimpletinyString.isEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.payable.service.PayableService;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.PayableSummaryBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayableAction extends BaseAction {
	private static final long serialVersionUID = 2614822711242785923L;

	private PayableSummaryBean summary;

	@Autowired
	private PayableService payableService;

	private String sales_name;

	public String searchPayableSummary() {

		String user_number = "";

		if (!isEmpty(sales_name)) {
			user_number = String.valueOf(ResourcesConstants.MAP_USER_NAME.get(sales_name));
		}

		summary = payableService.searchPayableSummary(user_number);

		if (null == summary)
			summary = new PayableSummaryBean();

		return SUCCESS;
	}

	private PayableBean payable;
	private List<PayableBean> payables;

	public String searchPayableByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			payable.setSales(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", payable);
		page.setParams(params);

		payables = payableService.searchPayableByPage(page);
		return SUCCESS;
	}

	private String client_employee_pks;

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

	public PayableSummaryBean getSummary() {
		return summary;
	}

	public void setSummary(PayableSummaryBean summary) {
		this.summary = summary;
	}

	public PayableBean getPayable() {
		return payable;
	}

	public void setPayable(PayableBean payable) {
		this.payable = payable;
	}

	public List<PayableBean> getPayables() {
		return payables;
	}

	public void setPayables(List<PayableBean> payables) {
		this.payables = payables;
	}
}
