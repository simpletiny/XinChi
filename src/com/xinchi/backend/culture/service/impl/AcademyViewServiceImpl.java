package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.AcademyViewDAO;
import com.xinchi.backend.culture.service.AcademyViewService;
import com.xinchi.bean.AcademyViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AcademyViewServiceImpl implements AcademyViewService {

	@Autowired
	private AcademyViewDAO dao;

	@Override
	public void insert(AcademyViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<AcademyViewBean> getAllViewsByPage(Page page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public AcademyViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(AcademyViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
