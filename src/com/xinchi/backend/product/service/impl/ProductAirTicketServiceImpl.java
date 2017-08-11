package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductAirTicketDAO;
import com.xinchi.backend.product.service.ProductAirTicketService;
import com.xinchi.bean.ProductAirTicketBean;

@Service
@Transactional
public class ProductAirTicketServiceImpl implements ProductAirTicketService {

	@Autowired
	private ProductAirTicketDAO dao;

	@Override
	public void insert(ProductAirTicketBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(ProductAirTicketBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ProductAirTicketBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<ProductAirTicketBean> selectByParam(ProductAirTicketBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public void deleteByProductPk(String product_pk) {
		dao.deleteByProduct_pk(product_pk);
	}

	@Override
	public List<ProductAirTicketBean> selectByProductPk(String product_pk) {

		return dao.selectByProductPk(product_pk);
	}

}