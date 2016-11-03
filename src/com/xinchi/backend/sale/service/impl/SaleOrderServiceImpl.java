package com.xinchi.backend.sale.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sale.dao.SaleOrderDAO;
import com.xinchi.backend.sale.service.SaleOrderService;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.BudgetOrderSupplierBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.SaleOrderNameListBean;

@Service
@Transactional
public class SaleOrderServiceImpl implements SaleOrderService {

	@Autowired
	private SaleOrderDAO dao;

	@Override
	public void insert(BudgetOrderBean bo) {
		dao.insert(bo);
	}

	@Override
	public void saveNameList(List<SaleOrderNameListBean> arrName) {
		dao.saveNameList(arrName);
	}

	@Override
	public void saveOrderSupplier(List<BudgetOrderSupplierBean> arrSupplier) {
		dao.saveOrderSupplier(arrSupplier);
	}

	@Override
	public List<BudgetOrderBean> searchOrders(BudgetOrderBean bo) {

		return dao.selectAllByParam(bo);
	}

	@Override
	public BudgetOrderBean searchBudgetOrderByPk(String order_pk) {
		return dao.selectBudgetOrderByPk(order_pk);
	}

	@Override
	public List<BudgetOrderSupplierBean> searchBudgetSupplier(String team_number) {
		return dao.searchBudgetSupplier(team_number);
	}

	@Override
	public void deleteNameListByTeamNo(String team_number) {
		dao.deleteNameListByTeamNo(team_number);

	}

	@Override
	public void deleteOrderSupplierByTeamNumber(String team_number) {
		dao.deleteOrderSupplierByTeamNumber(team_number);
	}

	@Override
	public void update(BudgetOrderBean order) {
		dao.updateBudgetOrder(order);

	}

	@Override
	public void saveReceivableDetail(ClientReceivedDetailBean detail) {
		BudgetOrderBean order = dao.selectBudgetOrderByTeamNumber(detail.getTeam_number());
		if(null==order.getReceived()){
			order.setReceived(detail.getReceived());
		}else{
			order.setReceived(order.getReceived().add(detail.getReceived()));
		}
		order.setClient_debt(order.getReceivable().subtract(order.getReceived()));
		dao.updateBudgetOrder(order);
		
		dao.saveReceivableDetail(detail);
	}

	@Override
	public List<ClientReceivedDetailBean> searchReceivableDetails(
			String team_number) {

		return dao.searchReceivableDetails(team_number);
	}

	@Override
	public void deleteReceivableDetail(String detail_pk) {
		ClientReceivedDetailBean detail = dao.selectClientReceivedDetailByPk(detail_pk);
		BudgetOrderBean order = dao.selectBudgetOrderByTeamNumber(detail.getTeam_number());
		if(null==order.getReceived()){
			//
		}else{
			order.setReceived(order.getReceived().subtract(detail.getReceived()));
		}
		
		order.setClient_debt(order.getReceivable().subtract(order.getReceived()));
		dao.updateBudgetOrder(order);
		dao.deleteReceivableDetail(detail_pk);
	}

}