package com.xinchi.backend.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientDAO;
import com.xinchi.backend.client.service.ClientService;
import com.xinchi.bean.ClientBean;
import com.xinchi.tools.Page;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientDAO dao;

	@Override
	@Transactional
	public String createCompany(ClientBean client) {
		dao.insert(client);
		return "success";
	}

	@Override
	public void insert(com.xinchi.bean.ClientBean bo) {
		dao.insert(bo);
	}

	@Override
	@Transactional
	public void update(com.xinchi.bean.ClientBean bo) {
		dao.update(bo);
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
		dao.update(client);
		return "success";
	}

	@Override
	public List<ClientBean> getAllCompaniesByPage(Page<ClientBean> page) {
		return dao.getAllCompaniesByPage(page);
	}

}