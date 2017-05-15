package com.xinchi.backend.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeDAO dao;
	@Autowired
	private UserDAO userDao;

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
	public List<ClientEmployeeBean> getAllClientEmployeeByParam(ClientEmployeeBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	public String createEmployee(ClientEmployeeBean employee) {

		List<ClientEmployeeBean> exists = dao.getAllByParam(employee);

		if (exists != null && exists.size() > 0)
			return "exist";
		dao.insert(employee);
		return "success";
	}

	@Override
	public String updateEmployee(ClientEmployeeBean employee) {
		List<ClientEmployeeBean> exists = dao.getAllByParam(employee);

		if (exists != null && exists.size() > 0) {
			if (exists.get(0).getPk().equals(employee.getPk())) {
				dao.update(employee);
				return "success";
			} else {
				return "exist";
			}
		} else {
			dao.update(employee);
			return "success";
		}
	}

	@Override
	public List<ClientEmployeeBean> getAllClientEmployeeByPage(Page<ClientEmployeeBean> page) {
		return dao.getAllByPage(page);
	}

	@Override
	public List<String> getBodyPksByEmployeePks(String[] employee_pks) {

		return dao.getBodyPksByEmployeePks(employee_pks);
	}

	@Override
	public String deleteClientEmployee(List<String> employee_pks) {
		dao.deleteClientEmployeeByPks(employee_pks);
		return "success";
	}
	@Override
	public String recoveryClientEmployee(List<String> employee_pks) {
		dao.recoveryClientEmployeeByPks(employee_pks);
		return "success";
	}

}