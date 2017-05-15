package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.AcademyViewBean;
import com.xinchi.tools.Page;

public interface AcademyViewService {

	public void insert(AcademyViewBean view);

	public List<AcademyViewBean> getAllViewsByPage(Page page);

	public AcademyViewBean selectViewByPk(String view_pk);

	public void update(AcademyViewBean view);

	public void delete(String view_pk);

}
