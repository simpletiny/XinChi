package com.xinchi.backend.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderReportDAO;
import com.xinchi.backend.order.service.OrderReportService;
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

}
