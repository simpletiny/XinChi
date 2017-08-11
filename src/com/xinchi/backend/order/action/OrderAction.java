package com.xinchi.backend.order.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.order.service.BudgetNonStandardOrderService;
import com.xinchi.backend.order.service.BudgetStandardOrderService;
import com.xinchi.backend.order.service.OrderNameListService;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
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

	public String createBudgetStandardOrder() {
		resultStr = bsoService.insert(bsOrder);
		return SUCCESS;
	}

	@Autowired
	private BudgetNonStandardOrderService bnsoService;

	private BudgetNonStandardOrderBean bnsOrder;

	public String createBudgetNonStandardOrder() {
		resultStr = bnsoService.insert(bnsOrder);
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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			option.setCreate_user(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);

		tbcOrders = service.selectCByPage(page);
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
	 * 更新标准预算单
	 * 
	 * @return
	 */
	public String updateBudgetStandardOrder() {
		resultStr = bsoService.update(bsOrder);
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
		return SUCCESS;
	}

	/**
	 * 更新非标准预算单
	 * 
	 * @return
	 */
	public String updateBudgetNonStandardOrder() {
		resultStr = bnsoService.update(bnsOrder);
		return SUCCESS;
	}

	/**
	 * 查询非标准预算单
	 * 
	 * @return
	 */
	public String searchTbcBnsOrderByPk() {
		bnsOrder = bnsoService.selectByPrimaryKey(order_pk);
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
}