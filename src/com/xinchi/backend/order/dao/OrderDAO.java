package com.xinchi.backend.order.dao;

import java.util.List;

import com.xinchi.bean.OrderDto;
import com.xinchi.tools.Page;

public interface OrderDAO {

	public List<OrderDto> selectTbcByPage(Page<OrderDto> page);

	public List<OrderDto> selectCByPage(Page<OrderDto> page);

	public OrderDto selectByTeamNumber(String team_number);

	public List<OrderDto> selectFByPage(Page<OrderDto> page);

	public List<OrderDto> selectTbcByParam(OrderDto option);
}