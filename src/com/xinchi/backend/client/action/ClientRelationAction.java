package com.xinchi.backend.client.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.AccurateSaleService;
import com.xinchi.backend.client.service.ClientRelationService;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.bean.AccurateSaleBean;
import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.SaleScoreDto;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope("prototype")
public class ClientRelationAction extends BaseAction {
	private static final long serialVersionUID = -5702112495549468432L;

	private ClientVisitBean visit;

	private ClientRelationSummaryBean relation;
	private List<ClientRelationSummaryBean> relations;
	@Autowired
	private ClientRelationService service;

	public String createVisit() {
		resultStr = service.createVisit(visit);
		return SUCCESS;
	}

	public String searchRelationsByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			relation.setSales(sessionBean.getPk());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", relation);
		page.setParams(params);
		relations = service.getRelationsByPage(page);
		return SUCCESS;
	}

	private String employeeCount;
	private String monthOrderCount;
	private List<ClientSummaryDto> clientSummary;
	@Autowired
	private OrderService orderService;

	@Autowired
	private AccurateSaleService accurateSaleService;

	private BigDecimal sale_score;

	private int today_point;

	public String searchClientSummary() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		SaleScoreDto ssd = new SaleScoreDto();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			relation.setSales_name(sessionBean.getUser_name());
		}
		ssd.setSale_number(sessionBean.getUser_number());
		ssd.setConfirm_month(DateUtil.getDateStr("yyyy-MM"));

		List<SaleScoreDto> scores = orderService.searchSaleScoreByParam(ssd);
		if (null != scores && scores.size() > 0) {
			sale_score = scores.get(0).getScore();
		}
		// 计算当日勤点
		today_point = -10;
		String today = DateUtil.today();
		// 搜索当日确认订单
		OrderDto orderOption = new OrderDto();
		orderOption.setCreate_user_number(sessionBean.getUser_number());
		orderOption.setConfirm_date(today);
		List<OrderDto> orders = orderService.selectByParam(orderOption);
		if (null != orders && orders.size() > 0) {
			today_point += orders.size() * 3;
		}
		// 搜索当日有效拜访
		ClientVisitBean visitOption = new ClientVisitBean();
		visitOption.setCreate_user(sessionBean.getUser_number());
		visitOption.setDate(today);
		visitOption.setType("VISIT");
		List<ClientVisitBean> visits = service.selectVisitByParam(visitOption);

		if (null != visits && visits.size() > 0) {
			today_point += visits.size() * 2;
		}

		// 搜索当日有效沟通（精推）
		AccurateSaleBean asOption = new AccurateSaleBean();
		asOption.setDate(today);
		asOption.setCreate_user(sessionBean.getUser_number());
		List<AccurateSaleBean> ases = accurateSaleService.getAllByParam(asOption);
		if (null != ases && ases.size() > 0) {
			today_point += ases.size();
		}

		clientSummary = service.getClientSummary(relation);
		employeeCount = service.selectClientEmployeeCount(relation);
		monthOrderCount = service.selectMonthOrderCount(relation);
		return SUCCESS;
	}

	private List<ClientVisitBean> visits;

	// 搜索拜访记录
	public String searchVisitByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", visit);
		page.setParams(params);

		visits = service.selectVisitByPage(page);
		return SUCCESS;
	}

	public ClientVisitBean getVisit() {
		return visit;
	}

	public void setVisit(ClientVisitBean visit) {
		this.visit = visit;
	}

	public ClientRelationSummaryBean getRelation() {
		return relation;
	}

	public void setRelation(ClientRelationSummaryBean relation) {
		this.relation = relation;
	}

	public List<ClientRelationSummaryBean> getRelations() {
		return relations;
	}

	public void setRelations(List<ClientRelationSummaryBean> relations) {
		this.relations = relations;
	}

	public List<ClientSummaryDto> getClientSummary() {
		return clientSummary;
	}

	public void setClientSummary(List<ClientSummaryDto> clientSummary) {
		this.clientSummary = clientSummary;
	}

	public String getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(String employeeCount) {
		this.employeeCount = employeeCount;
	}

	public String getMonthOrderCount() {
		return monthOrderCount;
	}

	public void setMonthOrderCount(String monthOrderCount) {
		this.monthOrderCount = monthOrderCount;
	}

	public List<ClientVisitBean> getVisits() {
		return visits;
	}

	public void setVisits(List<ClientVisitBean> visits) {
		this.visits = visits;
	}

	public BigDecimal getSale_score() {
		return sale_score;
	}

	public void setSale_score(BigDecimal sale_score) {
		this.sale_score = sale_score;
	}

	public int getToday_point() {
		return today_point;
	}

	public void setToday_point(int today_point) {
		this.today_point = today_point;
	}
}