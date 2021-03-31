package com.xinchi.backend.order.dao;

import java.util.List;

import com.xinchi.bean.OrderReportDto;
import com.xinchi.bean.TeamReportBean;
import com.xinchi.tools.Page;

public interface OrderReportDAO {

	public List<OrderReportDto> selectOrderReportByPage(Page<OrderReportDto> page);

	public TeamReportBean selectTeamReportByTn(String team_number);

	public void updateTeamReport(TeamReportBean tr);

}