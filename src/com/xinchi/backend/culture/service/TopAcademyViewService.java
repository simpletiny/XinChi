package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.TopAcademyViewBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "高层学院")
public interface TopAcademyViewService {

	@LogDescription(des = "新建高层课程")
	public void insert(TopAcademyViewBean view);

	@LogDescription(des = "搜索高层学院")
	public List<TopAcademyViewBean> getAllViewsByPage(Page<TopAcademyViewBean> page);

	@LogDescription(des = "阅读高层课程")
	public TopAcademyViewBean selectViewByPk(String view_pk);

	@LogDescription(des = "更新高层课程")
	public void update(TopAcademyViewBean view);

	@LogDescription(des = "删除高层课程")
	public void delete(String view_pk);

}
