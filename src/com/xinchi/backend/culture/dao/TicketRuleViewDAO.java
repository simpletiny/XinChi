package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.TicketRuleViewBean;
import com.xinchi.tools.Page;

public interface TicketRuleViewDAO {

	public void insert(TicketRuleViewBean view);

	public List<TicketRuleViewBean> getAllViewsByPage(Page<TicketRuleViewBean> page);

	public TicketRuleViewBean selectByPk(String view_pk);

	public void update(TicketRuleViewBean view);

	public void delete(String view_pk);

}
