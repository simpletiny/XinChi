package com.xinchi.backend.payable.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.payable.dao.AirTicketPaidDetailDAO;
import com.xinchi.backend.payable.service.AirTicketPaidDetailService;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPaidDto;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AirTicketPaidDetailServiceImpl implements AirTicketPaidDetailService {

	@Autowired
	private AirTicketPaidDetailDAO dao;

	@Override
	public void insert(AirTicketPaidDetailBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(AirTicketPaidDetailBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public AirTicketPaidDetailBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<AirTicketPaidDetailBean> selectByParam(AirTicketPaidDetailBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<AirTicketPaidDto> selectByPage(Page<AirTicketPaidDto> page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<AirTicketPaidDetailBean> selectByRelatedPk(String related_pk) {
		return dao.selectByRelatedPk(related_pk);
	}

}