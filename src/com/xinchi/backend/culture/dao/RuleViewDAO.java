package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.RuleViewBean;
import com.xinchi.tools.Page;

public interface RuleViewDAO {

	public void insert(RuleViewBean view);

	public List<RuleViewBean> getAllViewsByPage(Page page);

	public RuleViewBean selectByPk(String view_pk);

	public void update(RuleViewBean view);

	public void delete(String view_pk);

}
