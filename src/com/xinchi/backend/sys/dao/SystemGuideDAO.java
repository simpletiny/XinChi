package com.xinchi.backend.sys.dao;

import java.util.List;

import com.xinchi.bean.SystemGuideBean;
import com.xinchi.tools.Page;

public interface SystemGuideDAO {

	public void insert(SystemGuideBean view);

	public List<SystemGuideBean> getAllViewsByPage(Page page);

	public SystemGuideBean selectByPk(String view_pk);

	public void update(SystemGuideBean view);

	public void delete(String view_pk);

}
