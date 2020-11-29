package com.xinchi.backend.supplier.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductOrderSupplierDAO;
import com.xinchi.backend.supplier.dao.SupplierEmployeeDAO;
import com.xinchi.backend.supplier.service.SupplierEmployeeService;
import com.xinchi.bean.OrderSupplierBean;
import com.xinchi.bean.SupplierEmployeeBean;
import com.xinchi.tools.Page;

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
	public List<SupplierEmployeeBean> getAllSupplierEmployeeByParam(SupplierEmployeeBean bo) {
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

	@Override
	public List<SupplierEmployeeBean> getAllSupplierEmployeeByPage(Page<SupplierEmployeeBean> page) {
		return dao.getAllByPage(page);
	}

	@Override
	public List<String> getBodyPksByEmployeePks(String[] employee_pks) {

		return dao.getBodyPksByEmployeePks(employee_pks);
	}

	@Autowired
	private ProductOrderSupplierDAO productOrderSupplierDao;

	@Override
	public String deleteEmployee(String employee_pk) {
		List<OrderSupplierBean> orderSuppliers = productOrderSupplierDao.selectByEmployeePk(employee_pk);
		if (null != orderSuppliers && orderSuppliers.size() > 0) {
			return "existorder";
		} else {
			dao.delete(employee_pk);
			return SUCCESS;
		}
	}

}