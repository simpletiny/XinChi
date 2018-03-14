package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.SaleRuleViewDAO;
import com.xinchi.backend.culture.service.SaleRuleViewService;
import com.xinchi.bean.SaleRuleViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class SaleRuleViewServiceImpl implements SaleRuleViewService {

	@Autowired
	private SaleRuleViewDAO dao;

	@Override
	public void insert(SaleRuleViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<SaleRuleViewBean> getAllViewsByPage(Page<SaleRuleViewBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public SaleRuleViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(SaleRuleViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
