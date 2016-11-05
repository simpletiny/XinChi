package com.xinchi.backend.sale.dao;

import java.util.List;

import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.BudgetOrderSupplierBean;
import com.xinchi.tools.Page;

public interface SaleOrderDAO {

	public void insert(BudgetOrderBean bo);

	public void saveNameList(List<SaleOrderNameListBean> arrName);

	public void saveOrderSupplier(List<BudgetOrderSupplierBean> arrSupplier);

	public List<BudgetOrderBean> selectAllByParam(BudgetOrderBean bo);

	public BudgetOrderBean selectBudgetOrderByPk(String order_pk);

	public List<BudgetOrderSupplierBean> searchBudgetSupplier(String team_number);

	public void deleteNameListByTeamNo(String team_number);

	public void deleteOrderSupplierByTeamNumber(String team_number);

	public void updateBudgetOrder(BudgetOrderBean order);

	public void saveReceivableDetail(ClientReceivedDetailBean detail);

	public List<ClientReceivedDetailBean> searchReceivableDetails(
			String team_number);

	public void deleteReceivableDetail(String detail_pk);
	
	public BudgetOrderBean selectBudgetOrderByTeamNumber(String team_number);

	public ClientReceivedDetailBean selectClientReceivedDetailByPk(
			String detail_pk);

	public List<BudgetOrderBean> selectAllByPage(Page<BudgetOrderBean> page);

}
