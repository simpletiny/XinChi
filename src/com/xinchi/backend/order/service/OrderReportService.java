package com.xinchi.backend.order.service;

import java.util.List;

import com.xinchi.bean.OrderReportDto;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface OrderReportService extends BaseService {

	public List<OrderReportDto> selectOrderReportByPage(Page<OrderReportDto> page);

	public String apporveTeamReport(String team_number);

	public String rollBackTeamReport(String team_number);
}
