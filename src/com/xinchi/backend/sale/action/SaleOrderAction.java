package com.xinchi.backend.sale.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.sale.service.SaleOrderService;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.BudgetOrderSupplierBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope("prototype")
public class SaleOrderAction extends BaseAction {
	private static final long serialVersionUID = 962407273491281441L;

	private BudgetOrderBean order;
	private String supplierJson;
	private String nameList;

	@Autowired
	private SaleOrderService saleOrderService;

	/**
	 * 创建订单
	 * 
	 * @return
	 * @throws ParseException
	 */
	public String createOrder() {
		String team_number = generateTeamNumber(order.getDeparture_date());
		order.setTeam_number(team_number);

		// 保存名单
		List<SaleOrderNameListBean> arrName = new ArrayList<SaleOrderNameListBean>();

		String[] names = nameList.split(";");
		if (names.length != 0) {
			for (String str : names) {
				String people[] = str.split(":");
				if (people.length != 2)
					continue;

				SaleOrderNameListBean name = new SaleOrderNameListBean();
				name.setName(people[0]);
				name.setId(people[1]);
				name.setTeam_number(order.getTeam_number());
				arrName.add(name);
			}
			saleOrderService.saveNameList(arrName);
		}

		// 保存订单供应商
		List<BudgetOrderSupplierBean> arrSupplier = new ArrayList<BudgetOrderSupplierBean>();

		JSONArray array = JSONArray.fromObject(supplierJson);
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));

			String supplierEmployeeName = obj.getString("supplierEmployeeName");
			String supplierEmployeePk = obj.getString("supplierEmployeePk");
			String payable = obj.getString("payable");

			BudgetOrderSupplierBean supplier = new BudgetOrderSupplierBean();

			supplier.setTeam_number(order.getTeam_number());
			supplier.setSupplier_employee_name(supplierEmployeeName);
			supplier.setSupplier_employee_pk(supplierEmployeePk);

			if (null != payable && !payable.equals("")) {
				BigDecimal p = new BigDecimal(payable);
				supplier.setPayable(p);
				sum = sum.add(p);
			}
			arrSupplier.add(supplier);
		}

		if (null != order.getOther_payment()) {
			sum.add(order.getOther_payment());
		}
		// 保存订单
		String departureDate = order.getDeparture_date();
		int days = order.getDays();
		String returnDate = DateUtil.addDate(departureDate, days - 1);
		order.setReturn_date(returnDate);
		order.setPayable(sum);
		order.setClient_debt(order.getReceivable());
		order.setReceived(BigDecimal.ZERO);
		saleOrderService.insert(order);

		saleOrderService.saveOrderSupplier(arrSupplier);
		resultStr = OK;
		return SUCCESS;
	}

	/**
	 * 生成团号
	 * 
	 * @return
	 */
	private String generateTeamNumber(String departure_date) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String number = departure_date.replaceAll("-", "");
		String d = DateUtil.getDateStr("HHmm");
		number += d;
		number = sessionBean.getUser_number() + number;
		return number;
	}

	private List<BudgetOrderBean> orders;

	public String searchOrder() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			order = new BudgetOrderBean();
			order.setCreate_user(sessionBean.getUser_number());
		}
		orders = saleOrderService.searchOrders(order);
		return SUCCESS;
	}

	private String order_pk;

	public String searchOneOrder() {
		order = saleOrderService.searchBudgetOrderByPk(order_pk);
		return SUCCESS;
	}

	private List<BudgetOrderSupplierBean> budgetSuppliers;
	private String team_number;

	public String searchOrderSupplier() {
		budgetSuppliers = saleOrderService.searchBudgetSupplier(team_number);
		return SUCCESS;
	}

	public String updateOrder() {

		// 保存名单
		List<SaleOrderNameListBean> arrName = new ArrayList<SaleOrderNameListBean>();

		String[] names = nameList.split(";");
		if (names.length != 0) {
			for (String str : names) {
				String people[] = str.split(":");
				if (people.length != 2)
					continue;

				SaleOrderNameListBean name = new SaleOrderNameListBean();
				name.setName(people[0]);
				name.setId(people[1]);
				name.setTeam_number(order.getTeam_number());
				arrName.add(name);
			}
			// 删除之前的名单
			saleOrderService.deleteNameListByTeamNo(order.getTeam_number());
			saleOrderService.saveNameList(arrName);
		}

		// 保存订单供应商
		List<BudgetOrderSupplierBean> arrSupplier = new ArrayList<BudgetOrderSupplierBean>();

		JSONArray array = JSONArray.fromObject(supplierJson);
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));

			String supplierEmployeeName = obj.getString("supplierEmployeeName");
			String supplierEmployeePk = obj.getString("supplierEmployeePk");
			String payable = obj.getString("payable");

			BudgetOrderSupplierBean supplier = new BudgetOrderSupplierBean();

			supplier.setTeam_number(order.getTeam_number());
			supplier.setSupplier_employee_name(supplierEmployeeName);
			supplier.setSupplier_employee_pk(supplierEmployeePk);

			if (null != payable && !payable.equals("")) {
				BigDecimal p = new BigDecimal(payable);
				supplier.setPayable(p);
				sum = sum.add(p);
			}
			arrSupplier.add(supplier);
		}
		saleOrderService
				.deleteOrderSupplierByTeamNumber(order.getTeam_number());
		
		saleOrderService.saveOrderSupplier(arrSupplier);

		if (null != order.getOther_payment()) {
			sum.add(order.getOther_payment());
		}
		
		// 保存订单
		String departureDate = order.getDeparture_date();
		int days = order.getDays();
		String returnDate = DateUtil.addDate(departureDate, days - 1);
		order.setReturn_date(returnDate);
		order.setPayable(sum);
		
		saleOrderService.update(order);

		resultStr = OK;
		return SUCCESS;
	}
	
	private ClientReceivedDetailBean detail;
	public String saveReceivableDetail(){
		saleOrderService.saveReceivableDetail(detail);
		resultStr = OK;
		return SUCCESS;
	}
	
	private List<ClientReceivedDetailBean> receivableDetails;
	public String searchReceivableDetails(){
		receivableDetails = saleOrderService.searchReceivableDetails(team_number);
		return SUCCESS;
	}
	
	private String detail_pk;
	public String deleteReceivableDetail(){
		saleOrderService.deleteReceivableDetail(detail_pk);
		resultStr = OK;
		return SUCCESS;
	}
	public BudgetOrderBean getOrder() {
		return order;
	}

	public void setOrder(BudgetOrderBean order) {
		this.order = order;
	}

	public String getSupplierJson() {
		return supplierJson;
	}

	public void setSupplierJson(String supplierJson) {
		this.supplierJson = supplierJson;
	}

	public String getNameList() {
		return nameList;
	}

	public void setNameList(String nameList) {
		this.nameList = nameList;
	}

	public List<BudgetOrderBean> getOrders() {
		return orders;
	}

	public void setOrders(List<BudgetOrderBean> orders) {
		this.orders = orders;
	}

	public String getOrder_pk() {
		return order_pk;
	}

	public void setOrder_pk(String order_pk) {
		this.order_pk = order_pk;
	}

	public List<BudgetOrderSupplierBean> getBudgetSuppliers() {
		return budgetSuppliers;
	}

	public void setBudgetSuppliers(List<BudgetOrderSupplierBean> budgetSuppliers) {
		this.budgetSuppliers = budgetSuppliers;
	}

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public ClientReceivedDetailBean getDetail() {
		return detail;
	}

	public void setDetail(ClientReceivedDetailBean detail) {
		this.detail = detail;
	}

	public List<ClientReceivedDetailBean> getReceivableDetails() {
		return receivableDetails;
	}

	public void setReceivableDetails(
			List<ClientReceivedDetailBean> receivableDetails) {
		this.receivableDetails = receivableDetails;
	}

	public String getDetail_pk() {
		return detail_pk;
	}

	public void setDetail_pk(String detail_pk) {
		this.detail_pk = detail_pk;
	}
}
