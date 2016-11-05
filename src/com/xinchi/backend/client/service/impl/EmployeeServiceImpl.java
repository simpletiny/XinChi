package com.xinchi.backend.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.tools.Page;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeDAO dao;

	@Override
	public void insert(ClientEmployeeBean bo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ClientEmployeeBean bo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientEmployeeBean selectByPrimaryKey(String pk) {
		return dao.selectByPrimaryKey(pk);
	}

	@Override
	public List<ClientEmployeeBean> getAllClientEmployeeByParam(
			ClientEmployeeBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	@Transactional
	public String createEmployee(ClientEmployeeBean employee) {
		dao.insert(employee);
		return "success";
	}

	@Override
	@Transactional
	public String updateEmployee(ClientEmployeeBean employee) {
		dao.update(employee);
		return "success";
	}

	@Override
	public List<ClientEmployeeBean> getAllClientEmployeeByPage(
			Page<ClientEmployeeBean> page) {
		return dao.getAllByPage(page);
	}

}