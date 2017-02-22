package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.tools.Page;

public interface ClientRelationService {

	public String createVisit(ClientVisitBean visit);

	public List<ClientRelationSummaryBean> getRelationsByPage(Page page);

	public ClientSummaryDto getClientSummary(ClientRelationSummaryBean relation);

}
