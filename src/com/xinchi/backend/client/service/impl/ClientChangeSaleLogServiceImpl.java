package com.xinchi.backend.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientChangeSaleLogDAO;
import com.xinchi.backend.client.service.ClientChangeSaleLogService;
import com.xinchi.bean.ClientChangeSaleLogBean;

@Service
@Transactional
public class ClientChangeSaleLogServiceImpl implements ClientChangeSaleLogService {

	@Autowired
	private ClientChangeSaleLogDAO dao;

	@Override
	public void insert(ClientChangeSaleLogBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(ClientChangeSaleLogBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ClientChangeSaleLogBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<ClientChangeSaleLogBean> selectByParam(ClientChangeSaleLogBean bean) {
		return dao.selectByParam(bean);
	}

}