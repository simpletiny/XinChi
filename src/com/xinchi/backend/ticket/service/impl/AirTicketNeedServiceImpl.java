package com.xinchi.backend.ticket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.ticket.dao.AirTicketNeedDAO;
import com.xinchi.backend.ticket.service.AirTicketNeedService;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.OrderAirInfoBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AirTicketNeedServiceImpl implements AirTicketNeedService {

	@Autowired
	private AirTicketNeedDAO dao;

	@Override
	public List<AirTicketNeedBean> selectByPage(Page<AirTicketNeedBean> page) {

		return dao.selectByPage(page);
	}

	@Override
	public List<AirTicketNeedBean> selectOrderByPage(Page page) {
		return dao.selectOrderByPage(page);
	}

	@Override
	public List<OrderAirInfoBean> selectOrderAirInfoByTeamNumber(String team_number) {

		return dao.selectOrderAirInfoByTeamNumber(team_number);
	}

	@Override
	public List<OrderAirInfoBean> selectOrderAirInfoByProductOrderNumber(String order_number) {

		return dao.selectOrderAirInfoByProductOrderNumber(order_number);
	}

}
