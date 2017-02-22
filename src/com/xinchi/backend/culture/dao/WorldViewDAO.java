package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.WorldViewBean;
import com.xinchi.tools.Page;

public interface WorldViewDAO {

	public void insert(WorldViewBean view);

	public List<WorldViewBean> getAllViewsByPage(Page page);

	public WorldViewBean selectByPk(String view_pk);

	public void update(WorldViewBean view);

	public void delete(String view_pk);

}
