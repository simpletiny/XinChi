package com.xinchi.backend.order.service;

import java.util.List;

import com.xinchi.bean.OrderDto;
import com.xinchi.bean.SaleScoreDto;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface OrderService extends BaseService {

	public List<OrderDto> selectTbcByPage(Page<OrderDto> page);

	public List<OrderDto> selectCByPage(Page<OrderDto> page);

	public OrderDto selectByTeamNumber(String teamNumber);

	public List<OrderDto> selectFByPage(Page<OrderDto> page);

	public List<OrderDto> selectTbcByParam(OrderDto option);

	public OrderDto searchOrderByPk(String order_pk);

	public String finalOrder(OrderDto order);

	public String rollBackFinalOrder(String order_pk, String standard_flg);

	public String cancelOrder(OrderDto order);

	public List<SaleScoreDto> searchSaleScoreByPage(Page<SaleScoreDto> page);

	public List<SaleScoreDto> searchBackMoneyScoreByPage(Page<SaleScoreDto> page);

	public List<SaleScoreDto> searchSaleScoreByParam(SaleScoreDto ssd);

	public List<OrderDto> selectByParam(OrderDto order);

	public List<OrderDto> selectWithProductByParam(OrderDto order);

	public List<OrderDto> selectConfirmingOrders();

	public String confirmNameList(String team_number);

	public String rollBackConfirmNameList(String team_number);

	public List<OrderDto> selectByTeamNumbers(List<String> asList);

	public List<SaleScoreDto> search3MonthScoreByUserNumber(String user_number);

	public List<OrderDto> searchOrderByReceivedRelatedPk(String related_pk);

	public String createReceivable(String order_pk);

	public String checkCanBeEdit(String order_pk);

	public OrderDto selectFinalOrderByTeamNumber(String team_number);

}
