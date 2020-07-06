package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ProductNeedDto;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface ProductOrderService extends BaseService {

	public List<ProductNeedDto> selectNeedByPage(Page<ProductNeedDto> page);

	public String createProductOrder(String json);

	public List<ProductOrderBean> selectByPage(Page page);

	public List<OrderDto> searchSaleOrderByProductOrderNumber(String order_number);

	public List<SaleOrderNameListBean> searchSaleOrderNameListByProductOrderNumber(String order_number);

}