package com.xinchi.backend.client.service.impl;

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
	public ClientSummaryDto getClientSummary(ClientRelationSummaryBean relation) {
		ClientSummaryDto cs = new ClientSummaryDto();
		String sales_name = relation.getSales_name();
		String client_count = dao.selectClientCount(sales_name);
		String week_order_count = dao.selectWeekOrderCount(sales_name);
		String month_order_count = dao.selectMonthOrderCount(sales_name);
		String yesterday_visit_count = dao.selectYesterdayVisitCount(sales_name);
		String week_visit_count = dao.selectWeekVisitCount(sales_name);
		String month_visit_count = dao.selectMonthVisitCount(sales_name);
		String yesterday_chat_count = dao.selectYesterdayChatCount(sales_name);
		String week_chat_count = dao.selectWeekChatCount(sales_name);
		String month_chat_count = dao.selectMonthChatCount(sales_name);
		cs.setClient_count(client_count);
		cs.setWeek_order_count(week_order_count);
		cs.setMonth_order_count(month_order_count);
		cs.setYesterday_visit_count(yesterday_visit_count);
		cs.setWeek_visit_count(week_visit_count);
		cs.setMonth_visit_count(month_visit_count);
		cs.setYesterday_chat_count(yesterday_chat_count);
		cs.setWeek_chat_count(week_chat_count);
		cs.setMonth_chat_count(month_chat_count);
		return cs;
	}
}