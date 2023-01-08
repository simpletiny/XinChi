package com.xinchi.backend.data.service;

import java.util.List;

import com.xinchi.bean.DataFinanceSummaryDto;
import com.xinchi.bean.DataOrderCountDto;
import com.xinchi.bean.KeyValueDto;
import com.xinchi.bean.ProductAreaBean;
import com.xinchi.bean.ProductProductBean;
import com.xinchi.bean.ProductSaleBean;
import com.xinchi.common.BaseService;

public interface DataService extends BaseService {

	public List<DataOrderCountDto> fetchOrderCountData(DataOrderCountDto order_count);

	public DataFinanceSummaryDto fetchFinanceSummary() throws Exception;

	public List<ProductAreaBean> searchProductAreaData(ProductAreaBean productOption);

	public List<ProductProductBean> searchProductProductData(ProductAreaBean productOption);

	public List<ProductSaleBean> searchProductSaleData(ProductAreaBean productOption);

	public List<KeyValueDto> fetchPayableByArea(String provice);

}
