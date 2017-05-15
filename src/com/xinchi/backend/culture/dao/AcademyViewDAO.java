package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.AcademyViewBean;
import com.xinchi.tools.Page;

public interface AcademyViewDAO {

	public void insert(AcademyViewBean view);

	public List<AcademyViewBean> getAllViewsByPage(Page page);

	public AcademyViewBean selectByPk(String view_pk);

	public void update(AcademyViewBean view);

	public void delete(String view_pk);

}
