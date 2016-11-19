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
	public List<ClientEmployeeBean> getAllClientEmployeeByParam(
			ClientEmployeeBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	@Transactional
	public String createEmployee(ClientEmployeeBean employee) {
		if (employee.getPublic_flg().equals("Y")) {
			employee.setSales("");
			employee.setSales_name("公开");
		} else {
			if (!employee.getSales().equals("")) {
				String[] userPks = employee.getSales().split(",");
				String sales_name = "";

				List<UserBaseBean> users = userDao.getAllByPks(userPks);
				for (UserBaseBean user : users) {
					sales_name += user.getUser_name() + ",";
				}
				sales_name = sales_name.substring(0, sales_name.length() - 1);
				employee.setSales_name(sales_name);
			}
		}
		dao.insert(employee);
		return "success";
	}

	@Override
	@Transactional
	public String updateEmployee(ClientEmployeeBean employee) {
		if (employee.getPublic_flg().equals("Y")) {
			employee.setSales("");
			employee.setSales_name("公开");
		} else {
			if (!employee.getSales().equals("")) {
				String[] userPks = employee.getSales().split(",");
				String sales_name = "";

				List<UserBaseBean> users = userDao.getAllByPks(userPks);
				for (UserBaseBean user : users) {
					sales_name += user.getUser_name() + ",";
				}
				sales_name = sales_name.substring(0, sales_name.length() - 1);
				employee.setSales_name(sales_name);
			}
		}
		dao.update(employee);
		return "success";
	}

	@Override
	public List<ClientEmployeeBean> getAllClientEmployeeByPage(
			Page<ClientEmployeeBean> page) {
		return dao.getAllByPage(page);
	}

	@Override
	public List<String> getBodyPksByEmployeePks(String[] employee_pks) {
		
		return dao.getBodyPksByEmployeePks(employee_pks);
	}

}