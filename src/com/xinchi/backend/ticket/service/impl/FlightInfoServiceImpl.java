package com.xinchi.backend.ticket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.ticket.dao.FlightInfoDAO;
import com.xinchi.backend.ticket.service.FlightInfoService;
import com.xinchi.bean.FlightInfoBean;

@Service
@Transactional
public class FlightInfoServiceImpl implements FlightInfoService {

	@Autowired
	private FlightInfoDAO dao;

	@Override
	public void insert(FlightInfoBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(FlightInfoBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public FlightInfoBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<FlightInfoBean> selectByParam(FlightInfoBean bean) {
		return dao.selectByParam(bean);
	}

}