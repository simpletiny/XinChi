package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.ProductRuleViewDAO;
import com.xinchi.backend.culture.service.ProductRuleViewService;
import com.xinchi.bean.ProductRuleViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ProductRuleViewServiceImpl implements ProductRuleViewService {

	@Autowired
	private ProductRuleViewDAO dao;

	@Override
	public void insert(ProductRuleViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<ProductRuleViewBean> getAllViewsByPage(Page<ProductRuleViewBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public ProductRuleViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(ProductRuleViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
