package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.TopAcademyViewBean;
import com.xinchi.tools.Page;

public interface TopAcademyViewDAO {

	public void insert(TopAcademyViewBean view);

	public List<TopAcademyViewBean> getAllViewsByPage(Page<TopAcademyViewBean> page);

	public TopAcademyViewBean selectByPk(String view_pk);

	public void update(TopAcademyViewBean view);

	public void delete(String view_pk);

}
