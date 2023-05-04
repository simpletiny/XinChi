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

import com.xinchi.backend.order.service.OrderService;
import com.xinchi.backend.product.service.ProductOrderOperationService;
import com.xinchi.backend.product.service.ProductOrderTeamNumberService;
import com.xinchi.backend.product.service.ProductSupplierService;
import com.xinchi.backend.ticket.service.FlightService;
import com.xinchi.bean.DropOffBean;
import com.xinchi.bean.FlightBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.PayableOrderBean;
import com.xinchi.bean.ProductOrderOperationBean;
import com.xinchi.bean.ProductOrderTeamNumberBean;
import com.xinchi.bean.ProductSupplierBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

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

	private String standard_flg;

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

		if (standard_flg.equals("Y")) {
			productSuppliers = productSupplierService.selectByProductPk(product_pk);

			for (ProductSupplierBean psb : productSuppliers) {
				adult_price = adult_price.add(psb.getAdult_cost());
				special_price = special_price.add(psb.getChild_cost() == null ? BigDecimal.ZERO : psb.getChild_cost());
			}
		} else {
			productSuppliers = new ArrayList<ProductSupplierBean>();
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

		resultStr = service.finalOperation(operate_pk, final_supplier_cost, json);
		return SUCCESS;
	}

	/**
	 * 打回订单操作至未决算状态
	 * 
	 * @return
	 */
	public String rollBackOperation() {
		resultStr = service.rollBackOperation(operate_pk);
		return SUCCESS;
	}

	/**
	 * 
	 * @return
	 */
	public String updateOrderOperation() {
		resultStr = service.modifyOrderOperation(json);
		return SUCCESS;
	}

	public String searchOperationByTeamNumber() {
		operations = service.selectByTeamNumber(team_number);
		return SUCCESS;
	}

	public String searchOperationByPk() {
		operate_option = service.selectByPrimaryKey(operate_pk);
		return SUCCESS;
	}

	public String searchOpeartionDataByPk() {
		operate_option = service.selectByPrimaryKey(operate_pk);

		List<ProductOrderTeamNumberBean> potns = productOrderNumberService
				.selectByOrderNumber(operate_option.getTeam_number());

		List<String> t_ns = new ArrayList<String>();
		for (ProductOrderTeamNumberBean potn : potns) {
			t_ns.add(potn.getTeam_number());
		}

		orders = orderService.selectByTeamNumbers(t_ns);

		PayableOrderBean option = new PayableOrderBean();
		String supplier_employee_pk = operate_option.getSupplier_employee_pk();
		option.setSupplier_employee_pk(supplier_employee_pk);

		for (OrderDto order : orders) {
			option.setTeam_number(order.getTeam_number());
			List<PayableOrderBean> pos = service.selectPayableOrderByParam(option);
			if (null != pos && pos.size() > 0) {
				order.setPayable(pos.get(0).getBudget_payable());
			}
		}
		return SUCCESS;
	}

	private List<DropOffBean> drop_offs;

	private DropOffBean drop_off;

	public String searchDropOff() {

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			drop_off.setClient_number(sessionBean.getUser_number());
		}
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", drop_off);
		page.setParams(params);

		drop_offs = service.selectDropOffByPage(page);
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

	public String getStandard_flg() {
		return standard_flg;
	}

	public void setStandard_flg(String standard_flg) {
		this.standard_flg = standard_flg;
	}

	public List<DropOffBean> getDrop_offs() {
		return drop_offs;
	}

	public void setDrop_offs(List<DropOffBean> drop_offs) {
		this.drop_offs = drop_offs;
	}

	public DropOffBean getDrop_off() {
		return drop_off;
	}

	public void setDrop_off(DropOffBean drop_off) {
		this.drop_off = drop_off;
	}
}