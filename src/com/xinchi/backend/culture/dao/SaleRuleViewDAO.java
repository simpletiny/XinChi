package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.SaleRuleViewBean;
import com.xinchi.tools.Page;

public interface SaleRuleViewDAO {

	public void insert(SaleRuleViewBean view);

	public List<SaleRuleViewBean> getAllViewsByPage(Page<SaleRuleViewBean> page);

	public SaleRuleViewBean selectByPk(String view_pk);

	public void update(SaleRuleViewBean view);

	public void delete(String view_pk);

}
