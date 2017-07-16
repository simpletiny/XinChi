package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "客户关系")
public interface ClientRelationService {

	@LogDescription(des = "新增拜访")
	public String createVisit(ClientVisitBean visit);

	@LogDescription(des = "客户关系查看")
	public List<ClientRelationSummaryBean> getRelationsByPage(Page page);

	@LogDescription(ignore = true)
	public List<ClientSummaryDto> getClientSummary(ClientRelationSummaryBean relation);

	@LogDescription(ignore = true)
	public String selectClientEmployeeCount(ClientRelationSummaryBean relation);

	@LogDescription(ignore = true)
	public String selectMonthOrderCount(ClientRelationSummaryBean relation);

	@LogDescription(des = "拜访查看")
	public List<ClientVisitBean> selectVisitByPage(Page page);

}
