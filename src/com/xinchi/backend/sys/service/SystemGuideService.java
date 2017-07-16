package com.xinchi.backend.sys.service;

import java.util.List;

import com.xinchi.bean.SystemGuideBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "系统使用手册")
public interface SystemGuideService {
	@LogDescription(des = "新建文章")
	public void insert(SystemGuideBean view);

	@LogDescription(des = "搜索文章")
	public List<SystemGuideBean> getAllViewsByPage(Page page);

	@LogDescription(des = "阅读文章")
	public SystemGuideBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新文章")
	public void update(SystemGuideBean view);

	@LogDescription(des = "删除文章")
	public void delete(String view_pk);

}
