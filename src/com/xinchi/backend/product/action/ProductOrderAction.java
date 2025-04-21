package com.xinchi.backend.product.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.order.service.OrderService;
import com.xinchi.backend.product.service.ProductOrderService;
import com.xinchi.backend.user.service.AssistantManagerService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AssistantManagerBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.bean.ProductOrderNameBean;
import com.xinchi.bean.ProductOrderNameFlightSegmentBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyUser;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductOrderAction extends BaseAction {

	private static final long serialVersionUID = -4933810791428673563L;

	@Autowired
	private ProductOrderService service;

	private String json;

	/**
	 * 产品订单票务处理
	 * 
	 * @return
	 */
	public String createProductOrder() {
		resultStr = service.createProductOrder(json);
		return SUCCESS;
	}

	public String rollBackProductOrder() {
		resultStr = service.rollBackOrder(order_number);
		return SUCCESS;
	}

	private List<ProductOrderBean> orders;

	private ProductOrderBean order;

	@Autowired
	private AssistantManagerService assistantManagerService;

	public String searchProductOrderByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (null == order)
			order = new ProductOrderBean();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			List<String> product_manager_numbers = new ArrayList<>();
			product_manager_numbers.add(sessionBean.getUser_number());
			// 如果是产品助理
			if (roles.contains(ResourcesConstants.USER_ROLE_PRODUCT_ASSISTANT)) {
				AssistantManagerBean assistant_option = new AssistantManagerBean();
				assistant_option.setAssistant_number(sessionBean.getUser_number());
				assistant_option.setAssistant_type(ResourcesConstants.USER_ROLE_PRODUCT_ASSISTANT);
				List<AssistantManagerBean> ambs = assistantManagerService.selectByParam(assistant_option);
				for (AssistantManagerBean amb : ambs) {
					product_manager_numbers.add(amb.getManager_number());
				}
			}
			order.setProduct_manager_numbers(product_manager_numbers);
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", order);
		page.setParams(params);

		orders = service.selectByPage(page);
		return SUCCESS;
	}

	private String team_number;
	private String lock_flg;

	/**
	 * 更改销售订单锁定标识
	 * 
	 * @return
	 */
	public String changeOrderLock() {
		resultStr = service.changeOrderLock(team_number, lock_flg);
		return SUCCESS;
	}

	public String updateProductOrder() {
		resultStr = service.updateProductOrder(order);
		return SUCCESS;
	}

	/**
	 * 检验产品订单下的所有销售订单是否已经锁定
	 * 
	 * @return
	 */
	public String isAllOrdersLocked() {
		resultStr = service.isAllOrdersLocked(order_number);
		return SUCCESS;
	}

	private List<OrderDto> sale_orders;

	private String order_number;

	public String searchSaleOrderByProductOrderNumber() {
		sale_orders = service.searchSaleOrderByProductOrderNumber(order_number);
		return SUCCESS;
	}

	private String supplier_employee_pk;

	public String searchSaleOrderInfoByProductOrderInfo() {
		sale_orders = service.searchSaleOrderInfoByProductOrderInfo(order_number, supplier_employee_pk);
		return SUCCESS;
	}

	private List<SaleOrderNameListBean> passengers;

	public String searchSaleOrderNameListByProductOrderNumber() {
		passengers = service.searchSaleOrderNameListByProductOrderNumber(order_number);
		return SUCCESS;
	}

	private List<String> order_numbers;

	public String searchSaleOrderNameListByProductOrderNumbers() {
		passengers = service.searchSaleOrderNameListByProductOrderNumbers(order_numbers);
		return SUCCESS;
	}

	private List<AirTicketNameListBean> ticket_infos;

	public String searchTicketInfoByOrderNumber() {
		ticket_infos = service.searchTicketInfoByOrderNumber(order_number);
		return SUCCESS;
	}

	@Autowired
	private OrderService orderService;

	public String searchSameProductOrderByTeamNumber() {
		ProductOrderBean option = new ProductOrderBean();
		OrderDto sale_order = orderService.selectByTeamNumber(team_number);
		option.setProduct_name(sale_order.getProduct_name());
		option.setDeparture_date(sale_order.getDeparture_date());
		option.setProduct_model(sale_order.getProduct_model());
		UserSessionBean user = SimpletinyUser.user();
		if (!SimpletinyUser.isAdmin()) {
			option.setProduct_manager_number(user.getUser_number());
		}
		orders = service.selectByParam(option);
		return SUCCESS;
	}

	private List<ProductOrderNameBean> name_list;
	private ProductOrderNameBean name_option;

	public String searchProductOrderNameByPage() {
		UserSessionBean user = SimpletinyUser.user();
		if (!SimpletinyUser.isAdmin()) {
			List<String> product_manager_numbers = new ArrayList<>();
			product_manager_numbers.add(user.getUser_number());
			// 如果是产品助理
			if (user.getUser_roles().contains(ResourcesConstants.USER_ROLE_PRODUCT_ASSISTANT)) {
				AssistantManagerBean assistant_option = new AssistantManagerBean();
				assistant_option.setAssistant_number(user.getUser_number());
				assistant_option.setAssistant_type(ResourcesConstants.USER_ROLE_PRODUCT_ASSISTANT);
				List<AssistantManagerBean> ambs = assistantManagerService.selectByParam(assistant_option);
				for (AssistantManagerBean amb : ambs) {
					product_manager_numbers.add(amb.getManager_number());
				}
			}
			name_option.setProduct_manager_numbers(product_manager_numbers);
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", name_option);
		page.setParams(params);

		name_list = service.searchProductOrderNameByPage(page);
		return SUCCESS;
	}

	/**
	 * 发送票务需求
	 * 
	 * @return
	 */
	public String sendAirTicketNeed() {
		resultStr = service.sendAirTicketNeed(json);
		return SUCCESS;
	}

	/**
	 * 删除票务需求
	 * 
	 * @return
	 */
	public String deleteSendedAirNeed() {
		resultStr = service.deleteSendedAirNeed(air_need_pk);
		return SUCCESS;
	}

	private String name_pk;

	private Map<String, List<ProductOrderNameFlightSegmentBean>> flight_segments;

	public String searchAirNeedByNamePk() {
		flight_segments = service.searchAirNeedByNamePk(name_pk);
		return SUCCESS;
	}

	private String air_need_pk;

	public String selectProductOrderNameByAirNeedPk() {
		name_list = service.selectProductOrderNameByAirNeedPk(air_need_pk);
		return SUCCESS;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public List<ProductOrderBean> getOrders() {
		return orders;
	}

	public ProductOrderBean getOrder() {
		return order;
	}

	public void setOrders(List<ProductOrderBean> orders) {
		this.orders = orders;
	}

	public void setOrder(ProductOrderBean order) {
		this.order = order;
	}

	public List<OrderDto> getSale_orders() {
		return sale_orders;
	}

	public void setSale_orders(List<OrderDto> sale_orders) {
		this.sale_orders = sale_orders;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
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

	public String getLock_flg() {
		return lock_flg;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public void setLock_flg(String lock_flg) {
		this.lock_flg = lock_flg;
	}

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}

	public List<AirTicketNameListBean> getTicket_infos() {
		return ticket_infos;
	}

	public void setTicket_infos(List<AirTicketNameListBean> ticket_infos) {
		this.ticket_infos = ticket_infos;
	}

	public List<ProductOrderNameBean> getName_list() {
		return name_list;
	}

	public void setName_list(List<ProductOrderNameBean> name_list) {
		this.name_list = name_list;
	}

	public ProductOrderNameBean getName_option() {
		return name_option;
	}

	public void setName_option(ProductOrderNameBean name_option) {
		this.name_option = name_option;
	}

	public String getName_pk() {
		return name_pk;
	}

	public void setName_pk(String name_pk) {
		this.name_pk = name_pk;
	}

	public Map<String, List<ProductOrderNameFlightSegmentBean>> getFlight_segments() {
		return flight_segments;
	}

	public void setFlight_segments(Map<String, List<ProductOrderNameFlightSegmentBean>> flight_segments) {
		this.flight_segments = flight_segments;
	}

	public String getAir_need_pk() {
		return air_need_pk;
	}

	public void setAir_need_pk(String air_need_pk) {
		this.air_need_pk = air_need_pk;
	}

	public List<String> getOrder_numbers() {
		return order_numbers;
	}

	public void setOrder_numbers(List<String> order_numbers) {
		this.order_numbers = order_numbers;
	}

}