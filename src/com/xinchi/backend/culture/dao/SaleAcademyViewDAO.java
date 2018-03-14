package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.SaleAcademyViewBean;
import com.xinchi.tools.Page;

public interface SaleAcademyViewDAO {

	public void insert(SaleAcademyViewBean view);

	public List<SaleAcademyViewBean> getAllViewsByPage(Page<SaleAcademyViewBean> page);

	public SaleAcademyViewBean selectByPk(String view_pk);

	public void update(SaleAcademyViewBean view);

	public void delete(String view_pk);

}
