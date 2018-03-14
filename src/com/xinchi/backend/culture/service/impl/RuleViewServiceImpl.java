package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.RuleViewDAO;
import com.xinchi.backend.culture.service.RuleViewService;
import com.xinchi.bean.RuleViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class RuleViewServiceImpl implements RuleViewService {

	@Autowired
	private RuleViewDAO dao;

	@Override
	public void insert(RuleViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<RuleViewBean> getAllViewsByPage(Page<RuleViewBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public RuleViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(RuleViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
