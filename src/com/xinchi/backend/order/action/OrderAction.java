package com.xinchi.backend.order.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
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
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.FinalNonStandardOrderBean;
import com.xinchi.bean.FinalStandardOrderBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.ToolsUtil;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderAction extends BaseAction {
	private static final long serialVersionUID = -6272167129826318077L;
	@Autowired
	private BudgetStandardOrderService bsoService;

	private BudgetStandardOrderBean bsOrder;

	@Autowired
	private OrderNameListService orderNameService;

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
		resultStr = bnsoService.insert(bnsOrder, json);
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
			option.setCreate_user(sessionBean.getUser_number());
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

	public String updateConfirmedNonStandardOrder() {
		resultStr = bnsoService.updateConfirmedNonStandardOrder(bnsOrder);
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
	 * 决算订单打回重报
	 * 
	 * @return
	 */
	public String rollBackFinalOrder() {
		if (standard_flg.equals("N")) {
			fnsOrder = finalNonStandardOrderService.selectByPrimaryKey(order_pk);

			// 搜索预算订单,更新预算订单状态
			bnsOrder = bnsoService.selectByTeamNumber(fnsOrder.getTeam_number());
			bnsOrder.setConfirm_flg("Y");
			bnsoService.updateComment(bnsOrder);

			ReceivableBean receivable = receivableService.selectByTeamNumber(fnsOrder.getTeam_number());
			receivable.setFinal_flg("N");
			receivable.setFinal_receivable(BigDecimal.ZERO);
			receivable.setFinal_balance(BigDecimal.ZERO);
			receivableService.update(receivable);

			finalNonStandardOrderService.delete(order_pk);

		} else if (standard_flg.equals("Y")) {
			fsOrder = finalStandardOrderService.selectByPrimaryKey(order_pk);

			// 更新预算订单状态
			bsOrder = bsoService.selectByTeamNumber(fsOrder.getTeam_number());
			bsOrder.setConfirm_flg("Y");
			bsoService.updateComment(bsOrder);

			ReceivableBean receivable = receivableService.selectByTeamNumber(fsOrder.getTeam_number());
			receivable.setFinal_flg("N");
			receivable.setFinal_receivable(BigDecimal.ZERO);
			receivable.setFinal_balance(BigDecimal.ZERO);
			receivableService.update(receivable);

			finalStandardOrderService.delete(order_pk);
		}

		resultStr = SUCCESS;
		return SUCCESS;
	}

	/**
	 * 查询非标准预算单
	 * 
	 * @return
	 */
	public String searchTbcBnsOrderByPk() {
		bnsOrder = bnsoService.selectByPrimaryKey(order_pk);
		passengers = orderNameListService.selectByOrderPk(order_pk);
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

	public String selectOrderByTeamNumber() {
		option = service.selectByTeamNumber(team_number);
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
}