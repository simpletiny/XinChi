package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.bean.ProductGroupSupplierBean;
import com.xinchi.backend.product.dao.ProductGroupSupplierDAO;
import com.xinchi.backend.product.service.ProductGroupSupplierService;

@Service
@Transactional
public class ProductGroupSupplierServiceImpl implements ProductGroupSupplierService {

	@Autowired
	private ProductGroupSupplierDAO dao;

	@Override
	public void insert(ProductGroupSupplierBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(ProductGroupSupplierBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ProductGroupSupplierBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<ProductGroupSupplierBean> getAllByParam(ProductGroupSupplierBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<ProductGroupSupplierBean> selectByGroupPk(String group_pk) {
		ProductGroupSupplierBean options = new ProductGroupSupplierBean();
		options.setGroup_pk(group_pk);
		return dao.selectByParam(options);
	}

}