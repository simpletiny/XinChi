package com.xinchi.backend.order.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.order.service.OrderReportService;
import com.xinchi.bean.OrderReportDto;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)
				&& !roles.contains(ResourcesConstants.USER_ROLE_TICKET)) {
			option.setProduct_manager_number(sessionBean.getUser_number());
		}

		if (!SimpletinyString.isEmpty(option.getConfirm_month())) {
			option.setDeparture_date_from(option.getConfirm_month() + "-01");
			option.setDeparture_date_to(DateUtil.getLastDay(option.getConfirm_month()));
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);

		reports = service.selectOrderReportByPage(page);
		return SUCCESS;
	}

	private String team_number;

	/**
	 * 审核单团核算单
	 * 
	 * @return
	 */
	public String approveTeamReport() {
		resultStr = service.apporveTeamReport(team_number);
		return SUCCESS;
	}

	/**
	 * 打回已审核的单团核算单
	 * 
	 * @return
	 */
	public String rollBackTeamReport() {
		resultStr = service.rollBackTeamReport(team_number);
		return SUCCESS;
	}

	private BigDecimal air_ticket_cost;

	public String fillAirTicketCost() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)
				&& !roles.contains(ResourcesConstants.USER_ROLE_TICKET)) {
			return "noright";
		}
		resultStr = service.fillAriTicketCost(team_number, air_ticket_cost);
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

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public BigDecimal getAir_ticket_cost() {
		return air_ticket_cost;
	}

	public void setAir_ticket_cost(BigDecimal air_ticket_cost) {
		this.air_ticket_cost = air_ticket_cost;
	}
}