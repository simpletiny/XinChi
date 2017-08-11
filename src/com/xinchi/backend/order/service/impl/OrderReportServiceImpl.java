package com.xinchi.backend.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderReportDAO;
import com.xinchi.backend.order.service.OrderReportService;
import com.xinchi.bean.OrderReportDto;
import com.xinchi.tools.Page;

@Service
@Transactional
public class OrderReportServiceImpl implements OrderReportService {

	@Autowired
	private OrderReportDAO dao;

	@Override
	public List<OrderReportDto> selectOrderReportByPage(Page<OrderReportDto> page) {

		return dao.selectOrderReportByPage(page);
	}

}
