package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.RuleViewBean;
import com.xinchi.tools.Page;

public interface RuleViewService {

	public void insert(RuleViewBean view);

	public List<RuleViewBean> getAllViewsByPage(Page page);

	public RuleViewBean selectViewByPk(String view_pk);

	public void update(RuleViewBean view);

	public void delete(String view_pk);

}
