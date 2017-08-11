package com.xinchi.backend.ticket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.ticket.dao.AirTicketOrderDAO;
import com.xinchi.backend.ticket.service.AirTicketOrderService;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AirTicketOrderServiceImpl implements AirTicketOrderService{

	@Autowired
	private AirTicketOrderDAO dao;
	
	@Override
	public void insert(AirTicketOrderBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(AirTicketOrderBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public AirTicketOrderBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<AirTicketOrderBean> selectByParam(AirTicketOrderBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<AirTicketOrderBean> selectByPage(Page<AirTicketOrderBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<AirTicketOrderBean> selectByPks(List<String> airTicketOrderPks) {
		return dao.selectByPks(airTicketOrderPks);
	}
	
}