package com.xinchi.backend.supplier.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.supplier.dao.SupplierEmployeeDAO;
import com.xinchi.backend.supplier.service.SupplierEmployeeService;
import com.xinchi.bean.SupplierEmployeeBean;

@Service
public class SupplierEmployeeServiceImpl implements SupplierEmployeeService {
	@Autowired
	private SupplierEmployeeDAO dao;

	@Override
	public void insert(SupplierEmployeeBean bo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(SupplierEmployeeBean bo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public SupplierEmployeeBean selectByPrimaryKey(String pk) {
		return dao.selectByPrimaryKey(pk);
	}

	@Override
	public List<SupplierEmployeeBean> getAllSupplierEmployeeByParam(
			SupplierEmployeeBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	@Transactional
	public String createEmployee(SupplierEmployeeBean employee) {
		dao.insert(employee);
		return "success";
	}

	@Override
	@Transactional
	public String updateEmployee(SupplierEmployeeBean employee) {
		dao.update(employee);
		return "success";
	}

}