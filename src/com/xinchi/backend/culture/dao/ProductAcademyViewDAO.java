package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.ProductAcademyViewBean;
import com.xinchi.tools.Page;

public interface ProductAcademyViewDAO {

	public void insert(ProductAcademyViewBean view);

	public List<ProductAcademyViewBean> getAllViewsByPage(Page<ProductAcademyViewBean> page);

	public ProductAcademyViewBean selectByPk(String view_pk);

	public void update(ProductAcademyViewBean view);

	public void delete(String view_pk);

}
