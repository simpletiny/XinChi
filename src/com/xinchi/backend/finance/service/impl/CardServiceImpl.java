package com.xinchi.backend.finance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.finance.service.CardService;
import com.xinchi.bean.CardBean;

@Service
public class CardServiceImpl implements CardService {
	@Autowired
	private CardDAO dao;

	@Override
	@Transactional
	public void insert(CardBean bo) {
		bo.setBalance(bo.getInit_money());
		dao.insert(bo);
	}

}
