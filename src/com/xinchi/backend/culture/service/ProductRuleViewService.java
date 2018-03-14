package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.ProductRuleViewBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "产品制度")
public interface ProductRuleViewService {
	@LogDescription(des = "新建产品制度")
	public void insert(ProductRuleViewBean view);

	@LogDescription(des = "搜索产品制度")
	public List<ProductRuleViewBean> getAllViewsByPage(Page<ProductRuleViewBean> page);

	@LogDescription(des = "阅读产品制度")
	public ProductRuleViewBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新产品制度")
	public void update(ProductRuleViewBean view);

	@LogDescription(des = "删除产品制度")
	public void delete(String view_pk);

}
