package com.xinchi.backend.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.backend.order.service.OrderNameListService;
import com.xinchi.backend.ticket.dao.AirNeedTeamNumberDAO;
import com.xinchi.bean.AirNeedTeamNumberBean;
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

	@Autowired
	private AirNeedTeamNumberDAO airNeedTeamNumberDao;

	@Override
	public List<SaleOrderNameListBean> selectByAirNeedPk(String need_pk) {

		List<AirNeedTeamNumberBean> atns = airNeedTeamNumberDao.selectByNeedPk(need_pk);
		List<String> team_numbers = new ArrayList<String>();

		for (AirNeedTeamNumberBean atn : atns) {
			team_numbers.add(atn.getTeam_number());
		}

		if (team_numbers.size() == 0)
			return null;

		return dao.selectByTeamNumbers(team_numbers);

	}

	@Override
	public List<SaleOrderNameListBean> selectByTeamNumbers(List<String> t_ns) {
		return dao.selectByTeamNumbers(t_ns);
	}

}