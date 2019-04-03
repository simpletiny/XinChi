package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.ProductResearchViewBean;
import com.xinchi.tools.Page;

public interface ProductResearchViewDAO {

	public void insert(ProductResearchViewBean view);

	public List<ProductResearchViewBean> getAllViewsByPage(Page<ProductResearchViewBean> page);

	public ProductResearchViewBean selectByPk(String view_pk);

	public void update(ProductResearchViewBean view);

	public void delete(String view_pk);

}
