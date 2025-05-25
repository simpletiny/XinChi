package com.xinchi.backend.order.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.order.service.OrderReportService;
import com.xinchi.backend.payable.service.AirTicketPayableService;
import com.xinchi.bean.AirOtherPaymentDto;
import com.xinchi.bean.AirServiceFeeDto;
import com.xinchi.bean.OrderReportDto;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.TeamReportBean;
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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		// if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)
		// && !roles.contains(ResourcesConstants.USER_ROLE_TICKET)) {
		// option.setProduct_manager_number(sessionBean.getUser_number());
		// }

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
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

	private OrderReportDto report;

	@Autowired
	private AirTicketPayableService airTicketPayableService;

	@Autowired
	private PaymentDetailService paymentDetailService;

	/**
	 * 单团核算单汇总
	 * 
	 * @return
	 */
	public String searchSumReport() {
		if (null == option)
			option = new OrderReportDto();
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		String from_month = option.getDate_from();
		String to_month = option.getDate_to();
		String product_manager_number = option.getProduct_manager_number();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			option.setProduct_manager_number(sessionBean.getUser_number());
		}

		if (!SimpletinyString.isEmpty(option.getDate_from())) {
			option.setDate_from(option.getDate_from() + "-00");
		}
		if (!SimpletinyString.isEmpty(option.getDate_to())) {
			option.setDate_to(option.getDate_to() + "-31");
		}

		report = service.searchSumReport(option);
		boolean no_data = false;
		if (report == null) {
			report = new OrderReportDto();
			no_data = true;
		}

		// 搜索押金扣款
		AirServiceFeeDto summary_option = new AirServiceFeeDto();
		summary_option.setProduct_manager_number(option.getProduct_manager_number());
		summary_option.setFrom_month(from_month);
		summary_option.setTo_month(to_month);
		List<AirOtherPaymentDto> deposit_deducts = airTicketPayableService.searchDepositDeducts(summary_option);
		BigDecimal air_deduct = BigDecimal.ZERO;
		if (null != deposit_deducts && deposit_deducts.size() > 0) {
			for (AirOtherPaymentDto aopd : deposit_deducts) {
				air_deduct = air_deduct.add(aopd.getMoney());
			}
		}
		report.setAir_deduct(air_deduct);
		// 搜索汇兑
		BigDecimal exchange = BigDecimal.ZERO;
		if (roles.contains(ResourcesConstants.USER_ROLE_ADMIN) && SimpletinyString.isEmpty(product_manager_number)) {
			PaymentDetailBean pd_option = new PaymentDetailBean();
			pd_option.setFrom_month(from_month);
			pd_option.setTo_month(to_month);
			exchange = paymentDetailService.selectExchangeByParam(pd_option);
		}
		report.setExchange(exchange);

		BigDecimal gross_profit = BigDecimal.ZERO;
		BigDecimal per_profit = BigDecimal.ZERO;
		if (!no_data) {
			gross_profit = report.getReceivable().subtract(report.getTail98()).subtract(report.getAir_ticket_cost())
					.subtract(report.getProduct_cost()).subtract(report.getOther_cost()).subtract(report.getFy()).subtract(report.getSys_cost())
					.subtract(report.getSale_cost()).subtract(report.getAir_deduct()).subtract(report.getExchange());
			per_profit = gross_profit.divide(BigDecimal.valueOf(report.getAdult_count() + report.getSpecial_count()), 2, RoundingMode.HALF_UP);
		}
		report.setGross_profit(gross_profit);
		report.setPer_profit(per_profit);
		return SUCCESS;
	}

	public String checkOrderReportCanBeApproved() {
		resultStr = service.checkOrderReportCanBeApproved(team_number);
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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN) && !roles.contains(ResourcesConstants.USER_ROLE_TICKET)) {
			return "noright";
		}
		resultStr = service.fillAriTicketCost(team_number, air_ticket_cost);
		return SUCCESS;
	}

	private String reconciliation_type;
	private BigDecimal money;

	public String addReconciliation() {
		TeamReportBean tr = service.selectTeamReportByTeamNumber(team_number);
		if (reconciliation_type.equals(ResourcesConstants.SIMPLETINY_PAY)) {
			tr.setOther_pay(money);
		} else {
			tr.setOther_receive(money);
		}
		resultStr = service.updateTeamReport(tr);
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

	public OrderReportDto getReport() {
		return report;
	}

	public void setReport(OrderReportDto report) {
		this.report = report;
	}

	public String getReconciliation_type() {
		return reconciliation_type;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setReconciliation_type(String reconciliation_type) {
		this.reconciliation_type = reconciliation_type;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
}