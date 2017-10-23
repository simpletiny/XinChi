package com.xinchi.backend.payable.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.payable.dao.AirTicketPayableDAO;
import com.xinchi.backend.payable.service.AirTicketPayableService;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AirTicketPayableServiceImpl implements AirTicketPayableService {

	@Autowired
	private AirTicketPayableDAO dao;

	@Override
	public void insert(AirTicketPayableBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(AirTicketPayableBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public AirTicketPayableBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<AirTicketPayableBean> selectByParam(AirTicketPayableBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<AirTicketPayableBean> selectByPage(Page<AirTicketPayableBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<AirTicketPayableBean> selectByPks(List<String> pks) {
		return dao.selectByPks(pks);
	}

}