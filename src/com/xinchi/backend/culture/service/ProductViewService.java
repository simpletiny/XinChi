package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.ProductViewBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "产品学院")
public interface ProductViewService {

	@LogDescription(des = "新建文章")
	public void insert(ProductViewBean view);

	@LogDescription(des = "搜索产品学院")
	public List<ProductViewBean> getAllViewsByPage(Page page);

	@LogDescription(des = "阅读文章")
	public ProductViewBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新文章")
	public void update(ProductViewBean view);

	@LogDescription(des = "删除文章")
	public void delete(String view_pk);

}
