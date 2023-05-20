package com.xinchi.backend.receivable.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.receivable.dao.AirReceivedDAO;
import com.xinchi.backend.receivable.service.AirReceivedService;
import com.xinchi.bean.AirReceivedDetailBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AirReceivedServiceImpl implements AirReceivedService {

	@Autowired
	private AirReceivedDAO dao;

	@Override
	public void insert(AirReceivedDetailBean detail) {
		dao.insert(detail);
	}

	@Override
	public List<AirReceivedDetailBean> selectByPage(Page<AirReceivedDetailBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public String rollBackReceived(String received_pks) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(AirReceivedDetailBean detail) {
		dao.update(detail);
	}

	@Override
	public AirReceivedDetailBean selectByPk(String detail_pk) {
		return dao.selectByPk(detail_pk);
	}

	@Override
	public List<AirReceivedDetailBean> selectByParam(AirReceivedDetailBean option) {
		return dao.selectByParam(option);
	}

}
