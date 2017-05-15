package com.xinchi.backend.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientDAO;
import com.xinchi.backend.client.service.ClientService;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.ClientBean;
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
		ClientBean options = new ClientBean();
		options.setSales(client.getSales());
		options.setClient_name(client.getClient_name());

		List<ClientBean> exists = dao.getAllByParam(options);
		if (exists != null && exists.size() > 0)
			return "exist";

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
	public List<com.xinchi.bean.ClientBean> getAllCompaniesByParam(com.xinchi.bean.ClientBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	public String updateCompany(ClientBean client) {
		ClientBean options = new ClientBean();
		options.setSales(client.getSales());
		options.setClient_name(client.getClient_name());

		List<ClientBean> exists = dao.getAllByParam(options);

		if (exists != null && exists.size() > 0) {
			if (exists.get(0).getPk().equals(client.getPk())) {
				dao.update(client);
				return "success";
			} else {
				return "exist";
			}
		} else {
			dao.update(client);
			return "success";
		}
	}

	@Override
	public List<ClientBean> getAllCompaniesByPage(Page<ClientBean> page) {
		return dao.getAllCompaniesByPage(page);
	}

	@Override
	public String deleteClientEmployee(List<String> company_pks) {
		dao.deleteCompanyByPks(company_pks);
		return "success";
	}

	@Override
	public String recoveryClientEmployee(List<String> company_pks) {
		dao.recoveryCompanyByPks(company_pks);
		return "success";
	}

}