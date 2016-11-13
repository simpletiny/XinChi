package com.xinchi.backend.sale.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sale.dao.FinalOrderDAO;
import com.xinchi.backend.sale.dao.SaleOrderDAO;
import com.xinchi.backend.sale.service.FinalOrderService;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.FinalOrderSupplierBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class FinalOrderServiceImpl implements FinalOrderService {
	@Autowired
	private FinalOrderDAO dao;

	@Autowired
	private SaleOrderDAO saleDao;

	@Override
	public void insert(FinalOrderBean order) {
		BudgetOrderBean budgetOrder = saleDao
				.selectBudgetOrderByTeamNumber(order.getTeam_number());

		budgetOrder.setFinal_flg("Y");
		saleDao.updateBudgetOrder(budgetOrder);
		dao.insert(order);

	}

	@Override
	public void saveOrderSupplier(List<FinalOrderSupplierBean> arrSupplier) {
		dao.saveOrderSupplier(arrSupplier);
	}

	@Override
	public List<FinalOrderBean> searchOrders(FinalOrderBean order) {
		return dao.selectAllByParam(order);
	}

	@Override
	public FinalOrderBean searchFinalOrderByPk(String order_pk) {
		return dao.searchFinalOrderByPk(order_pk);
	}

	@Override
	public List<FinalOrderSupplierBean> searchFinalSupplier(String team_number) {
		return dao.searchFinalSupplier(team_number);
	}

	@Override
	public List<FinalOrderBean> searchOrdersByPage(Page<FinalOrderBean> page) {
		return dao.selectAllByPage(page);
	}

	@Override
	public FinalOrderBean getFinalOrderByTeamNo(String team_number) {
		
		return dao.selectByTeamNumber(team_number);
	}

}
