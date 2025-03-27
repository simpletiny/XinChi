package com.xinchi.backend.product.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.order.service.OrderService;
import com.xinchi.bean.OrderDto;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductNeedAction extends BaseAction {
	private static final long serialVersionUID = 4763290041236544121L;

	private String product_name;
	private String product_model;
	private String departure_date;
	private String product_manager_number;
	private List<OrderDto> sale_orders;
	@Autowired
	private OrderService orderService;

	public String checkSameNeedOrder() {
		OrderDto option = new OrderDto();

		option.setProduct_name(product_name);
		option.setProduct_model(product_model.equals("非标") ? null : product_model);
		option.setDeparture_date(departure_date);

		List<String> confirm_flgs = new ArrayList<String>();
		confirm_flgs.add("Y");
		confirm_flgs.add("F");
		option.setConfirm_flgs(confirm_flgs);
		option.setOperate_flg(ResourcesConstants.ORDER_OPERATE_STATUS_NO + ",");

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		if (!sessionBean.getUser_roles().contains("ADMIN")) {
			option.setProduct_manager_number(product_manager_number);
		}

		sale_orders = orderService.selectWithProductByParam(option);
		return SUCCESS;
	}

	public String getProduct_name() {
		return product_name;
	}

	public String getProduct_model() {
		return product_model;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public void setProduct_model(String product_model) {
		this.product_model = product_model;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public List<OrderDto> getSale_orders() {
		return sale_orders;
	}

	public void setSale_orders(List<OrderDto> sale_orders) {
		this.sale_orders = sale_orders;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}
}