package com.xinchi.backend.finance.dao;

import java.util.List;

import com.xinchi.bean.CardBean;

public interface CardDAO {
	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(com.xinchi.bean.CardBean bean);

	public List<CardBean> getAllByParam(CardBean bo);

	public List<String> getAllAccounts();

	public String getAccountBalance(String account);

	public CardBean getCardByAccount(String account);

	public void update(CardBean card);
}
