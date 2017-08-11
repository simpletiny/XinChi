package com.xinchi.backend.order.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.order.service.OrderReportService;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.OrderReportDto;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderReportAction extends BaseAction {
	private static final long serialVersionUID = 7840369023805017600L;

	@Autowired
	private OrderReportService service;

	private OrderReportDto option;

	private List<OrderReportDto> reports;

	/**
	 * 搜索单团核算报表
	 * 
	 * @return
	 */
	public String searchOrderReportByPage() {
		if (null == option)
			option = new OrderReportDto();
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			option.setSale_name(sessionBean.getUser_name());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);

		reports = service.selectOrderReportByPage(page);
		return SUCCESS;
	}

	public OrderReportDto getOption() {
		return option;
	}

	public void setOption(OrderReportDto option) {
		this.option = option;
	}

	public List<OrderReportDto> getReports() {
		return reports;
	}

	public void setReports(List<OrderReportDto> reports) {
		this.reports = reports;
	}
}