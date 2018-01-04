package com.xinchi.backend.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.backend.order.service.OrderNameListService;
import com.xinchi.bean.SaleOrderNameListBean;

@Service
@Transactional
public class OrderNameListServiceImpl implements OrderNameListService {

	@Autowired
	private OrderNameListDAO dao;

	@Override
	public String saveNameList(List<SaleOrderNameListBean> names) {
		for (SaleOrderNameListBean bean : names) {
			dao.insert(bean);
		}
		return SUCCESS;
	}

	@Override
	public List<SaleOrderNameListBean> selectByTeamNumber(String team_number) {
		return dao.selectByTeamNumber(team_number);
	}

	@Override
	public List<SaleOrderNameListBean> selectByOrderPk(String order_pk) {
		return dao.selectByOrderPk(order_pk);
	}

}