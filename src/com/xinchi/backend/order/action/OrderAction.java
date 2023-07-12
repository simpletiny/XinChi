package com.xinchi.backend.order.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.order.service.BudgetNonStandardOrderService;
import com.xinchi.backend.order.service.BudgetStandardOrderService;
import com.xinchi.backend.order.service.FinalNonStandardOrderService;
import com.xinchi.backend.order.service.FinalStandardOrderService;
import com.xinchi.backend.order.service.OrderNameListService;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.backend.order.service.OrderTicketInfoService;
import com.xinchi.backend.product.service.ProductOrderOperationService;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.ticket.service.AirTicketNameListService;
import com.xinchi.backend.ticket.service.TicketService;
import com.xinchi.bean.AirTicketChangeLogBean;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.FinalNonStandardOrderBean;
import com.xinchi.bean.FinalStandardOrderBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.PassengerTicketInfoBean;
import com.xinchi.bean.PayableOrderBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.SaleOrderTicketInfoBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderAction extends BaseAction {
	private static final long serialVersionUID = -6272167129826318077L;
	@Autowired
	private BudgetStandardOrderService bsoService;

	private BudgetStandardOrderBean bsOrder;

	private String json;

	public String createBudgetStandardOrder() {
		// 保存订单和名单
		resultStr = bsoService.createOrder(bsOrder, json);
		return SUCCESS;
	}

	@Autowired
	private BudgetNonStandardOrderService bnsoService;

	private BudgetNonStandardOrderBean bnsOrder;

	public String createBudgetNonStandardOrder() {
		resultStr = bnsoService.createOrder(bnsOrder, json);
		return SUCCESS;
	}

	/**
	 * 创建单机票订单
	 * 
	 * @return
	 */
	public String createOnlyTicketOrder() {
		resultStr = bnsoService.createOnlyTicketOrder(bnsOrder, json);
		return SUCCESS;
	}

	private OrderDto option;
	private List<OrderDto> tbcOrders;
	@Autowired
	private OrderService service;

	/**
	 * 搜索待确认订单
	 * 
	 * @return
	 */
	public String searchTbcOrdersByPage() {
		if (null == option)
			option = new OrderDto();
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			option.setCreate_user(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);

		tbcOrders = service.selectTbcByPage(page);
		return SUCCESS;
	}

	/**
	 * 搜索已确认确认订单
	 * 
	 * @return
	 */
	public String searchCOrdersByPage() {
		if (null == option)
			option = new OrderDto();
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			option.setCreate_user(sessionBean.getUser_number());
		}
		String period = option.getConfirm_period();
		String today = DateUtil.getDateStr("yyyy-MM-dd");
		if (period != null) {
			if (period.equals("today")) {
				option.setConfirm_date_from(today);
				option.setConfirm_date_to(today);
			} else if (period.equals("thisweek")) {
				option.setConfirm_date_from(DateUtil.getThisWeekFirstDay());
				option.setConfirm_date_to(DateUtil.getThisWeekLastDay());
			} else if (period.equals("lastweek")) {
				option.setConfirm_date_from(DateUtil.addDate(DateUtil.getThisWeekFirstDay(), -7));
				option.setConfirm_date_to(DateUtil.addDate(DateUtil.getThisWeekFirstDay(), -1));
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);

		tbcOrders = service.selectCByPage(page);
		return SUCCESS;
	}

	private OrderDto order;

	/**
	 * 
	 * @return
	 */
	public String searchCOrderByPk() {
		order = service.searchOrderByPk(order_pk);
		return SUCCESS;
	}

	/**
	 * 搜索已决算订单
	 * 
	 * @return
	 */
	public String searchFOrdersByPage() {
		if (null == option)
			option = new OrderDto();
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			option.setSale_number(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);

		tbcOrders = service.selectFByPage(page);
		return SUCCESS;
	}

	private String order_pk;
	private String standard_flg;

	/**
	 * 删除待确认订单
	 * 
	 * @return
	 */
	public String deleteTbcOrder() {
		if (standard_flg.equals("N")) {
			resultStr = bnsoService.delete(order_pk);
		} else if (standard_flg.equals("Y")) {
			resultStr = bsoService.delete(order_pk);
		} else {
			resultStr = "lol";
		}
		return SUCCESS;
	}

	/**
	 * 生成订单应收款
	 * 
	 * @return
	 */
	public String createReceivable() {
		resultStr = service.createReceivable(order_pk);
		return SUCCESS;
	}

	/**
	 * 打回已确认订单
	 * 
	 * @return
	 */
	public String rollBackCOrder() {
		if (standard_flg.equals("N")) {
			resultStr = bnsoService.rollBackCOrder(order_pk);
		} else if (standard_flg.equals("Y")) {
			resultStr = bsoService.rollBackCOrder(order_pk);
		} else {
			resultStr = "lol";
		}
		return SUCCESS;
	}

	/**
	 * 更新标准预算单
	 * 
	 * @return
	 */
	public String updateBudgetStandardOrder() {
		resultStr = bsoService.update(bsOrder, json);
		return SUCCESS;
	}

	public String confirmBudgetStandardOrder() {
		resultStr = bsoService.confirmStandardOrder(bsOrder, json);
		return SUCCESS;
	}

	/**
	 * 检测已确认订单是否可以修改
	 * 
	 * @return
	 */
	public String checkCanBeEdit() {
		resultStr = service.checkCanBeEdit(order_pk);
		return SUCCESS;
	}

	/**
	 * 更新已确认标准预算订单
	 * 
	 * @return
	 */
	public String updateConfirmedStandardOrder() {
		resultStr = bsoService.updateConfirmedStandardOrder(bsOrder, json);
		return SUCCESS;
	}

	private FinalStandardOrderBean fsOrder;

	@Autowired
	private FinalStandardOrderService finalStandardOrderService;

	@Autowired
	private ReceivableService receivableService;

	/**
	 * 决算标准订单
	 * 
	 * @return
	 */
	public String finalBudgetStandardOrder() {
		bsOrder = bsoService.selectByPrimaryKey(fsOrder.getPk());
		bsOrder.setConfirm_flg("F");
		bsoService.updateComment(bsOrder);

		fsOrder.setPk(null);
		fsOrder.setTeam_number(bsOrder.getTeam_number());
		fsOrder.setAir_ticket_cost(bsOrder.getAir_ticket_cost());
		fsOrder.setProduct_cost(bsOrder.getProduct_cost());
		fsOrder.setOperate_flg(bsOrder.getOperate_flg());
		finalStandardOrderService.insert(fsOrder);

		// 更新应收款
		ReceivableBean receivable = receivableService.selectByTeamNumber(bsOrder.getTeam_number());
		receivable.setFinal_flg("Y");
		receivable.setFinal_receivable(fsOrder.getReceivable());
		receivable.setFinal_balance(receivable.getFinal_receivable().subtract(receivable.getReceived()));

		receivableService.update(receivable);

		resultStr = SUCCESS;
		return SUCCESS;
	}

	/**
	 * 更新订单备注
	 * 
	 * @return
	 */
	public String updateComment() {
		if (standard_flg.equals("N")) {
			resultStr = bnsoService.updateComment(bnsOrder);
		} else if (standard_flg.equals("Y")) {
			resultStr = bsoService.updateComment(bsOrder);
		} else {
			resultStr = "lol";
		}
		return SUCCESS;
	}

	/**
	 * 查询标准预算单
	 * 
	 * @return
	 */
	public String searchTbcBsOrderByPk() {
		bsOrder = bsoService.selectByPrimaryKey(order_pk);
		passengers = orderNameListService.selectByOrderPk(order_pk);
		return SUCCESS;
	}

	/**
	 * 更新非标准预算单
	 * 
	 * @return
	 */
	public String updateBudgetNonStandardOrder() {
		resultStr = bnsoService.update(bnsOrder, json);
		return SUCCESS;
	}

	public String confirmBudgetNonStandardOrder() {
		resultStr = bnsoService.confirm(bnsOrder, json);
		return SUCCESS;
	}

	/**
	 * 更新单机票订单
	 * 
	 * @return
	 */
	public String updateOnlyTicketOrder() {
		resultStr = bnsoService.updateOnlyTicketOrder(bnsOrder, json);
		return SUCCESS;
	}

	/**
	 * 确认单机票订单
	 * 
	 * @return
	 */
	public String confirmOnlyTicketOrder() {
		resultStr = bnsoService.confirmOnlyTicketOrder(bnsOrder, json);
		return SUCCESS;
	}

	public String updateConfirmedOnlyTicketOrder() {
		resultStr = bnsoService.updateConfirmedOnlyTicketOrder(bnsOrder, json);
		return SUCCESS;
	}

	public String updateConfirmedNonStandardOrder() {
		resultStr = bnsoService.updateConfirmedNonStandardOrder(bnsOrder, json);
		return SUCCESS;
	}

	private FinalNonStandardOrderBean fnsOrder;

	@Autowired
	private FinalNonStandardOrderService finalNonStandardOrderService;

	/**
	 * 决算非标准订单
	 * 
	 * @return
	 */
	public String finalBudgetNonStandardOrder() {
		bnsOrder = bnsoService.selectByPrimaryKey(fnsOrder.getPk());
		bnsOrder.setConfirm_flg("F");
		bnsoService.updateComment(bnsOrder);

		fnsOrder.setPk(null);
		fnsOrder.setProduct_manager(bnsOrder.getProduct_manager());
		fnsOrder.setTeam_number(bnsOrder.getTeam_number());
		fnsOrder.setAir_ticket_cost(bnsOrder.getAir_ticket_cost());
		fnsOrder.setProduct_cost(bnsOrder.getProduct_cost());
		fnsOrder.setOperate_flg(bnsOrder.getOperate_flg());
		finalNonStandardOrderService.insert(fnsOrder);

		// 更新应收款
		ReceivableBean receivable = receivableService.selectByTeamNumber(bnsOrder.getTeam_number());
		receivable.setFinal_flg("Y");
		receivable.setFinal_receivable(fnsOrder.getReceivable());
		receivable.setFinal_balance(receivable.getFinal_receivable().subtract(receivable.getReceived()));

		receivableService.update(receivable);

		resultStr = SUCCESS;
		return SUCCESS;
	}

	/**
	 * 决算订单
	 * 
	 * @return
	 */
	public String finalOrder() {
		resultStr = service.finalOrder(order);
		return SUCCESS;
	}

	/**
	 * 取消已确认订单
	 * 
	 * @return
	 */
	public String cancelCOrder() {
		resultStr = service.cancelOrder(order);
		return SUCCESS;
	}

	/**
	 * 决算订单打回重报
	 * 
	 * @return
	 */
	public String rollBackFinalOrder() {
		resultStr = service.rollBackFinalOrder(order_pk, standard_flg);
		return SUCCESS;
	}

	private List<SaleOrderTicketInfoBean> ticketInfos;

	@Autowired
	private OrderTicketInfoService orderTicketInfoService;

	/**
	 * 查询非标准预算单
	 * 
	 * @return
	 */
	public String searchTbcBnsOrderByPk() {
		bnsOrder = bnsoService.selectByPrimaryKey(order_pk);
		passengers = orderNameListService.selectByOrderPk(order_pk);

		if (bnsOrder.getIndependent_flg().equals("A")) {
			ticketInfos = orderTicketInfoService.selectByOrderPk(order_pk);
		}
		return SUCCESS;
	}

	private List<SaleOrderNameListBean> passengers;
	private String team_number;

	@Autowired
	private OrderNameListService orderNameListService;

	public String selectSaleOrderNameListByTeamNumber() {
		passengers = orderNameListService.selectByTeamNumber(team_number);
		return SUCCESS;
	}

	private String team_numbers;

	public String selectSaleOrderNameListByTeamNumbers() {
		List<String> t_ns = new ArrayList<String>();
		t_ns.addAll(Arrays.asList(team_numbers.split(",")));
		passengers = orderNameListService.selectByTeamNumbers(t_ns);
		return SUCCESS;
	}

	private String need_pk;

	public String selectSaleOrderNameListByAirNeedPk() {
		passengers = orderNameListService.selectByAirNeedPk(need_pk);
		return SUCCESS;
	}

	public String selectOrderByTeamNumber() {
		option = service.selectByTeamNumber(team_number);
		return SUCCESS;
	}

	private OrderDto final_order;
	@Autowired
	private AirTicketNameListService airTicketNameListService;

	private List<AirTicketNameListBean> name_info;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private ProductOrderOperationService productOrderOperationService;

	private List<PayableOrderBean> payable_orders;

	public String selectOrderInfoByTeamNumber() {
		// 预算信息
		option = service.selectByTeamNumber(team_number);

		// 决算信息
		final_order = service.selectFinalOrderByTeamNumber(team_number);

		if (null != final_order) {
			String final_comment = "";
			if (final_order.getFinal_type().equals(ResourcesConstants.FINAL_TYPE_NO_CHANGE)) {
				final_comment = "无变化";
			} else if (final_order.getFinal_type().equals(ResourcesConstants.FINAL_TYPE_CHANGE)) {
				if (final_order.getRaise_money().compareTo(BigDecimal.ZERO) == 0) {
					final_comment = "费用减少：" + final_order.getReduce_money() + "；备注：" + final_order.getReduce_comment();
				} else {
					final_comment = "费用增加：" + final_order.getRaise_money() + "；备注：" + final_order.getRaise_comment();
				}
			} else if (final_order.getFinal_type().equals(ResourcesConstants.FINAL_TYPE_COMPLAIN)) {
				final_comment = "有投诉，费用：" + final_order.getComplain_money() + "；投诉原因："
						+ final_order.getComplain_reason() + "；解决方案：" + final_order.getComplain_solution();
			} else {
				final_comment = "订单取消";
			}

			final_order.setFinal_comment(final_comment);
		}
		// 机票款信息
		List<String> t_ns = new ArrayList<>();
		t_ns.add(team_number);

		List<AirTicketNameListBean> names = airTicketNameListService.selectWithInfoByTeamNumbers(t_ns);
		if (null != names && names.size() > 0) {

			name_info = new ArrayList<>();
			for (AirTicketNameListBean name : names) {
				String detail_comment = "";
				if (!name.getStatus().equals("I")) {
					PassengerTicketInfoBean pti = name.getTicket_infos().get(0);
					name.setBudget_cost(pti.getTicket_cost());
					if (name.getStatus().equals("C")) {
						name.setFinal_cost(pti.getTicket_cost().add(name.getChange_cost()));
						AirTicketChangeLogBean atcl = ticketService.searchFlightChangeLogByPk(name.getChange_pk());
						detail_comment = "航变原因：" + atcl.getChange_reason() + "；航变费用：" + name.getChange_cost() + "；备注："
								+ atcl.getComment();
					} else {
						name.setFinal_cost(pti.getTicket_cost());
						detail_comment = "正常出票。";
					}
				} else {
					name.setBudget_cost(BigDecimal.ZERO);
					name.setFinal_cost(BigDecimal.ZERO);
					detail_comment = "未出票。";
				}
				name.setDetail_comment(detail_comment);
				name_info.add(name);
			}
		}
		// 地接款信息
		payable_orders = productOrderOperationService.selectPayableOrderByTeamNumber(team_number);
		// if (option.getIndependent_flg().equals("A")) {
		// ticketInfos = orderTicketInfoService.selectByOrderPk(option.getPk());
		// }

		return SUCCESS;
	}

	private String related_pk;

	public String searchOrderByReceivedRelatedPk() {
		orders = service.searchOrderByReceivedRelatedPk(related_pk);
		return SUCCESS;
	}

	private List<OrderDto> orders;

	public String searchConfirmingOrders() {
		orders = service.selectConfirmingOrders();
		return SUCCESS;
	}

	public String confirmNameList() {
		resultStr = service.confirmNameList(team_number);
		return SUCCESS;
	}

	public BudgetStandardOrderBean getBsOrder() {
		return bsOrder;
	}

	public void setBsOrder(BudgetStandardOrderBean bsOrder) {
		this.bsOrder = bsOrder;
	}

	public BudgetNonStandardOrderBean getBnsOrder() {
		return bnsOrder;
	}

	public void setBnsOrder(BudgetNonStandardOrderBean bnsOrder) {
		this.bnsOrder = bnsOrder;
	}

	public List<OrderDto> getTbcOrders() {
		return tbcOrders;
	}

	public void setTbcOrders(List<OrderDto> tbcOrders) {
		this.tbcOrders = tbcOrders;
	}

	public OrderDto getOption() {
		return option;
	}

	public void setOption(OrderDto option) {
		this.option = option;
	}

	public String getOrder_pk() {
		return order_pk;
	}

	public void setOrder_pk(String order_pk) {
		this.order_pk = order_pk;
	}

	public String getStandard_flg() {
		return standard_flg;
	}

	public void setStandard_flg(String standard_flg) {
		this.standard_flg = standard_flg;
	}

	public List<SaleOrderNameListBean> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<SaleOrderNameListBean> passengers) {
		this.passengers = passengers;
	}

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public FinalStandardOrderBean getFsOrder() {
		return fsOrder;
	}

	public void setFsOrder(FinalStandardOrderBean fsOrder) {
		this.fsOrder = fsOrder;
	}

	public FinalNonStandardOrderBean getFnsOrder() {
		return fnsOrder;
	}

	public void setFnsOrder(FinalNonStandardOrderBean fnsOrder) {
		this.fnsOrder = fnsOrder;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public OrderDto getOrder() {
		return order;
	}

	public void setOrder(OrderDto order) {
		this.order = order;
	}

	public List<OrderDto> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDto> orders) {
		this.orders = orders;
	}

	public String getNeed_pk() {
		return need_pk;
	}

	public void setNeed_pk(String need_pk) {
		this.need_pk = need_pk;
	}

	public String getTeam_numbers() {
		return team_numbers;
	}

	public void setTeam_numbers(String team_numbers) {
		this.team_numbers = team_numbers;
	}

	public List<SaleOrderTicketInfoBean> getTicketInfos() {
		return ticketInfos;
	}

	public void setTicketInfos(List<SaleOrderTicketInfoBean> ticketInfos) {
		this.ticketInfos = ticketInfos;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public OrderDto getFinal_order() {
		return final_order;
	}

	public void setFinal_order(OrderDto final_order) {
		this.final_order = final_order;
	}

	public List<AirTicketNameListBean> getName_info() {
		return name_info;
	}

	public void setName_info(List<AirTicketNameListBean> name_info) {
		this.name_info = name_info;
	}

	public List<PayableOrderBean> getPayable_orders() {
		return payable_orders;
	}

	public void setPayable_orders(List<PayableOrderBean> payable_orders) {
		this.payable_orders = payable_orders;
	}
}