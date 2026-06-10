package com.xinchi.backend.ticket.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.service.AirTicketNameListService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.PassengerAllotDto;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
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

	@Autowired
	private OrderNameListDAO saleOrderNameListDao;

	private final static String NO_NEED_UNLOCK = "销售已删除名单！";

	@Override
	public String toggleLockName(List<String> passenger_pks, String lock_flg) {
		List<AirTicketNameListBean> names = dao.selectByPks(passenger_pks.toArray(new String[0]));
		for (AirTicketNameListBean n : names) {
			if (n.getDelete_flg().equals("Y"))
				return NO_NEED_UNLOCK;
			// 更新票务名单锁定状态
			n.setLock_flg(lock_flg);
			dao.update(n);
			// 更新销售名单锁定状态
			SaleOrderNameListBean sale_name = saleOrderNameListDao.selectByPrimaryKey(n.getBase_pk());
			sale_name.setLock_flg(SimpletinyString.replaceCharFromRight(sale_name.getLock_flg(), lock_flg, 1));
			saleOrderNameListDao.update(sale_name);
		}

		return SUCCESS;
	}

	@Autowired
	private OrderDAO orderDao;

	@Override
	public String deletePassengerByPassengerPks(List<String> passenger_pks) {
		List<AirTicketNameListBean> names = dao.selectByPks(passenger_pks.toArray(new String[0]));

		// 订单状况
		Set<String> team_numbers = new HashSet<>();
		for (AirTicketNameListBean name : names) {
			team_numbers.add(name.getTeam_number());
		}
		Map<String, String> orderCancel = new HashMap<>();
		for (String team_number : team_numbers) {
			OrderDto order = orderDao.selectByTeamNumber(team_number);
			if (order.getConfirm_flg().equals("Y")) {
				orderCancel.put(team_number, "N");
			} else if (order.getConfirm_flg().equals("F")) {
				OrderDto finalOrder = orderDao.selectFinalOrderByTeamNumber(team_number);
				if (finalOrder.getFinal_type().equals(ResourcesConstants.FINAL_TYPE_CANCEL)) {
					orderCancel.put(team_number, "Y");
				} else {
					orderCancel.put(team_number, "N");
				}
			}
		}

		for (AirTicketNameListBean name : names) {
			if (name.getDelete_flg().equals("N") && orderCancel.get(name.getTeam_number()).equals("N")) {
				return "乘客" + name.getName() + "并未退团" + ",不能删除名单！";
			}
		}
		for (AirTicketNameListBean name : names) {
			dao.delete(name.getPk());
		}
		return SUCCESS;
	}

	@Override
	public List<AirTicketNameListBean> selectWithInfoByTeamNumbers(List<String> team_numbers) {
		return dao.selectWithInfoByTeamNumbers(team_numbers);
	}

	@Override
	public List<AirTicketNameListBean> selectTicketCostInfo(AirTicketNameListBean passenger) {
		return dao.selectTicketCostInfo(passenger);
	}
}