package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.HistoryViewDAO;
import com.xinchi.backend.culture.service.HistoryViewService;
import com.xinchi.bean.HistoryViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class HistoryViewServiceImpl implements HistoryViewService {

	@Autowired
	private HistoryViewDAO dao;

	@Override
	public void insert(HistoryViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<HistoryViewBean> getAllViewsByPage(Page<HistoryViewBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public HistoryViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(HistoryViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
