package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.SaleAcademyViewDAO;
import com.xinchi.backend.culture.service.SaleAcademyViewService;
import com.xinchi.bean.SaleAcademyViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class SaleAcademyViewServiceImpl implements SaleAcademyViewService {

	@Autowired
	private SaleAcademyViewDAO dao;

	@Override
	public void insert(SaleAcademyViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<SaleAcademyViewBean> getAllViewsByPage(Page<SaleAcademyViewBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public SaleAcademyViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(SaleAcademyViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
