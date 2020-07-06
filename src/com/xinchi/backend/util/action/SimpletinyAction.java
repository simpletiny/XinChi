package com.xinchi.backend.util.action;

import static com.xinchi.common.SimpletinyString.isEmpty;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.dao.ClientRelationDAO;
import com.xinchi.backend.client.dao.ClientUserDAO;
import com.xinchi.backend.client.dao.ClientVisitDAO;
import com.xinchi.backend.client.dao.IncomingCallDAO;
import com.xinchi.backend.client.dao.MobileTouchDAO;
import com.xinchi.backend.client.service.ClientEmployeeUserService;
import com.xinchi.backend.client.service.ClientService;
import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.service.BudgetNonStandardOrderService;
import com.xinchi.backend.order.service.BudgetStandardOrderService;
import com.xinchi.backend.order.service.OrderNameListService;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.backend.payable.dao.PayableDAO;
import com.xinchi.backend.payable.service.PayableService;
import com.xinchi.backend.product.service.ProductAirTicketService;
import com.xinchi.backend.product.service.ProductService;
import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.backend.sale.service.FinalOrderService;
import com.xinchi.backend.sale.service.SaleOrderService;
import com.xinchi.backend.ticket.service.AirTicketNeedService;
import com.xinchi.backend.ticket.service.AirTicketOrderService;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.backend.user.service.UserService;
import com.xinchi.backend.util.service.SimpletinyService;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.BudgetOrderSupplierBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.ClientBean;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientEmployeeUserBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.ClientRelationBean;
import com.xinchi.bean.ClientUserBean;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.FinalOrderSupplierBean;
import com.xinchi.bean.IncomingCallBean;
import com.xinchi.bean.MobileTouchBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.ProductAirTicketBean;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.TempBean;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.solr.service.SimpletinySolr;
import com.xinchi.tools.PropertiesUtil;

@Controller
@Scope("prototype")
public class SimpletinyAction extends BaseAction {

	private static final long serialVersionUID = 5764501280171459444L;

	@Autowired
	private UserService userService;

	public String changeAllPasswordToMD5() {
		List<UserBaseBean> users = new ArrayList<UserBaseBean>();
		users = userService.getAllByParam(null);
		for (UserBaseBean user : users) {
			String password = SimpletinyString.MD5(user.getPassword());
			user.setPassword(password);

			userService.update(user);
		}

		return SUCCESS;
	}

	@Autowired
	private SaleOrderService saleOrderService;

	@Autowired
	private FinalOrderService finalOrderService;

	@Autowired
	private ReceivableService receivableService;

	@Autowired
	private UserDAO userDao;

	@Autowired
	private SimpletinySolr solrService;

	public String autoGenReceivable() {
		SolrClient solr = solrService.getSolr(PropertiesUtil.getProperty("solr.receivableUrl"));
		List<BudgetOrderBean> budgets = saleOrderService.searchOrders(null);
		if (null != budgets) {
			for (BudgetOrderBean budget : budgets) {

				ReceivableBean receivable = new ReceivableBean();
				receivable.setTeam_number(budget.getTeam_number());
				receivable.setClient_employee_name(budget.getClient_employee_name());
				receivable.setClient_employee_pk(budget.getClient_employee_pk());
				receivable.setDeparture_date(budget.getDeparture_date());
				receivable.setReturn_date(budget.getReturn_date());
				receivable.setProduct(budget.getProduct());
				receivable.setPeople_count(budget.getPeople_count());
				receivable.setBudget_receivable(budget.getReceivable());
				receivable.setSales(budget.getCreate_user());

				UserBaseBean userBase = userDao.getByUserNumber(budget.getCreate_user());

				receivable.setSales_name(userBase.getUser_name());
				receivable.setReceived(BigDecimal.ZERO);

				FinalOrderBean finalOrder = finalOrderService.getFinalOrderByTeamNo(budget.getTeam_number());

				if (null != finalOrder) {
					receivable.setFinal_flg("Y");
					receivable.setFinal_receivable(finalOrder.getReceivable());
				}
				receivable.setBudget_balance(receivable.getBudget_receivable());

				if (null != receivable.getFinal_flg() && receivable.getFinal_flg().equals("Y")) {
					receivable.setFinal_balance(receivable.getFinal_receivable());
				}

				receivableService.insert(receivable);

				SolrInputDocument document = castR2D(receivable);

				try {
					solr.add(document);
					solr.commit();
				} catch (SolrServerException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return SUCCESS;
	}

	private List<ReceivableBean> receivables;

	@Autowired
	private ReceivableDAO receivableDao;

	public String autoGenReceivable2th() {
		SolrClient solr = solrService.getSolr(PropertiesUtil.getProperty("solr.receivableUrl"));

		receivables = receivableDao.selectAllReceivablesWithFinancial();
		for (ReceivableBean receivable : receivables) {
			SolrInputDocument document = castR2D(receivable);
			try {
				solr.add(document);
				solr.commit();
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}

	@Autowired
	private PayableService payableService;

	public String autoGenPayable() {
		SolrClient solr = solrService.getSolr(PropertiesUtil.getProperty("solr.payableUrl"));

		List<BudgetOrderSupplierBean> budgets = saleOrderService.searchBudgetSupplierByParam(null);

		if (null != budgets) {
			for (BudgetOrderSupplierBean budget : budgets) {

				BudgetOrderBean bob = saleOrderService.searchBudgetOrderByTeamNumber(budget.getTeam_number());

				PayableBean payable = new PayableBean();
				payable.setTeam_number(budget.getTeam_number());
				payable.setSupplier_employee_name(budget.getSupplier_employee_name());
				payable.setSupplier_employee_pk(budget.getSupplier_employee_pk());

				payable.setDeparture_date(bob.getDeparture_date());
				payable.setReturn_date(bob.getReturn_date());

				payable.setProduct(bob.getProduct());
				payable.setPeople_count(bob.getPeople_count());
				payable.setBudget_payable(budget.getPayable());
				payable.setSales(bob.getCreate_user());

				UserBaseBean userBase = userDao.getByUserNumber(bob.getCreate_user());

				payable.setSales_name(userBase.getUser_name());
				payable.setPaid(BigDecimal.ZERO);
				payable.setBudget_balance(budget.getPayable());

				FinalOrderBean finalOrder = finalOrderService.getFinalOrderByTeamNo(budget.getTeam_number());

				if (null != finalOrder) {
					payable.setFinal_flg("Y");

					FinalOrderSupplierBean fosb = new FinalOrderSupplierBean();
					fosb.setTeam_number(budget.getTeam_number());
					fosb.setSupplier_employee_pk(budget.getSupplier_employee_pk());

					List<FinalOrderSupplierBean> fosbs = finalOrderService.searchFinalSupplierByParam(fosb);
					fosb = fosbs.get(0);

					payable.setFinal_payable(fosb.getPayable());
					payable.setFinal_balance(fosb.getPayable());
				}

				payableService.insert(payable);

				SolrInputDocument document = castP2D(payable);

				try {
					solr.add(document);
					solr.commit();
				} catch (SolrServerException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return SUCCESS;
	}

	@Autowired
	private PayableDAO dao;
	private List<PayableBean> payables;

	// 第二次生成应付款solr逻辑
	public String autoGenPayable2th() {
		SolrClient solr = solrService.getSolr(PropertiesUtil.getProperty("solr.payableUrl"));
		payables = dao.selectAllPayableWithSupplier();
		for (PayableBean payable : payables) {
			SolrInputDocument document = castP2D(payable);
			try {
				solr.add(document);
				solr.commit();
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}

	@Autowired
	private BudgetStandardOrderService budgetStandardOrderService;
	@Autowired
	private OrderNameListService orderNameListService;

	public String fixPassenger() {
		List<BudgetStandardOrderBean> orders = budgetStandardOrderService.selectByParam(null);
		for (BudgetStandardOrderBean order : orders) {
			if (isEmpty(order.getTeam_number()))
				continue;
			List<SaleOrderNameListBean> names = orderNameListService.selectByTeamNumber(order.getTeam_number());
			String passenger_captain = "";
			for (SaleOrderNameListBean name : names) {
				if (name.getChairman().equals("Y")) {
					passenger_captain = name.getName();
					if (passenger_captain.length() > 10) {
						passenger_captain = passenger_captain.substring(0, 5);
					}
					break;
				}
			}
			order.setPassenger_captain(passenger_captain);

			budgetStandardOrderService.updateComment(order);

		}
		return SUCCESS;
	}

	@Autowired
	private SimpletinyService service;

	private String account_name;

	// 自动修正流水账余额
	public String autoFixBalance() {
		service.autoFixBalance(account_name);
		return SUCCESS;
	}

	private SolrInputDocument castP2D(PayableBean payable) {
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", payable.getPk());
		document.addField("team_number", payable.getTeam_number());
		document.addField("final_flg", payable.getFinal_flg());
		document.addField("supplier_employee_name", payable.getSupplier_employee_name());
		document.addField("supplier_employee_pk", payable.getSupplier_employee_pk());
		document.addField("supplier_name", payable.getSupplier_name());
		document.addField("supplier_pk", payable.getSupplier_pk());
		document.addField("departure_date", DateUtil.castStr2Date(payable.getDeparture_date()));
		document.addField("return_date", DateUtil.castStr2Date(payable.getReturn_date()));
		document.addField("product", payable.getProduct());
		document.addField("people_count", payable.getPeople_count());
		document.addField("budget_payable",
				(null == payable.getBudget_payable() ? 0 : payable.getBudget_payable().doubleValue()));

		document.addField("final_payable",
				(null == payable.getFinal_payable() ? 0 : payable.getFinal_payable().doubleValue()));

		document.addField("paid", (null == payable.getPaid() ? 0 : payable.getPaid().doubleValue()));

		document.addField("budget_balance",
				(null == payable.getBudget_balance() ? 0 : payable.getBudget_balance().doubleValue()));

		document.addField("final_balance",
				(null == payable.getFinal_balance() ? 0 : payable.getFinal_balance().doubleValue()));

		document.addField("sales", payable.getSales());
		document.addField("sales_name", payable.getSales_name());
		return document;
	}

	private SolrInputDocument castR2D(ReceivableBean receivable) {
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", receivable.getPk());
		document.addField("team_number", receivable.getTeam_number());
		document.addField("final_flg", receivable.getFinal_flg());
		document.addField("client_employee_name", receivable.getClient_employee_name());
		document.addField("client_employee_pk", receivable.getClient_employee_pk());

		document.addField("departure_date", DateUtil.castStr2Date(receivable.getDeparture_date()));
		document.addField("return_date", DateUtil.castStr2Date(receivable.getReturn_date()));
		document.addField("product", receivable.getProduct());
		document.addField("people_count", receivable.getPeople_count());
		document.addField("budget_receivable",
				(null == receivable.getBudget_receivable() ? 0 : receivable.getBudget_receivable().doubleValue()));

		document.addField("final_receivable",
				(null == receivable.getFinal_receivable() ? 0 : receivable.getFinal_receivable().doubleValue()));

		document.addField("received", (null == receivable.getReceived() ? 0 : receivable.getReceived().doubleValue()));

		document.addField("budget_balance",
				(null == receivable.getBudget_balance() ? 0 : receivable.getBudget_balance().doubleValue()));

		document.addField("final_balance",
				(null == receivable.getFinal_balance() ? 0 : receivable.getFinal_balance().doubleValue()));
		document.addField("financial_body_name", receivable.getFinancial_body_name());
		document.addField("financial_body_pk", receivable.getFinancial_body_pk());
		document.addField("sales", receivable.getSales());
		document.addField("sales_name", receivable.getSales_name());

		return document;
	}

	@Autowired
	private AirTicketNeedService needService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductAirTicketService productAirTicketService;

	@Autowired
	private AirTicketOrderService airTicketOrderService;
	@Autowired
	private BudgetStandardOrderService bsoService;

	@Autowired
	private BudgetNonStandardOrderService bnsoService;

	@Autowired
	private OrderNameListService orderNamelistService;

	public String autoGenTicketOrder() {

		Map<String, Object> params = new HashMap<String, Object>();
		AirTicketNeedBean x = new AirTicketNeedBean();
		params.put("bo", x);

		page.setParams(params);
		page.setStart(0);
		page.setCount(1000);
		List<AirTicketNeedBean> list = needService.selectOrderByPage(page);
		for (AirTicketNeedBean need : list) {
			String names = "";
			if (need.getConfirm_flg().equals("Y")) {
				if (need.getStandard_flg().equals("Y")) {
					BudgetStandardOrderBean bsOrder = bsoService.selectByPrimaryKey(need.getSale_order_pk());
					names = bsOrder.getName_list();
				} else {
					BudgetNonStandardOrderBean bnsOrder = bnsoService.selectByPrimaryKey(need.getSale_order_pk());
					names = bnsOrder.getName_list();
				}
			}
			if (names == null)
				continue;
			String arrName[] = names.split(";");
			List<SaleOrderNameListBean> nnnn = new ArrayList<SaleOrderNameListBean>();
			for (String name : arrName) {
				String[] info = name.split(":");
				if (info.length < 2)
					continue;
				SaleOrderNameListBean y = new SaleOrderNameListBean();

				y.setName(info[0]);
				y.setId(info[1]);
				y.setTeam_number(need.getTeam_number());
				nnnn.add(y);
			}

			orderNamelistService.saveNameList(nnnn);

			AirTicketOrderBean airTicketOrder = new AirTicketOrderBean();
			if (need.getStandard_flg().equals("Y")) {
				// 销售的产品
				ProductBean saleProduct = productService.selectByPrimaryKey(need.getProduct_pk());
				if (saleProduct == null)
					continue;

				// 产品机票信息
				List<ProductAirTicketBean> productAirTickets = productAirTicketService
						.selectByProductPk(saleProduct.getPk());
				ProductAirTicketBean firstTicketInfo = productAirTickets.get(0);

				airTicketOrder.setClient_number(need.getTicket_client_number());
				airTicketOrder.setTicket_cost(need.getAir_ticket_cost());
				airTicketOrder.setFirst_ticket_date(need.getFirst_ticket_date());

				airTicketOrder.setFirst_start_city(firstTicketInfo.getStart_city());
				airTicketOrder.setFirst_end_city(firstTicketInfo.getEnd_city());
				// airTicketOrder.setPeople_count(need.getPeople_count());
				airTicketOrder.setTeam_number(need.getTeam_number());
				airTicketOrder.setTour_product_pk(saleProduct.getPk());
				airTicketOrder.setSale_order_pk(need.getSale_order_pk());

			} else {
				airTicketOrder.setClient_number(need.getTicket_client_number());
				airTicketOrder.setTicket_cost(need.getAir_ticket_cost());
				// airTicketOrder.setPeople_count(need.getPeople_count());
				airTicketOrder.setTeam_number(need.getTeam_number());
				airTicketOrder.setSale_order_pk(need.getSale_order_pk());
			}
			airTicketOrder.setSale_standard_flg(need.getStandard_flg());
			airTicketOrderService.insert(airTicketOrder);
		}

		return SUCCESS;

	}

	public String updateProductDetail() {
		List<ProductBean> products = productService.getAllByParam(null);

		for (ProductBean product : products) {
			if (null != product.getAdult_price() && product.getBusiness_profit_substract() != null
					&& product.getLocal_adult_cost() != null) {

				BigDecimal product_price = product.getAdult_price().subtract(product.getBusiness_profit_substract())
						.subtract(product.getMax_profit_substract());
				// 毛利
				BigDecimal gross_profit = product_price.subtract(product.getAir_ticket_cost())
						.subtract(product.getLocal_adult_cost())
						.subtract((null == product.getOther_cost()) ? BigDecimal.ZERO : product.getOther_cost());

				// 毛利率
				BigDecimal denominator = product.getAdult_price().subtract(product.getBusiness_profit_substract())
						.divide(new BigDecimal(100));

				BigDecimal gross_profit_rate = BigDecimal.ZERO;
				if (denominator.compareTo(BigDecimal.ZERO) != 0)
					gross_profit_rate = gross_profit.divide(denominator, 0, BigDecimal.ROUND_HALF_UP);
				float rate = gross_profit_rate.floatValue();

				// 现付资金
				BigDecimal spot_cash = BigDecimal.ZERO;

				if (product.getCash_flow_air_flg().equals("Y")) {
					spot_cash = spot_cash.add(product.getAir_ticket_cost());
				}

				if (product.getCash_flow_local_flg().equals("Y")) {
					spot_cash = spot_cash.add(product.getLocal_adult_cost());
				}

				if (product.getCash_flow_other_flg().equals("Y")) {
					spot_cash = spot_cash
							.add((null == product.getOther_cost()) ? BigDecimal.ZERO : product.getOther_cost());
				}

				// 现金流
				BigDecimal cash_flow = product_price.subtract(spot_cash);

				product.setGross_profit(gross_profit);
				product.setGross_profit_rate(rate);
				product.setCash_flow(cash_flow);
				product.setSpot_cash(spot_cash);

			}
			if (null != product.getChild_price() && product.getBusiness_profit_substract() != null
					&& product.getLocal_child_cost() != null) {

				// 儿童
				BigDecimal product_child_price = product.getChild_price()
						.subtract(product.getBusiness_profit_substract()).subtract(product.getMax_profit_substract());

				BigDecimal gross_child_profit = product_child_price.subtract(product.getAir_ticket_child_cost())
						.subtract(product.getLocal_child_cost())
						.subtract((null == product.getOther_child_cost()) ? BigDecimal.ZERO
								: product.getOther_child_cost());

				BigDecimal denominator_child = product.getChild_price().subtract(product.getBusiness_profit_substract())
						.divide(new BigDecimal(100));

				BigDecimal gross_child_profit_rate = BigDecimal.ZERO;

				if (denominator_child.compareTo(BigDecimal.ZERO) != 0)
					gross_child_profit_rate = gross_child_profit.divide(denominator_child, 0, BigDecimal.ROUND_HALF_UP);

				float rate_child = gross_child_profit_rate.floatValue();

				// 现付资金
				BigDecimal spot_child_cash = BigDecimal.ZERO;

				if (product.getCash_flow_air_flg().equals("Y")) {
					spot_child_cash = spot_child_cash.add(product.getAir_ticket_child_cost());
				}

				if (product.getCash_flow_local_flg().equals("Y")) {
					spot_child_cash = spot_child_cash.add(product.getLocal_child_cost());
				}

				if (product.getCash_flow_other_flg().equals("Y")) {

					spot_child_cash = spot_child_cash.add(
							(null == product.getOther_child_cost()) ? BigDecimal.ZERO : product.getOther_child_cost());
				}

				// 现金流
				BigDecimal cash_child_flow = product_child_price.subtract(spot_child_cash);

				product.setGross_child_profit(gross_child_profit_rate);
				product.setGross_child_profit_rate(rate_child);
				product.setCash_child_flow(cash_child_flow);
				product.setSpot_child_cash(spot_child_cash);

			}
			productService.sysUpdate(product);
		}

		return SUCCESS;
	}

	@Autowired
	private ClientService clientService;
	@Autowired
	private ClientUserDAO clientUserDao;

	public String insertIntoClientUser() {
		List<ClientBean> clients = clientService.getAllCompaniesByParam(null);
		for (ClientBean client : clients) {

			ClientUserBean cub = new ClientUserBean();
			cub.setClient_pk(client.getPk());
			cub.setUser_pk(client.getSales());
			clientUserDao.insert(cub);
		}

		return SUCCESS;
	}

	@Autowired
	private ReceivedService receivedService;

	public String fixFly() {
		ClientReceivedDetailBean option = new ClientReceivedDetailBean();
		option.setType("FLY");
		List<ClientReceivedDetailBean> res = receivedService.selectByParam(option);
		for (ClientReceivedDetailBean r : res) {
			ReceivableBean receivable = receivableService.selectByTeamNumber(r.getTeam_number());

			receivable.setReceived(receivable.getReceived().subtract(r.getReceived()));
			receivable.setBudget_balance(receivable.getBudget_balance().add(r.getReceived()));

			if (receivable.getFinal_flg().equals("Y")
					&& receivable.getFinal_balance().compareTo(BigDecimal.ZERO) != 0) {
				receivable.setFinal_balance(receivable.getFinal_balance().add(r.getReceived()));
			}

			receivableService.update(receivable);
		}
		return SUCCESS;
	}

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private OrderService orderService;

	public String fixClientRelation() {
		List<ClientEmployeeBean> employees = employeeService.getAllClientEmployeeByParam(null);
		for (ClientEmployeeBean c : employees) {
			String a = DateUtil.castDate2Str(new Date(Long.parseLong(c.getCreate_time())));
			OrderDto option2 = new OrderDto();

			option2.setClient_employee_pk(c.getPk());
			option2.setConfirm_year("2018");

			List<OrderDto> orders = orderService.selectByParam(option2);
			// 删除2018年4月份之前没有订单的
			if (orders.size() == 0 && DateUtil.compare(a, "2018-04-01") == 2) {
				employeeService.delete(c.getPk());
			}
			// 4月份之后没有订单的归为新增级
			if (orders.size() == 0 && DateUtil.compare(a, "2018-03-31") == 1) {
				c.setRelation_level("新增级");
				employeeService.update(c);
			}
			// 有2个订单以内的尝试级
			if (orders.size() > 0 && orders.size() < 3) {
				c.setRelation_level("尝试级");
				employeeService.update(c);
			}

			if (orders.size() > 2 && (c.getRelation_level().equals("朋友级") || c.getRelation_level().equals("商务级"))) {
				c.setRelation_level("尝试级");
				employeeService.update(c);
			} else if (orders.size() > 2) {
				c.setRelation_level("市场级");
				employeeService.update(c);

			}

		}

		return SUCCESS;
	}

	@Autowired
	private ClientEmployeeUserService clientEmployeeUserService;

	/**
	 * 自动更新以公开的客户用户对应表
	 * 
	 * @return
	 */
	public String autoUpdateClientUser() {
		// 清除多余的对应关系
		List<ClientEmployeeUserBean> allCeubs = clientEmployeeUserService.selectByParam(null);
		for (ClientEmployeeUserBean ceub : allCeubs) {
			ClientEmployeeBean employee = employeeService.selectByPrimaryKey(ceub.getEmployee_pk());
			if (null == employee) {
				clientEmployeeUserService.delete(ceub.getPk());
			} else {
				if (ceub.getUser_pk().equals(ResourcesConstants.USER_PUBLIC) && employee.getPublic_flg().equals("N")) {
					clientEmployeeUserService.delete(ceub.getPk());
				}
			}
		}

		ClientEmployeeBean option1 = new ClientEmployeeBean();
		option1.setPublic_flg("Y");
		List<ClientEmployeeBean> employees = employeeService.getAllClientEmployeeByParam(option1);
		for (ClientEmployeeBean employee : employees) {
			List<ClientEmployeeUserBean> ceubs = clientEmployeeUserService.selectByEmployeePk(employee.getPk());
			for (ClientEmployeeUserBean ceub : ceubs) {
				clientEmployeeUserService.delete(ceub.getPk());
			}
			ClientEmployeeUserBean newCeub = new ClientEmployeeUserBean();
			newCeub.setUser_pk(ResourcesConstants.USER_PUBLIC);
			newCeub.setEmployee_pk(employee.getPk());
			newCeub.setCreate_time(DateUtil.getTimeMillis("2018-10-01"));
			clientEmployeeUserService.insertWithCreateTime(newCeub);
		}

		return SUCCESS;
	}

	@Autowired
	private ClientRelationDAO clientRelationDao;

	@Autowired
	private OrderDAO orderDao;
	@Autowired
	private ClientVisitDAO visitDao;
	@Autowired
	private IncomingCallDAO callDao;
	@Autowired
	private MobileTouchDAO touchDao;

	/**
	 * 校正客户关系交流信息
	 * 
	 * @return
	 */
	public String autoUpdateClientRelationConnect() {
		List<ClientRelationBean> crbs = clientRelationDao.selectByParam(null);
		String tempDate = "1988-03-22";
		for (ClientRelationBean crb : crbs) {
			String connect_date = "-";
			String type = "-";
			String extra_info = "-";
			String employee_pk = crb.getClient_employee_pk();
			String date1 = orderDao.selectMaxConfirmDateByEmployeePk(employee_pk);
			String date2 = visitDao.selectMaxVisitDateByEmployeePk(employee_pk);
			String date3 = callDao.selectMaxCallDateByEmployeePk(employee_pk);
			String date4 = touchDao.selectMaxTouchDateByEmployeePk(employee_pk);
			if (null == date1 && null == date2 && null == date3 && null == date4) {
				crb.setConnect_date(connect_date);
				crb.setType(type);
				crb.setExtra_info(extra_info);
			} else {
				List<TempBean> a = new ArrayList<TempBean>();

				if (null == date1)
					date1 = tempDate;
				if (null == date2)
					date2 = tempDate;
				if (null == date3)
					date3 = tempDate;
				if (null == date4)
					date4 = tempDate;
				TempBean d1 = new TempBean();
				TempBean d2 = new TempBean();
				TempBean d3 = new TempBean();
				TempBean d4 = new TempBean();
				d1.setConnect_date(date1);
				d1.setType("ORDER");
				d2.setConnect_date(date2);
				d2.setType("VISIT");
				d3.setConnect_date(date3);
				d3.setType("INCOMING");
				d4.setConnect_date(date4);
				d4.setType("TOUCH");
				a.add(d1);
				a.add(d2);
				a.add(d3);
				a.add(d4);
				Collections.sort(a);
				connect_date = a.get(3).getConnect_date();
				type = a.get(3).getType();
				if (type.equals("ORDER")) {
					OrderDto option = new OrderDto();
					option.setConfirm_date(connect_date);
					option.setClient_employee_pk(employee_pk);
					List<OrderDto> orders = orderDao.selectByParam(option);
					extra_info = orders.get(0).getProduct_name() + ":"
							+ (orders.get(0).getAdult_count()
									+ (orders.get(0).getSpecial_count() == null ? 0 : orders.get(0).getSpecial_count()))
							+ "人";
				} else if (type.equals("VISIT")) {
					ClientVisitBean option = new ClientVisitBean();
					option.setDate(connect_date);
					option.setClient_employee_pk(employee_pk);
					List<ClientVisitBean> orders = visitDao.selectByParam(option);
					extra_info = orders.get(0).getTarget();
				} else if (type.equals("INCOMING")) {
					IncomingCallBean option = new IncomingCallBean();
					option.setDate(connect_date);
					option.setClient_employee_pk(employee_pk);
					List<IncomingCallBean> orders = callDao.selectByParam(option);
					extra_info = orders.get(0).getType();
				} else if (type.equals("TOUCH")) {
					MobileTouchBean option = new MobileTouchBean();
					option.setDate(connect_date);
					option.setClient_employee_pk(employee_pk);
					List<MobileTouchBean> orders = touchDao.selectByParam(option);
					extra_info = orders.get(0).getTouch_target();
				}
				crb.setConnect_date(connect_date);
				crb.setType(type);
				crb.setExtra_info(extra_info);
			}
			clientRelationDao.update(crb);

		}

		return SUCCESS;
	}

	private int reboot_min;

	/**
	 * 重启倒计时
	 * 
	 * @return
	 */
	public String rebootTimer() {
		String nowTime = DateUtil.getDateStr(DateUtil.YYYY_MM_DD_HH_MM_SS);
		String endTime = DateUtil.addMin(nowTime, reboot_min);

		XinChiApplicationContext.setSession(ResourcesConstants.REBOOT_TIMER_KEY, endTime);
		return SUCCESS;
	}

	public List<PayableBean> getPayables() {
		return payables;
	}

	public void setPayables(List<PayableBean> payables) {
		this.payables = payables;
	}

	public List<ReceivableBean> getReceivables() {
		return receivables;
	}

	public void setReceivables(List<ReceivableBean> receivables) {
		this.receivables = receivables;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public int getReboot_min() {
		return reboot_min;
	}

	public void setReboot_min(int reboot_min) {
		this.reboot_min = reboot_min;
	}
}
