package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.TicketRuleViewDAO;
import com.xinchi.backend.culture.service.TicketRuleViewService;
import com.xinchi.bean.TicketRuleViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class TicketRuleViewServiceImpl implements TicketRuleViewService {

	@Autowired
	private TicketRuleViewDAO dao;

	@Override
	public void insert(TicketRuleViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<TicketRuleViewBean> getAllViewsByPage(Page<TicketRuleViewBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public TicketRuleViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(TicketRuleViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
