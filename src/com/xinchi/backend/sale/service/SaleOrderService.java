package com.xinchi.backend.sale.service;

import java.util.List;

import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.BudgetOrderSupplierBean;
import com.xinchi.tools.Page;

public interface SaleOrderService {
	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(com.xinchi.bean.BudgetOrderBean bo);

	public void saveNameList(List<SaleOrderNameListBean> arrName);

	public void saveOrderSupplier(List<BudgetOrderSupplierBean> arrSupplier);

	public List<BudgetOrderBean> searchOrders(BudgetOrderBean bo);

	public BudgetOrderBean searchBudgetOrderByPk(String order_pk);

	public List<BudgetOrderSupplierBean> searchBudgetSupplier(String team_number);

	public void deleteNameListByTeamNo(String team_number);

	public void deleteOrderSupplierByTeamNumber(String team_number);

	public void update(BudgetOrderBean order);

	public void saveReceivableDetail(ClientReceivedDetailBean detail);

	public List<ClientReceivedDetailBean> searchReceivableDetails(
			String team_number);

	public void deleteReceivableDetail(String detail_pk);

	public List<BudgetOrderBean> searchOrdersByPage(Page<BudgetOrderBean> page);

	public BudgetOrderBean searchBudgetOrderByTeamNumber(String team_number);
}
