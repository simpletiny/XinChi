package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.ValueViewBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "欣驰价值观")
public interface ValueViewService {
	@LogDescription(des = "新建文章")
	public void insert(ValueViewBean view);

	@LogDescription(des = "搜索欣驰价值观")
	public List<ValueViewBean> getAllViewsByPage(Page page);

	@LogDescription(des = "阅读欣驰价值观")
	public ValueViewBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新欣驰价值观")
	public void update(ValueViewBean view);

	@LogDescription(des = "删除欣驰价值观")
	public void delete(String view_pk);

}
