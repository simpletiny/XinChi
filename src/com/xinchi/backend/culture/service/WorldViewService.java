package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.WorldViewBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "欣驰故事")
public interface WorldViewService {
	@LogDescription(des = "新建故事")
	public void insert(WorldViewBean view);

	@LogDescription(des = "搜索欣驰故事")
	public List<WorldViewBean> getAllViewsByPage(Page<WorldViewBean> page);

	@LogDescription(des = "阅读欣驰故事")
	public WorldViewBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新欣驰故事")
	public void update(WorldViewBean view);

	@LogDescription(des = "删除欣驰故事")
	public void delete(String view_pk);

}
