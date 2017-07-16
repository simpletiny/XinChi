package com.xinchi.backend.finance.service;

import java.util.List;

import com.xinchi.bean.CardBean;
import com.xinchi.common.LogDescription;

@LogDescription(des = "账户管理")
public interface CardService {

	/**
	 * 新增
	 * 
	 * @param bo
	 */
	@LogDescription(des = "新增账户")
	public void insert(com.xinchi.bean.CardBean bo);

	@LogDescription(des = "搜索账户")
	public List<CardBean> getAllCardsByParam(com.xinchi.bean.CardBean bo);

	@LogDescription(ignore = true)
	public List<String> getAllAccounts();

	@LogDescription(ignore = true)
	public String getAccountBalance(String account);

	@LogDescription(ignore = true)
	public String checkAccount(String account);
}
