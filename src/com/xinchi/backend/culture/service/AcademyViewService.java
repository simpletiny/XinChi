package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.AcademyViewBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "销售学院")
public interface AcademyViewService {

	@LogDescription(des = "新建文章")
	public void insert(AcademyViewBean view);

	@LogDescription(des = "搜索销售学院")
	public List<AcademyViewBean> getAllViewsByPage(Page page);

	@LogDescription(des = "阅读文章")
	public AcademyViewBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新文章")
	public void update(AcademyViewBean view);

	@LogDescription(des = "删除文章")
	public void delete(String view_pk);

}
