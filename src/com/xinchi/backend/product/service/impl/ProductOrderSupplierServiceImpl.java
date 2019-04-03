package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductOrderSupplierDAO;
import com.xinchi.backend.product.service.ProductOrderSupplierService;
import com.xinchi.bean.OrderSupplierBean;

@Service
@Transactional
public class ProductOrderSupplierServiceImpl implements ProductOrderSupplierService {

	@Autowired
	private ProductOrderSupplierDAO dao;

	@Override
	public void insert(OrderSupplierBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(OrderSupplierBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public OrderSupplierBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<OrderSupplierBean> selectByParam(OrderSupplierBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public void deleteByProductPk(String product_pk) {
		dao.deleteByProductPk(product_pk);
	}

	@Override
	public List<OrderSupplierBean> selectByProductPk(String product_pk) {
		return dao.selectByProductPk(product_pk);
	}

}