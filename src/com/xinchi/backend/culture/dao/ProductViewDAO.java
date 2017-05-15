package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.ProductViewBean;
import com.xinchi.tools.Page;

public interface ProductViewDAO {

	public void insert(ProductViewBean view);

	public List<ProductViewBean> getAllViewsByPage(Page page);

	public ProductViewBean selectByPk(String view_pk);

	public void update(ProductViewBean view);

	public void delete(String view_pk);

}
