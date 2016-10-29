package com.xinchi.backend.supplier.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.supplier.dao.SupplierDAO;
import com.xinchi.backend.supplier.service.SupplierService;
import com.xinchi.bean.SupplierBean;

@Service
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	private SupplierDAO dao;

	@Override
	@Transactional
	public String createSupplier(SupplierBean supplier) {
		dao.insert(supplier);
		return "success";
	}

	@Override
	public void insert(com.xinchi.bean.SupplierBean bo) {
		dao.insert(bo);
	}

	@Override
	@Transactional
	public void update(com.xinchi.bean.SupplierBean bo) {
		dao.update(bo);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public com.xinchi.bean.SupplierBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<com.xinchi.bean.SupplierBean> getAllCompaniesByParam(
			com.xinchi.bean.SupplierBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	public String updateSupplier(SupplierBean supplier) {
		dao.update(supplier);
		return "success";
	}

}