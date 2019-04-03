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

	public OrderDto searchCOrderByPk(String order_pk);

	public List<SaleScoreDto> searchSaleScore(Page<SaleScoreDto> page);

	public List<SaleScoreDto> searchSaleScoreByParam(SaleScoreDto ssd);

	public List<OrderDto> selectByParam(OrderDto order);

	String selectMaxConfirmDateByEmployeePk(String employee_pk);

	public List<SaleScoreDto> searchBackMoneyScoreByPage(Page<SaleScoreDto> page);
}