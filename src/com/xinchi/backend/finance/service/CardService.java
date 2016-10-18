package com.xinchi.backend.finance.service;

import java.util.List;

import com.xinchi.bean.CardBean;

public interface CardService {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(com.xinchi.bean.CardBean bo);

	public List<CardBean> getAllCardsByParam(com.xinchi.bean.CardBean bo);

	public List<String> getAllAccounts();

	public String getAccountBalance(String account);

	public String checkAccount(String account);
}
