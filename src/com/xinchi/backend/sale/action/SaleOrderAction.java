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
import com.xinchi.bean.SaleOrderBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.SaleOrderSupplierBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;

@Controller
@Scope("prototype")
public class SaleOrderAction extends BaseAction {
	private static final long serialVersionUID = 962407273491281441L;

	private SaleOrderBean order;
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
	public String createOrder() throws ParseException {

		//保存名单
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

		//保存订单供应商
		List<SaleOrderSupplierBean> arrSupplier = new ArrayList<SaleOrderSupplierBean>();

		JSONArray array = JSONArray.fromObject(supplierJson);
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));

			String supplierEmployeeName = obj.getString("supplierEmployeeName");
			String supplierEmployeePk = obj.getString("supplierEmployeePk");
			String payable = obj.getString("payable");

			SaleOrderSupplierBean supplier = new SaleOrderSupplierBean();

			supplier.setTeam_number(order.getTeam_number());
			supplier.setSupplier_employee_name(supplierEmployeeName);
			supplier.setSupplier_employee_pk(supplierEmployeePk);

			if (null != payable && !payable.equals("")) {
				BigDecimal p = new BigDecimal(payable);
				supplier.setPayable(p);
				sum =  sum.add(p);
			}
			arrSupplier.add(supplier);
		}
		
		//保存订单
		String departureDate = order.getDeparture_date();
		int days = order.getDays();
		String returnDate = DateUtil.addDate(departureDate, days-1);
		order.setReturn_date(returnDate);
		order.setPayable(sum);
		saleOrderService.insert(order);
		
		saleOrderService.saveOrderSupplier(arrSupplier);
		resultStr = OK;
		return SUCCESS;
	}
	
	private List<SaleOrderBean> orders ;
	public String searchOrder(){
		orders = saleOrderService.searchOrders(null);
		return SUCCESS;
	}
	public SaleOrderBean getOrder() {
		return order;
	}

	public void setOrder(SaleOrderBean order) {
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
	public List<SaleOrderBean> getOrders() {
		return orders;
	}
	public void setOrders(List<SaleOrderBean> orders) {
		this.orders = orders;
	}
}
