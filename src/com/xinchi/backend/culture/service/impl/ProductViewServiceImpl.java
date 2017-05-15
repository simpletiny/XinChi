package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.ProductViewDAO;
import com.xinchi.backend.culture.service.ProductViewService;
import com.xinchi.bean.ProductViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ProductViewServiceImpl implements ProductViewService {

	@Autowired
	private ProductViewDAO dao;

	@Override
	public void insert(ProductViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<ProductViewBean> getAllViewsByPage(Page page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public ProductViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(ProductViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
