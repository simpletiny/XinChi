package com.xinchi.backend.receivable.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.ReceivableSummaryBean;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.common.BaseAction;
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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			receivable.setSales(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", receivable);
		page.setParams(params);

		receivables = receivableService.searchReceivableByPage(page);
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
}
