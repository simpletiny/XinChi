package com.xinchi.backend.finance.service.impl;

import java.util.List;

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

	@Override
	public List<CardBean> getAllCardsByParam(CardBean bo) {
		return dao.getAllByParam(bo);
	}

	@Override
	public List<String> getAllAccounts() {
		return dao.getAllAccounts();
	}

	@Override
	public String getAccountBalance(String account) {
		return dao.getAccountBalance(account);
	}

	@Override
	public String checkAccount(String account) {
		CardBean card = dao.getCardByAccount(account);
		if (card != null) {
			return "exist";
		} else {
			return "ok";
		}
	}

	@Override
	public CardBean selectByPk(String pk) {
		return dao.selectByPk(pk);
	}

	@Override
	public void update(CardBean bean) {
		dao.update(bean);
	}

	@Override
	public List<CardBean> selectByPurpose(String purpose) {
		return dao.selectByPurpose(purpose);
	}

	@Override
	public CardBean selectByAccount(String account) {
		return dao.getCardByAccount(account);
	}

}
