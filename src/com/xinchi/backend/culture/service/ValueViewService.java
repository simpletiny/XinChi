package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.ValueViewBean;
import com.xinchi.tools.Page;

public interface ValueViewService {

	public void insert(ValueViewBean view);

	public List<ValueViewBean> getAllViewsByPage(Page page);

	public ValueViewBean selectViewByPk(String view_pk);

	public void update(ValueViewBean view);

	public void delete(String view_pk);

}
