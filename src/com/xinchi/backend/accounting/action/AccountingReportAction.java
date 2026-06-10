package com.xinchi.backend.accounting.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.ReimbursementService;
import com.xinchi.backend.order.service.OrderReportService;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.backend.payable.service.AirTicketPayableService;
import com.xinchi.bean.AirOtherPaymentDto;
import com.xinchi.bean.AirServiceFeeDto;
import com.xinchi.bean.OrderReportDto;
import com.xinchi.bean.ProductReconciliationBean;
import com.xinchi.bean.ReimbursementBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountingReportAction extends BaseAction {
	private static final long serialVersionUID = -7178141267315060641L;

	private String date_from;
	private String date_to;
	private List<String> statuses;

	/* 损益表 start */
	public String searchPLSummary() {

		return SUCCESS;
	}

	// 主营
	@Autowired
	private OrderService orderService;

	private OrderReportDto main_report;
	@Autowired
	private OrderReportService orderReportService;

	public String searchPLMain() {
		OrderReportDto main_option = new OrderReportDto();
		if (!SimpletinyString.isEmpty(date_from)) {
			main_option.setDate_from(date_from + "-00");
		}
		if (!SimpletinyString.isEmpty(date_to)) {
			main_option.setDate_to(date_to + "-31");
		}
		main_option.setReport_statuses(statuses);
		main_report = orderReportService.searchSumReport(main_option);
		if (main_report == null) {
			main_report = new OrderReportDto();
		}

		// 搜索订单数
		int order_count = orderService.selectOrderCountByParam(main_option);
		main_report.setOrder_count(order_count);
		return SUCCESS;
	}

	// 票务
	@Autowired
	private AirTicketPayableService airTicketPayableService;

	private Map<String, BigDecimal> air_report;

	public String searchPLTicket() {
		air_report = new HashMap<>();
		// 搜索押金扣款
		AirServiceFeeDto summary_option = new AirServiceFeeDto();
		summary_option.setFrom_month(date_from);
		summary_option.setTo_month(date_to);
		List<AirOtherPaymentDto> deposit_deducts = airTicketPayableService.searchDepositDeducts(summary_option);
		BigDecimal air_deduct = BigDecimal.ZERO;
		if (null != deposit_deducts && deposit_deducts.size() > 0) {
			for (AirOtherPaymentDto aopd : deposit_deducts) {
				air_deduct = air_deduct.add(aopd.getMoney());
			}
		}
		air_report.put("deduct", air_deduct);
		AirServiceFeeDto fee_option = new AirServiceFeeDto();
		// 机票其他费用
		fee_option.setFrom_month(date_from);
		fee_option.setTo_month(date_to);
		List<AirOtherPaymentDto> no_b_payments = airTicketPayableService.searchNoneBussinessPayment(fee_option);
		BigDecimal no_b_payment = BigDecimal.ZERO;

		for (AirOtherPaymentDto nbp : no_b_payments) {
			no_b_payment = no_b_payment.add(nbp.getMoney());
		}
		air_report.put("payment", no_b_payment);
		// 票务费用申请
		ReimbursementBean re_option = new ReimbursementBean();
		re_option.setDate_from(date_from);
		re_option.setDate_to(date_to);
		re_option.setItem("A");
		List<ReimbursementBean> reis = reimbursementService.selectSumByParam(re_option);
		BigDecimal air_reimbursement = BigDecimal.ZERO;

		if (null != reis && reis.size() > 0) {
			air_reimbursement = reis.get(0).getMoney();
		}
		air_report.put("reimbursement", air_reimbursement);
		return SUCCESS;
	}

	public String searchPLHuman() {

		return SUCCESS;
	}

	@Autowired
	private ReimbursementService reimbursementService;
	private Map<String, BigDecimal> reibursements;
	private List<String> reimbursement_items;

	public String searchPLReimbursement() {
		ReimbursementBean option = new ReimbursementBean();
		option.setDate_from(date_from);
		option.setDate_to(date_to);
		option.setItems(reimbursement_items);
		reibursements = reimbursementService.searchSummaries(option);
		return SUCCESS;
	}

	public String searchPLOther() {

		return SUCCESS;
	}

	public String searchPLInner() {

		return SUCCESS;
	}
	/* 损益表 end */

	public Map<String, BigDecimal> getReibursements() {
		return reibursements;
	}

	public void setReibursements(Map<String, BigDecimal> reibursements) {
		this.reibursements = reibursements;
	}

	public String getDate_from() {
		return date_from;
	}

	public void setDate_from(String date_from) {
		this.date_from = date_from;
	}

	public String getDate_to() {
		return date_to;
	}

	public void setDate_to(String date_to) {
		this.date_to = date_to;
	}

	public List<String> getReimbursement_items() {
		return reimbursement_items;
	}

	public void setReimbursement_items(List<String> reimbursement_items) {
		this.reimbursement_items = reimbursement_items;
	}

	public Map<String, BigDecimal> getAir_report() {
		return air_report;
	}

	public void setAir_report(Map<String, BigDecimal> air_report) {
		this.air_report = air_report;
	}

	public OrderReportDto getMain_report() {
		return main_report;
	}

	public void setMain_report(OrderReportDto main_report) {
		this.main_report = main_report;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}
}
