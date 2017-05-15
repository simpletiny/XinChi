package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.ProductViewBean;
import com.xinchi.tools.Page;

public interface ProductViewService {

	public void insert(ProductViewBean view);

	public List<ProductViewBean> getAllViewsByPage(Page page);

	public ProductViewBean selectViewByPk(String view_pk);

	public void update(ProductViewBean view);

	public void delete(String view_pk);

}
