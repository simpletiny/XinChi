package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.TopAcademyViewDAO;
import com.xinchi.backend.culture.service.TopAcademyViewService;
import com.xinchi.bean.TopAcademyViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class TopAcademyViewServiceImpl implements TopAcademyViewService {

	@Autowired
	private TopAcademyViewDAO dao;

	@Override
	public void insert(TopAcademyViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<TopAcademyViewBean> getAllViewsByPage(Page<TopAcademyViewBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public TopAcademyViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(TopAcademyViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
