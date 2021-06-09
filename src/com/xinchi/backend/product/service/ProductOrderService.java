package com.xinchi.backend.product.service;

import java.util.List;

import com.xinchi.bean.AirTicketNameListBean;
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

	public String rollBackOrder(String order_number);

	public String changeOrderLock(String team_number, String lock_flg);

	public String isAllOrdersLocked(String order_number);

	public ProductOrderBean selectByOrderNumber(String order_number);

	public List<OrderDto> searchSaleOrderInfoByProductOrderInfo(String order_number, String supplier_employee_pk);

	public List<AirTicketNameListBean> searchTicketInfoByOrderNumber(String order_number);

}