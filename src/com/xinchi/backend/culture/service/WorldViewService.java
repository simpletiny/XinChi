package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.WorldViewBean;
import com.xinchi.tools.Page;

public interface WorldViewService {

	public void insert(WorldViewBean view);

	public List<WorldViewBean> getAllViewsByPage(Page page);

	public WorldViewBean selectViewByPk(String view_pk);

	public void update(WorldViewBean view);

	public void delete(String view_pk);

}
