package com.xinchi.backend.ticket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.ticket.dao.AirTicketNeedDAO;
import com.xinchi.backend.ticket.service.AirTicketNeedService;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.OrderAirInfoBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AirTicketNeedServiceImpl implements AirTicketNeedService {

	@Autowired
	private AirTicketNeedDAO dao;

	@Override
	public List<AirTicketNeedBean> selectByPage(Page<AirTicketNeedBean> page) {

		return dao.selectByPage(page);
	}

	@Override
	public List<AirTicketNeedBean> selectOrderByPage(Page page) {
		return dao.selectOrderByPage(page);
	}

	@Override
	public List<OrderAirInfoBean> selectOrderAirInfoByTeamNumber(String team_number) {

		return dao.selectOrderAirInfoByTeamNumber(team_number);
	}

	@Override
	public List<OrderAirInfoBean> selectOrderAirInfoByProductOrderNumber(String order_number) {

		return dao.selectOrderAirInfoByProductOrderNumber(order_number);
	}

	@Override
	public AirTicketNeedBean selectByPk(String pk) {
		return dao.selectByPk(pk);
	}

	@Override
	public List<AirTicketNeedBean> selectByPks(List<String> pks) {
		return dao.selectByPks(pks);
	}

	@Override
	public List<OrderAirInfoBean> selectOrderAirInfoByNeedPk(String need_pk) {
		return dao.selectOrderAirInfoByNeedPk(need_pk);
	}

	@Autowired
	private OrderDAO orderDao;

	@Override
	public String deleteTicketNeeds(List<String> need_pks) {
		for (String pk : need_pks) {
			AirTicketNeedBean atn = dao.selectByPk(pk);
			String team_number = atn.getProduct_order_number();
			if (team_number.startsWith("N")) {
				OrderDto order = orderDao.selectByTeamNumber(team_number);
				if (order.getCancel_flg().equals("N")) {
					return team_number + "未取消订单，不能删除票务需求！";
				}
			}else {
				return "只能删除单机票订单！";
			}
		}
		for (String pk : need_pks) {
			dao.delete(pk);
		}
		return SUCCESS;
	}
}
