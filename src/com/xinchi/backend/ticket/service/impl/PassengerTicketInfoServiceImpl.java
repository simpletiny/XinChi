package com.xinchi.backend.ticket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.ticket.dao.PassengerTicketInfoDAO;
import com.xinchi.backend.ticket.service.PassengerTicketInfoService;
import com.xinchi.bean.PassengerTicketInfoBean;

@Service
@Transactional
public class PassengerTicketInfoServiceImpl implements PassengerTicketInfoService {

	@Autowired
	private PassengerTicketInfoDAO dao;

	@Override
	public void insert(PassengerTicketInfoBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(PassengerTicketInfoBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public PassengerTicketInfoBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<PassengerTicketInfoBean> selectByParam(PassengerTicketInfoBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public String insertList(List<PassengerTicketInfoBean> ptis) {
		for (PassengerTicketInfoBean pti : ptis) {
			dao.insert(pti);
		}
		return SUCCESS;
	}

}