package com.xinchi.backend.product.service;

import java.util.List;
import java.util.Map;

import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ProductNeedDto;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.bean.ProductOrderNameBean;
import com.xinchi.bean.ProductOrderNameFlightSegmentBean;
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

	public List<ProductOrderBean> selectByParam(ProductOrderBean option);

	public List<ProductOrderNameBean> searchProductOrderNameByPage(Page<ProductOrderNameBean> page);

	public String sendAirTicketNeed(String json);

	public Map<String, List<ProductOrderNameFlightSegmentBean>> searchAirNeedByNamePk(String name_pk);

	public List<ProductOrderNameBean> selectProductOrderNameByAirNeedPk(String air_need_pk);

	public String deleteSendedAirNeed(String air_need_pk);

	public List<SaleOrderNameListBean> searchSaleOrderNameListByProductOrderNumbers(List<String> order_numbers);

}