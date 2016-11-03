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

import com.xinchi.backend.sale.service.FinalOrderService;
import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.FinalOrderSupplierBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope("prototype")
public class FinalOrderAction extends BaseAction {
	private static final long serialVersionUID = 962407273491281441L;

	private FinalOrderBean order;
	private String supplierJson;
	// private String nameList;

	@Autowired
	private FinalOrderService finalOrderService;

	/**
	 * 创建订单
	 * 
	 * @return
	 * @throws ParseException
	 */
	public String createFinalOrder() {
		// order.setTeam_number(team_number);

		// 保存名单
		// List<SaleOrderNameListBean> arrName = new
		// ArrayList<SaleOrderNameListBean>();
		//
		// String[] names = nameList.split(";");
		// if (names.length != 0) {
		// for (String str : names) {
		// String people[] = str.split(":");
		// if (people.length != 2)
		// continue;
		//
		// SaleOrderNameListBean name = new SaleOrderNameListBean();
		// name.setName(people[0]);
		// name.setId(people[1]);
		// name.setTeam_number(order.getTeam_number());
		// arrName.add(name);
		// }
		// saleOrderService.saveNameList(arrName);
		// }

		// 保存订单供应商
		List<FinalOrderSupplierBean> arrSupplier = new ArrayList<FinalOrderSupplierBean>();
		
		JSONArray array = JSONArray.fromObject(supplierJson);
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));

			String supplierEmployeeName = obj.getString("supplierEmployeeName");
			String supplierEmployeePk = obj.getString("supplierEmployeePk");
			String payable = obj.getString("payable");

			FinalOrderSupplierBean supplier = new FinalOrderSupplierBean();

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
		finalOrderService.saveOrderSupplier(arrSupplier);
		
		if (null != order.getOther_payment()) {
			sum.add(order.getOther_payment());
		}
		
		// 保存订单
		String departureDate = order.getDeparture_date();
		int days = order.getDays();
		String returnDate = DateUtil.addDate(departureDate, days - 1);
		order.setReturn_date(returnDate);
		order.setPayable(sum);
		order.setClient_debt(order.getReceivable().subtract(order.getReceived()));
		
		finalOrderService.insert(order);

		
		resultStr = OK;
		return SUCCESS;
	}
	
	private List<FinalOrderBean> orders;
	public String searchFinalOrders(){
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			order = new FinalOrderBean();
			order.setCreate_user(sessionBean.getUser_number());
		}
		orders = finalOrderService.searchOrders(order);
		
		return SUCCESS;
	}
	public FinalOrderBean getOrder() {
		return order;
	}

	public void setOrder(FinalOrderBean order) {
		this.order = order;
	}

	public String getSupplierJson() {
		return supplierJson;
	}

	public void setSupplierJson(String supplierJson) {
		this.supplierJson = supplierJson;
	}
	public List<FinalOrderBean> getOrders() {
		return orders;
	}
	public void setOrders(List<FinalOrderBean> orders) {
		this.orders = orders;
	}

}
