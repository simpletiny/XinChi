package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.SaleAcademyViewBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "销售学院")
public interface SaleAcademyViewService {

	@LogDescription(des = "新建文章")
	public void insert(SaleAcademyViewBean view);

	@LogDescription(des = "搜索销售学院")
	public List<SaleAcademyViewBean> getAllViewsByPage(Page<SaleAcademyViewBean> page);

	@LogDescription(des = "阅读文章")
	public SaleAcademyViewBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新文章")
	public void update(SaleAcademyViewBean view);

	@LogDescription(des = "删除文章")
	public void delete(String view_pk);

}
