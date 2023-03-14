package com.xinchi.backend.product.dao;

import java.util.List;

import com.xinchi.bean.ProductProfitBean;

public interface ProductProfitDAO {

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<ProductProfitBean> selectByParam(ProductProfitBean bean);

}