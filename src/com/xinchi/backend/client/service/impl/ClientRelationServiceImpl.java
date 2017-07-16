package com.xinchi.backend.client.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientRelationDAO;
import com.xinchi.backend.client.dao.ClientVisitDAO;
import com.xinchi.backend.client.service.ClientRelationService;
import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.common.SimpletinyString;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ClientRelationServiceImpl implements ClientRelationService {

	@Autowired
	private ClientVisitDAO visitDao;
	@Autowired
	private ClientRelationDAO dao;

	@Override
	public String createVisit(ClientVisitBean visit) {
		visitDao.insert(visit);
		return "OK";
	}

	@Override
	public List<ClientRelationSummaryBean> getRelationsByPage(Page page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<ClientSummaryDto> getClientSummary(ClientRelationSummaryBean relation) {
		String sales_name = relation.getSales_name();
		List<ClientSummaryDto> cs = new ArrayList<ClientSummaryDto>();
		if (SimpletinyString.isEmpty(relation.getLevel())) {
			cs = dao.selectSummary(sales_name);
		} else if (relation.getLevel().equals("关系度")) {
			cs = dao.selectRelationSummary(sales_name);
		} else if (relation.getLevel().equals("回款誉")) {
			cs = dao.selectBackSummary(sales_name);
		} else if (relation.getLevel().equals("市场力")) {
			cs = dao.selectMarketSummary(sales_name);
		}

		return cs;
	}

	@Override
	public String selectClientEmployeeCount(ClientRelationSummaryBean relation) {
		String sales_name = relation.getSales_name();
		return dao.selectClientCount(sales_name);
	}

	@Override
	public String selectMonthOrderCount(ClientRelationSummaryBean relation) {
		String sales_name = relation.getSales_name();
		return dao.selectMonthOrderCount(sales_name);
	}

	@Override
	public List<ClientVisitBean> selectVisitByPage(Page page) {

		return visitDao.selectByPage(page);
	}
}