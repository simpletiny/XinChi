package com.xinchi.backend.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientEmployeeUserDAO;
import com.xinchi.backend.client.service.ClientEmployeeUserService;
import com.xinchi.bean.ClientEmployeeUserBean;

@Service
@Transactional
public class ClientEmployeeUserServiceImpl implements ClientEmployeeUserService {

	@Autowired
	private ClientEmployeeUserDAO dao;

	@Override
	public void insert(ClientEmployeeUserBean bo) {
		dao.insert(bo);
	}

	@Override
	public void update(ClientEmployeeUserBean bo) {
		dao.update(bo);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public void deleteByEmployeePk(String employee_pk) {
		dao.deleteByEmployeePk(employee_pk);

	}

	@Override
	public List<ClientEmployeeUserBean> selectByEmployeePk(String employee_pk) {
		return dao.selectByEmployeePk(employee_pk);
	}

	@Override
	public void insertWithCreateTime(ClientEmployeeUserBean bo) {
		dao.insertWithCreateTime(bo);

	}

	@Override
	public List<ClientEmployeeUserBean> selectByParam(ClientEmployeeUserBean option) {

		return dao.selectByParam(option);
	}

}