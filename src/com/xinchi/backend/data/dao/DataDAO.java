package com.xinchi.backend.data.dao;

import java.util.List;

import com.xinchi.bean.ProductAreaBean;
import com.xinchi.bean.ProductProductBean;
import com.xinchi.bean.ProductSaleBean;

public interface DataDAO {

	List<ProductAreaBean> selectProductAreaData(ProductAreaBean productOption);

	List<ProductProductBean> selectProductProductData(ProductAreaBean productOption);

	List<ProductSaleBean> selectProductSaleData(ProductAreaBean productOption);

}
