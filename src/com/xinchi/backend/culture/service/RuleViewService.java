package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.RuleViewBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "规章制度")
public interface RuleViewService {
	@LogDescription(des = "新建规章制度")
	public void insert(RuleViewBean view);

	@LogDescription(des = "搜索规章制度")
	public List<RuleViewBean> getAllViewsByPage(Page<RuleViewBean> page);

	@LogDescription(des = "阅读规章制度")
	public RuleViewBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新规章制度")
	public void update(RuleViewBean view);

	@LogDescription(des = "删除规章制度")
	public void delete(String view_pk);

}
