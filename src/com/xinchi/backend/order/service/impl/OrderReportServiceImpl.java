package com.xinchi.backend.order.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderReportDAO;
import com.xinchi.backend.order.service.OrderReportService;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.OrderReportDto;
import com.xinchi.bean.TeamReportBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class OrderReportServiceImpl implements OrderReportService {

	@Autowired
	private OrderReportDAO dao;

	@Override
	public List<OrderReportDto> selectOrderReportByPage(Page<OrderReportDto> page) {

		return dao.selectOrderReportByPage(page);
	}

	@Override
	public String apporveTeamReport(String team_number) {
		TeamReportBean tr = dao.selectTeamReportByTn(team_number);
		tr.setApproved("Y");
		dao.updateTeamReport(tr);
		return SUCCESS;
	}

	@Override
	public String rollBackTeamReport(String team_number) {
		TeamReportBean tr = dao.selectTeamReportByTn(team_number);
		tr.setApproved("N");
		dao.updateTeamReport(tr);
		return SUCCESS;
	}

	@Autowired
	private OrderDAO orderDao;

	@Autowired
	private BudgetStandardOrderDAO bsoDao;
	@Autowired
	private BudgetNonStandardOrderDAO bnsoDao;

	@Override
	public String fillAriTicketCost(String team_number, BigDecimal air_ticket_cost) {
		OrderDto order = orderDao.selectByTeamNumber(team_number);

		if (order.getStandard_flg().equals("Y")) {
			BudgetStandardOrderBean bso = new BudgetStandardOrderBean();
			bso.setPk(order.getPk());
			bso.setAir_ticket_cost(air_ticket_cost);
			bsoDao.update(bso);
		} else {
			BudgetNonStandardOrderBean bnso = new BudgetNonStandardOrderBean();
			bnso.setPk(order.getPk());
			bnso.setAir_ticket_cost(air_ticket_cost);
			bnsoDao.update(bnso);
		}

		return SUCCESS;
	}

}
