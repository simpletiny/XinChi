package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.ProductRuleViewBean;
import com.xinchi.tools.Page;

public interface ProductRuleViewDAO {

	public void insert(ProductRuleViewBean view);

	public List<ProductRuleViewBean> getAllViewsByPage(Page<ProductRuleViewBean> page);

	public ProductRuleViewBean selectByPk(String view_pk);

	public void update(ProductRuleViewBean view);

	public void delete(String view_pk);

}
