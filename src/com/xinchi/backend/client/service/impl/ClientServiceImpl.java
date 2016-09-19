package com.xinchi.backend.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinchi.backend.client.dao.ClientDAO;
import com.xinchi.backend.client.service.ClientService;
import com.xinchi.bean.ClientBean;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientDAO dao;

	@Override
	public String createCompany(ClientBean client) {
		dao.insert(client);
		return "success";
	}

	@Override
	public void insert(com.xinchi.bean.ClientBean bo) {
		dao.insert(bo);
	}

	@Override
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
	public List<com.xinchi.bean.ClientBean> getAllByParam(
			com.xinchi.bean.ClientBean bo) {
		return dao.getAllByParam(bo);
	}

}