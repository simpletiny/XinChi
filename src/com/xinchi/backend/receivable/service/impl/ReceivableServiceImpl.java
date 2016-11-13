package com.xinchi.backend.receivable.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.bean.ReceivableBean;

@Service
@Transactional
public class ReceivableServiceImpl implements ReceivableService {
	@Autowired
	private ReceivableDAO dao;

	@Override
	public void insert(ReceivableBean receivable) {
		dao.insert(receivable);
	}

}
