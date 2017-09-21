package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductSupplierDAO;
import com.xinchi.backend.product.service.ProductSupplierService;
import com.xinchi.bean.ProductSupplierBean;

@Service
@Transactional
public class ProductSupplierServiceImpl implements ProductSupplierService {

	@Autowired
	private ProductSupplierDAO dao;

	@Override
	public void insert(ProductSupplierBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(ProductSupplierBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ProductSupplierBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<ProductSupplierBean> selectByParam(ProductSupplierBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public void deleteByProductPk(String product_pk) {
		dao.deleteByProductPk(product_pk);
	}

	@Override
	public List<ProductSupplierBean> selectByProductPk(String product_pk) {
		return dao.selectByProductPk(product_pk);
	}

}