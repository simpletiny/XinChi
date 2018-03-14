package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.ProductAcademyViewDAO;
import com.xinchi.backend.culture.service.ProductAcademyViewService;
import com.xinchi.bean.ProductAcademyViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ProductAcademyViewServiceImpl implements ProductAcademyViewService {

	@Autowired
	private ProductAcademyViewDAO dao;

	@Override
	public void insert(ProductAcademyViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<ProductAcademyViewBean> getAllViewsByPage(Page<ProductAcademyViewBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public ProductAcademyViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(ProductAcademyViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
