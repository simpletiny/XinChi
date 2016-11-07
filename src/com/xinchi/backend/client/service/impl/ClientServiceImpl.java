package com.xinchi.backend.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientDAO;
import com.xinchi.backend.client.service.ClientService;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.ClientBean;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.tools.Page;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientDAO dao;
	@Autowired
	private UserDAO userDao;

	@Override
	@Transactional
	public String createCompany(ClientBean client) {
		if (client.getPublic_flg().equals("Y")) {
			client.setSales("");
			client.setSales_name("公开");
		} else {
			if (!client.getSales().equals("")) {
				String[] userPks = client.getSales().split(",");
				String sales_name = "";

				List<UserBaseBean> users = userDao.getAllByPks(userPks);
				for (UserBaseBean user : users) {
					sales_name += user.getUser_name() + ",";
				}
				sales_name = sales_name.substring(0, sales_name.length() - 1);
				client.setSales_name(sales_name);
			}
		}

		dao.insert(client);
		return "success";
	}

	@Override
	public void insert(com.xinchi.bean.ClientBean bo) {
		dao.insert(bo);
	}

	@Override
	@Transactional
	public void update(ClientBean client) {
		dao.update(client);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public com.xinchi.bean.ClientBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<com.xinchi.bean.ClientBean> getAllCompaniesByParam(
			com.xinchi.bean.ClientBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	public String updateCompany(ClientBean client) {
		if (client.getPublic_flg().equals("Y")) {
			client.setSales("");
			client.setSales_name("公开");
		} else {
			if (!client.getSales().equals("")) {
				String[] userPks = client.getSales().split(",");
				String sales_name = "";

				List<UserBaseBean> users = userDao.getAllByPks(userPks);
				for (UserBaseBean user : users) {
					sales_name += user.getUser_name() + ",";
				}
				sales_name = sales_name.substring(0, sales_name.length() - 1);
				client.setSales_name(sales_name);
			}
		}
		dao.update(client);
		return "success";
	}

	@Override
	public List<ClientBean> getAllCompaniesByPage(Page<ClientBean> page) {
		return dao.getAllCompaniesByPage(page);
	}

}