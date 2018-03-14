package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.TicketRuleViewBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "票务制度")
public interface TicketRuleViewService {
	@LogDescription(des = "新建票务制度")
	public void insert(TicketRuleViewBean view);

	@LogDescription(des = "搜索票务制度")
	public List<TicketRuleViewBean> getAllViewsByPage(Page<TicketRuleViewBean> page);

	@LogDescription(des = "阅读票务制度")
	public TicketRuleViewBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新票务制度")
	public void update(TicketRuleViewBean view);

	@LogDescription(des = "删除票务制度")
	public void delete(String view_pk);

}
