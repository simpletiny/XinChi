package com.xinchi.backend.order.dao;

import java.util.List;

import com.xinchi.bean.OrderDto;
import com.xinchi.bean.SaleScoreDto;
import com.xinchi.tools.Page;

public interface OrderDAO {

	public List<OrderDto> selectTbcByPage(Page<OrderDto> page);

	public List<OrderDto> selectCByPage(Page<OrderDto> page);

	public OrderDto selectByTeamNumber(String team_number);

	public List<OrderDto> selectFByPage(Page<OrderDto> page);

	public List<OrderDto> selectTbcByParam(OrderDto option);

	public OrderDto searchOrderByPk(String order_pk);

	public List<SaleScoreDto> searchSaleScore(Page<SaleScoreDto> page);

	public List<SaleScoreDto> searchSaleScoreByParam(SaleScoreDto ssd);

	public List<OrderDto> selectByParam(OrderDto order);

	public List<OrderDto> selectWithProductByParam(OrderDto order);

	public String selectMaxConfirmDateByEmployeePk(String employee_pk);

	public List<SaleScoreDto> searchBackMoneyScoreByPage(Page<SaleScoreDto> page);

	public List<OrderDto> selectConfirmingOrders(OrderDto orderOption);

	public List<OrderDto> selectByTeamNumbers(List<String> team_numbers);

	public List<SaleScoreDto> search3MonthScoreByUserNumber(String user_number);

	public List<OrderDto> selectPayableInfoByParam(OrderDto option);

	public OrderDto selectFinalOrderByTeamNumber(String team_number);
}