package com.xinchi.backend.receivable.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ReceivedServiceImpl implements ReceivedService {

	@Autowired
	private ReceivedDAO dao;

	@Override
	public void insert(ClientReceivedDetailBean detail) {

		dao.insert(detail);
	}

	@Override
	public void insertWithPk(ClientReceivedDetailBean detail) {

		dao.insertWithPk(detail);
	}

	@Override
	public List<ClientReceivedDetailBean> getAllReceivedsByPage(Page page) {
		return dao.getAllByPage(page);
	}
}
