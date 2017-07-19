package com.xinchi.backend.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.bean.OrderDto;
import com.xinchi.tools.Page;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO dao;

	@Override
	public List<OrderDto> selectTbcByPage(Page<OrderDto> page) {
		return dao.selectTbcByPage(page);
	}

	@Override
	public List<OrderDto> selectCByPage(Page<OrderDto> page) {

		return dao.selectCByPage(page);
	}

}
