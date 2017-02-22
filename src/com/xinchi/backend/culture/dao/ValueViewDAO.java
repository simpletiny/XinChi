package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.ValueViewBean;
import com.xinchi.tools.Page;

public interface ValueViewDAO {

	public void insert(ValueViewBean view);

	public List<ValueViewBean> getAllViewsByPage(Page page);

	public ValueViewBean selectByPk(String view_pk);

	public void update(ValueViewBean view);

	public void delete(String view_pk);

}
