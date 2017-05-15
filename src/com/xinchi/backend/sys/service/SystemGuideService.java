package com.xinchi.backend.sys.service;

import java.util.List;

import com.xinchi.bean.SystemGuideBean;
import com.xinchi.tools.Page;

public interface SystemGuideService {

	public void insert(SystemGuideBean view);

	public List<SystemGuideBean> getAllViewsByPage(Page page);

	public SystemGuideBean selectViewByPk(String view_pk);

	public void update(SystemGuideBean view);

	public void delete(String view_pk);

}
