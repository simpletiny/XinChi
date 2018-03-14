package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.ValueViewDAO;
import com.xinchi.backend.culture.service.ValueViewService;
import com.xinchi.bean.ValueViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ValueViewServiceImpl implements ValueViewService {

	@Autowired
	private ValueViewDAO dao;

	@Override
	public void insert(ValueViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<ValueViewBean> getAllViewsByPage(Page<ValueViewBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public ValueViewBean selectViewByPk(String view_pk) {
		
		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(ValueViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
