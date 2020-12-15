package com.xinchi.backend.ticket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.service.AirTicketNameListService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.PassengerAllotDto;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AirTicketNameListServiceImpl implements AirTicketNameListService {

	@Autowired
	private AirTicketNameListDAO dao;

	@Override
	public void insert(AirTicketNameListBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(AirTicketNameListBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public AirTicketNameListBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<AirTicketNameListBean> selectByParam(AirTicketNameListBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public String insertList(List<AirTicketNameListBean> airTicketNameList) {
		for (AirTicketNameListBean name : airTicketNameList) {
			dao.insert(name);
		}
		return SUCCESS;
	}

	@Override
	public List<AirTicketNameListBean> selectByPage(Page<AirTicketNameListBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<AirTicketNameListBean> selectByPks(String[] pks) {
		return dao.selectByPks(pks);
	}

	@Override
	public List<PassengerAllotDto> selectPassengerAllotByPassengerPks(List<String> pks) {
		return dao.selectByPassengerPks(pks);
	}

	@Override
	public List<AirTicketNameListBean> selectByOrderNumber(String order_number) {
		if (order_number.startsWith("N")) {
			return dao.selectByTeamNumber(order_number);
		} else if (order_number.startsWith("A")) {
			return dao.selectByOrderNumber(order_number);
		}
		return null;
	}

	@Override
	public List<AirTicketNameListBean> selectDoneByPage(Page<AirTicketNameListBean> page) {
		return dao.selectDoneByPage(page);
	}

	@Override
	public List<AirTicketNameListBean> selectByChangePk(String ticket_change_pk) {

		return dao.selectByChangePk(ticket_change_pk);
	}

}