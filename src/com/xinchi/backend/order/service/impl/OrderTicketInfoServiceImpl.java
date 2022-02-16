package com.xinchi.backend.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderTicketInfoDAO;
import com.xinchi.backend.order.service.OrderTicketInfoService;
import com.xinchi.bean.SaleOrderTicketInfoBean;

@Service
@Transactional
public class OrderTicketInfoServiceImpl implements OrderTicketInfoService {

	@Autowired
	private OrderTicketInfoDAO dao;

	@Override
	public void insert(SaleOrderTicketInfoBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(SaleOrderTicketInfoBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public SaleOrderTicketInfoBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<SaleOrderTicketInfoBean> selectByParam(SaleOrderTicketInfoBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<SaleOrderTicketInfoBean> selectByOrderPk(String order_pk) {
		return dao.selectByOrderPk(order_pk);
	}

}