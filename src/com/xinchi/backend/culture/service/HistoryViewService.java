package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.HistoryViewBean;
import com.xinchi.tools.Page;

public interface HistoryViewService {
	public void insert(HistoryViewBean view);

	public List<HistoryViewBean> getAllViewsByPage(Page<HistoryViewBean> page);

	public HistoryViewBean selectViewByPk(String view_pk);

	public void update(HistoryViewBean view);

	public void delete(String view_pk);

}
