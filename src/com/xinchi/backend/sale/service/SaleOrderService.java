package com.xinchi.backend.sale.service;

import java.util.List;

import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.BudgetOrderSupplierBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "预算单")
public interface SaleOrderService {
	/**
	 * 新增
	 * 
	 * @param bo
	 */
	@LogDescription(des = "新建预算单")
	public void insert(com.xinchi.bean.BudgetOrderBean bo);

	@LogDescription(ignore = true)
	public void saveNameList(List<SaleOrderNameListBean> arrName);

	@LogDescription(ignore = true)
	public void saveOrderSupplier(List<BudgetOrderSupplierBean> arrSupplier);

	@LogDescription(ignore = true)
	public List<BudgetOrderBean> searchOrders(BudgetOrderBean bo);

	@LogDescription(des = "查看预算单详情")
	public BudgetOrderBean searchBudgetOrderByPk(String order_pk);

	@LogDescription(ignore = true)
	public List<BudgetOrderSupplierBean> searchBudgetSupplier(String team_number);

	@LogDescription(ignore = true)
	public void deleteNameListByTeamNo(String team_number);

	@LogDescription(ignore = true)
	public void deleteOrderSupplierByTeamNumber(String team_number);

	@LogDescription(des = "修改预算单")
	public void update(BudgetOrderBean order);

	@LogDescription(ignore = true)
	public void saveReceivableDetail(ClientReceivedDetailBean detail);

	@LogDescription(ignore = true)
	public List<ClientReceivedDetailBean> searchReceivableDetails(String team_number);

	@LogDescription(ignore = true)
	public void deleteReceivableDetail(String detail_pk);

	@LogDescription(des = "搜索预算单")
	public List<BudgetOrderBean> searchOrdersByPage(Page<BudgetOrderBean> page);

	@LogDescription(ignore = true)
	public BudgetOrderBean searchBudgetOrderByTeamNumber(String team_number);

	@LogDescription(ignore = true)
	public List<BudgetOrderSupplierBean> searchBudgetSupplierByParam(BudgetOrderSupplierBean bo);
}
