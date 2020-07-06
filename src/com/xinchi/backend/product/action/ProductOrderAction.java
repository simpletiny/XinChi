package com.xinchi.backend.product.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.product.service.ProductOrderService;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
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

	private List<ProductOrderBean> orders;

	private ProductOrderBean order;

	public String searchProductOrderByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (null == order)
			order = new ProductOrderBean();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			order.setProduct_manager_number(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", order);
		page.setParams(params);

		orders = service.selectByPage(page);
		return SUCCESS;
	}

	private List<OrderDto> sale_orders;

	private String order_number;

	public String searchSaleOrderByProductOrderNumber() {
		sale_orders = service.searchSaleOrderByProductOrderNumber(order_number);
		return SUCCESS;
	}

	private List<SaleOrderNameListBean> passengers;

	public String searchSaleOrderNameListByProductOrderNumber() {
		passengers = service.searchSaleOrderNameListByProductOrderNumber(order_number);
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

}