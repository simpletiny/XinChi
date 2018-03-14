package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.SaleRuleViewBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "销售制度")
public interface SaleRuleViewService {
	@LogDescription(des = "新建销售制度")
	public void insert(SaleRuleViewBean view);

	@LogDescription(des = "搜索销售制度")
	public List<SaleRuleViewBean> getAllViewsByPage(Page<SaleRuleViewBean> page);

	@LogDescription(des = "阅读销售制度")
	public SaleRuleViewBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新销售制度")
	public void update(SaleRuleViewBean view);

	@LogDescription(des = "删除销售制度")
	public void delete(String view_pk);

}
