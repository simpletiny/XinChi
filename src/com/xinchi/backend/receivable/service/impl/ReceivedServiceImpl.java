package com.xinchi.backend.receivable.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.ClientReceivedDetailBean;

@Service
@Transactional
public class ReceivedServiceImpl implements ReceivedService {
	
	@Autowired
	private ReceivedDAO dao;
	
	@Override
	public void insert(ClientReceivedDetailBean detail) {
		
		dao.insert(detail);
	}

}
