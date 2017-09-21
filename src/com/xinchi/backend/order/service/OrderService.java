package com.xinchi.backend.order.service;

import java.util.List;

import com.xinchi.bean.OrderDto;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface OrderService extends BaseService {

	public List<OrderDto> selectTbcByPage(Page<OrderDto> page);
	public List<OrderDto> selectCByPage(Page<OrderDto> page);
	
	public OrderDto selectByTeamNumber(String teamNumber);
	
}
