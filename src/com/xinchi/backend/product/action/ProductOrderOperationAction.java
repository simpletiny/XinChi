package com.xinchi.backend.product.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.order.service.BudgetNonStandardOrderService;
import com.xinchi.backend.order.service.BudgetStandardOrderService;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.backend.payable.service.PayableService;
import com.xinchi.backend.product.service.ProductOrderOperationService;
import com.xinchi.backend.product.service.ProductOrderTeamNumberService;
import com.xinchi.backend.product.service.ProductSupplierService;
import com.xinchi.backend.ticket.service.FlightService;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.FlightBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.ProductOrderAirBaseBean;
import com.xinchi.bean.ProductOrderOperationBean;
import com.xinchi.bean.ProductOrderTeamNumberBean;
import com.xinchi.bean.ProductSupplierBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductOrderOperationAction extends BaseAction {
	private static final long serialVersionUID = 8708141404532798268L;

	private String json;

	@Autowired
	private ProductOrderOperationService service;

	private String team_number;

	@Autowired
	private OrderService orderService;

	@Autowired
	private BudgetNonStandardOrderService bnsoService;

	@Autowired
	private BudgetStandardOrderService bsoService;

	@Autowired
	private PayableService payableService;

	private String product_pk;
	private String order_pk;

	private List<ProductSupplierBean> productSuppliers;

	@Autowired
	private ProductSupplierService productSupplierService;

	private OrderDto order;

	private List<OrderDto> orders;

	private int adult_count;
	private int special_count;

	private String product_order_number;

	@Autowired
	private ProductOrderTeamNumberService productOrderNumberService;

	public String searchProductDataForOrder() {
		List<ProductOrderTeamNumberBean> potns = productOrderNumberService.selectByOrderNumber(product_order_number);

		List<String> t_ns = new ArrayList<String>();
		for (ProductOrderTeamNumberBean potn : potns) {
			t_ns.add(potn.getTeam_number());
		}

		orders = orderService.selectByTeamNumbers(t_ns);

		BigDecimal adult_price = BigDecimal.ZERO;
		BigDecimal special_price = BigDecimal.ZERO;

		productSuppliers = productSupplierService.selectByProductPk(product_pk);

		for (ProductSupplierBean psb : productSuppliers) {
			adult_price = adult_price.add(psb.getAdult_cost());
			special_price = special_price.add(psb.getChild_cost() == null ? BigDecimal.ZERO : psb.getChild_cost());
		}

		for (OrderDto o : orders) {
			BigDecimal product_cost = BigDecimal.ZERO;
			product_cost = product_cost.add(adult_price.multiply(new BigDecimal(o.getAdult_count())).add(
					special_price.multiply(new BigDecimal(o.getSpecial_count() == null ? 0 : o.getSpecial_count()))));
			o.setProduct_cost(product_cost);
			adult_count += o.getAdult_count();
			special_count += o.getSpecial_count() == null ? 0 : o.getSpecial_count();
		}
		return SUCCESS;
	}

	public String searchProductSuppliersByPk() {
		productSuppliers = productSupplierService.selectByProductPk(product_pk);

		if (productSuppliers != null && productSuppliers.size() > 0) {
			OrderDto order = orderService.selectByTeamNumber(team_number);
			BigDecimal peopleCnt = new BigDecimal(
					order.getAdult_count() + (null == order.getSpecial_count() ? 0 : order.getSpecial_count()));

			for (ProductSupplierBean ps : productSuppliers) {
				ps.setSum_cost(ps.getSupplier_cost().multiply(peopleCnt));
			}
		}
		return SUCCESS;
	}

	private FlightBean flight;

	@Autowired
	private FlightService flightService;

	public String searchProductFlightByProductPk() {
		flight = flightService.selectByProductPk(product_pk);
		if (null == flight)
			flight = new FlightBean();
		return SUCCESS;
	}

	private ProductOrderAirBaseBean air_base;

	/**
	 * 生成订单操作
	 * 
	 * @return
	 */
	public String createOrderOperation() {
		resultStr = service.createOrderOperation(json);
		return SUCCESS;
	}

	private ProductOrderOperationBean operate_option;

	private List<ProductOrderOperationBean> operations;

	public String searchProductOrderOperationByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (null == operate_option)
			operate_option = new ProductOrderOperationBean();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			operate_option.setCreate_user(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", operate_option);
		page.setParams(params);

		operations = service.selectByPage(page);

		return SUCCESS;
	}

	private String operate_pks;

	/**
	 * 确认操作订单
	 * 
	 * @return
	 */
	public String confirmOperation() {
		String[] o_pks = operate_pks.split(",");
		for (String operate_pk : o_pks) {
			ProductOrderOperationBean operation = service.selectByPrimaryKey(operate_pk);
			operation.setStatus("Y");
			service.update(operation);
		}

		resultStr = SUCCESS;
		return SUCCESS;
	}

	private String team_numbers;

	/**
	 * 打回操作中的订单
	 * 
	 * @return
	 */
	public String deleteOperation() {
		resultStr = service.deleteOperation(team_numbers);
		return SUCCESS;
	}

	private String operate_pk;

	private BigDecimal final_supplier_cost;

	/**
	 * 决算操作订单
	 * 
	 * @return
	 */
	public String finalOperation() {
		ProductOrderOperationBean operation = service.selectByPrimaryKey(operate_pk);
		operation.setStatus("F");
		operation.setFinal_supplier_cost(final_supplier_cost);

		service.update(operation);

		// 更新应付款
		PayableBean payable_option = new PayableBean();
		payable_option.setTeam_number(operation.getTeam_number());
		payable_option.setSupplier_employee_pk(operation.getSupplier_employee_pk());
		List<PayableBean> payables = payableService.selectByParam(payable_option);
		if (null != payables && payables.size() > 0) {
			PayableBean payable = payables.get(0);
			payable.setFinal_flg("Y");
			payable.setFinal_payable(final_supplier_cost);
			payable.setFinal_balance(final_supplier_cost.subtract(payable.getPaid()));
			payableService.update(payable);
		}

		resultStr = SUCCESS;
		return SUCCESS;
	}

	/**
	 * 打回订单操作至未决算状态
	 * 
	 * @return
	 */
	public String rollBackOperation() {
		ProductOrderOperationBean operation = service.selectByPrimaryKey(operate_pk);
		operation.setStatus("Y");
		operation.setFinal_supplier_cost(BigDecimal.ZERO);

		service.update(operation);

		// 更新应付款
		PayableBean payable_option = new PayableBean();
		payable_option.setTeam_number(operation.getTeam_number());
		payable_option.setSupplier_employee_pk(operation.getSupplier_employee_pk());
		List<PayableBean> payables = payableService.selectByParam(payable_option);
		if (null != payables && payables.size() > 0) {
			PayableBean payable = payables.get(0);
			payable.setFinal_flg("N");
			payable.setFinal_payable(BigDecimal.ZERO);
			payable.setFinal_balance(BigDecimal.ZERO);
			payableService.update(payable);
		}
		resultStr = SUCCESS;

		return SUCCESS;
	}

	/**
	 * 
	 * @return
	 */
	public String updateOrderOperation() {
		OrderDto order = orderService.selectByTeamNumber(team_number);
		String departure_date = order.getDeparture_date();
		String return_date = DateUtil.addDate(departure_date, order.getDays() - 1);
		int people_count = order.getAdult_count() + (null == order.getSpecial_count() ? 0 : order.getSpecial_count());

		BigDecimal product_cost = BigDecimal.ZERO;
		JSONArray array = JSONArray.fromObject(json);
		int sumcount = array.size();

		// 删除之前的操作
		service.deleteByTeamNumber(team_number);

		// 之前的应付款
		PayableBean payable_options = new PayableBean();
		payable_options.setTeam_number(team_number);
		List<PayableBean> payables = payableService.selectByParam(payable_options);

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			int supplier_index = obj.getInt("supplier_index");
			String supplier_employee_pk = obj.getString("supplier_employee_pk");
			String supplier_product_name = obj.getString("supplier_product_name");
			String c = obj.getString("supplier_cost");
			BigDecimal supplier_cost = null == c ? BigDecimal.ZERO : new BigDecimal(c);

			product_cost = product_cost.add(supplier_cost);
			int land_day = obj.getInt("land_day");
			String pick_type = obj.getString("pick_type");
			String picker = obj.getString("picker");
			String picker_cellphone = obj.getString("picker_cellphone");
			int off_day = obj.getInt("off_day");
			String send_type = obj.getString("send_type");

			ProductOrderOperationBean poo = new ProductOrderOperationBean();
			poo.setSupplier_count(sumcount);
			poo.setTeam_number(team_number);
			poo.setOperate_index(supplier_index);
			poo.setSupplier_cost(supplier_cost);
			poo.setSupplier_product_name(supplier_product_name);
			poo.setPeople_count(people_count);
			String pick_date = DateUtil.addDate(departure_date, land_day - 1);
			String send_date = DateUtil.addDate(departure_date, off_day - 1);
			poo.setPick_date(pick_date);
			poo.setSend_date(send_date);
			poo.setSupplier_employee_pk(supplier_employee_pk);
			poo.setPick_type(pick_type);
			poo.setPicker_cellphone(picker_cellphone);
			poo.setSend_type(send_type);
			poo.setPicker(picker);
			poo.setOff_day(off_day);
			poo.setLand_day(land_day);
			service.insert(poo);

			// 生成应付款
			PayableBean payable = new PayableBean();

			for (PayableBean p : payables) {
				if (p.getSupplier_employee_pk().equals(supplier_employee_pk)) {
					payable = p;
					payables.remove(p);
					break;
				}
			}

			payable.setTeam_number(team_number);
			payable.setFinal_flg("N");
			payable.setSupplier_employee_pk(supplier_employee_pk);
			payable.setDeparture_date(departure_date);
			payable.setReturn_date(return_date);
			payable.setProduct(supplier_product_name);
			payable.setPeople_count(people_count);
			payable.setBudget_payable(supplier_cost);
			payable.setSales(order.getCreate_user_number());

			if (null != payable.getPk()) {
				payableService.update(payable);
			} else {
				payable.setPaid(BigDecimal.ZERO);
				payable.setBudget_balance(supplier_cost);
				payableService.insert(payable);
			}
		}

		// 删除已经不存在的应付款
		for (PayableBean p : payables) {
			payableService.deleteByPk(p.getPk());
		}

		String standard_flg = order.getStandard_flg();
		String order_pk = order.getPk();

		if (standard_flg.equals("Y")) {
			BudgetStandardOrderBean bsOrder = bsoService.selectByPrimaryKey(order_pk);
			bsOrder.setProduct_cost(product_cost);
			bsoService.updateComment(bsOrder);
		} else {
			BudgetNonStandardOrderBean bnsOrder = bnsoService.selectByPrimaryKey(order_pk);
			bnsOrder.setProduct_cost(product_cost);
			bnsoService.updateComment(bnsOrder);
		}
		resultStr = SUCCESS;
		return SUCCESS;
	}

	public String searchOperationByTeamNumber() {
		operations = service.selectByTeamNumber(team_number);
		return SUCCESS;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public ProductOrderOperationBean getOperate_option() {
		return operate_option;
	}

	public void setOperate_option(ProductOrderOperationBean operate_option) {
		this.operate_option = operate_option;
	}

	public List<ProductOrderOperationBean> getOperations() {
		return operations;
	}

	public void setOperations(List<ProductOrderOperationBean> operations) {
		this.operations = operations;
	}

	public String getOperate_pks() {
		return operate_pks;
	}

	public void setOperate_pks(String operate_pks) {
		this.operate_pks = operate_pks;
	}

	public String getProduct_pk() {
		return product_pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public String getOrder_pk() {
		return order_pk;
	}

	public void setOrder_pk(String order_pk) {
		this.order_pk = order_pk;
	}

	public List<ProductSupplierBean> getProductSuppliers() {
		return productSuppliers;
	}

	public void setProductSuppliers(List<ProductSupplierBean> productSuppliers) {
		this.productSuppliers = productSuppliers;
	}

	public String getOperate_pk() {
		return operate_pk;
	}

	public void setOperate_pk(String operate_pk) {
		this.operate_pk = operate_pk;
	}

	public BigDecimal getFinal_supplier_cost() {
		return final_supplier_cost;
	}

	public void setFinal_supplier_cost(BigDecimal final_supplier_cost) {
		this.final_supplier_cost = final_supplier_cost;
	}

	public String getTeam_numbers() {
		return team_numbers;
	}

	public void setTeam_numbers(String team_numbers) {
		this.team_numbers = team_numbers;
	}

	public OrderDto getOrder() {
		return order;
	}

	public void setOrder(OrderDto order) {
		this.order = order;
	}

	public FlightBean getFlight() {
		return flight;
	}

	public void setFlight(FlightBean flight) {
		this.flight = flight;
	}

	public ProductOrderAirBaseBean getAir_base() {
		return air_base;
	}

	public void setAir_base(ProductOrderAirBaseBean air_base) {
		this.air_base = air_base;
	}

	public List<OrderDto> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDto> orders) {
		this.orders = orders;
	}

	public int getAdult_count() {
		return adult_count;
	}

	public int getSpecial_count() {
		return special_count;
	}

	public void setAdult_count(int adult_count) {
		this.adult_count = adult_count;
	}

	public void setSpecial_count(int special_count) {
		this.special_count = special_count;
	}

	public String getProduct_order_number() {
		return product_order_number;
	}

	public void setProduct_order_number(String product_order_number) {
		this.product_order_number = product_order_number;
	}
}