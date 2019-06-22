package com.xinchi.backend.ticket.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.payable.dao.AirTicketPayableDAO;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.dao.AirTicketOrderDAO;
import com.xinchi.backend.ticket.dao.PassengerTicketInfoDAO;
import com.xinchi.backend.ticket.service.PassengerTicketInfoService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.PassengerAllotDto;
import com.xinchi.bean.PassengerTicketInfoBean;
import com.xinchi.common.SimpletinyString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class PassengerTicketInfoServiceImpl implements PassengerTicketInfoService {

	@Autowired
	private PassengerTicketInfoDAO dao;

	@Override
	public void insert(PassengerTicketInfoBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(PassengerTicketInfoBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public PassengerTicketInfoBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<PassengerTicketInfoBean> selectByParam(PassengerTicketInfoBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public String insertList(List<PassengerTicketInfoBean> ptis) {
		for (PassengerTicketInfoBean pti : ptis) {
			dao.insert(pti);
		}
		return SUCCESS;
	}

	@Override
	public String checkSameAirLeg(List<String> passenger_pks) {
		List<PassengerAllotDto> passengerAllots = airTicketNameListDAO.selectByPassengerPks(passenger_pks);

		List<PassengerAllotDto> compares = new ArrayList<PassengerAllotDto>();

		String compare_passenger_pk = passengerAllots.get(0).getPassenger_pk();

		for (int i = passengerAllots.size() - 1; i >= 0; i--) {
			if (passengerAllots.get(i).getIs_allot().equals("Y")) {
				passengerAllots.remove(i);
			} else {
				if (passengerAllots.get(i).getPassenger_pk().equals(compare_passenger_pk)) {
					compares.add(passengerAllots.get(i));
				}
			}
		}

		if (compares.size() == passengerAllots.size()) {
			return SUCCESS;
		} else {
			for (PassengerAllotDto compare : compares) {
				int hasCount = 1;
				for (PassengerAllotDto allot : passengerAllots) {
					if (compare.getPassenger_pk().equals(allot.getPassenger_pk()))
						continue;
					if (compare.getDate().equals(allot.getDate()) && compare.getFrom_city().equals(allot.getFrom_city())
							&& compare.getTo_city().equals(allot.getTo_city()) && allot.getIs_allot().equals("N")) {
						hasCount += 1;
					}
				}
				if (hasCount == passenger_pks.size()) {
					return SUCCESS;
				}
			}
		}
		return FAIL;
	}

	@Autowired
	private AirTicketPayableDAO airTicketPayableDao;

	@Autowired
	private AirTicketNameListDAO airTicketNameListDAO;

	@Autowired
	private AirTicketOrderDAO airTicketOrderDao;

	@Autowired
	private OrderDAO orderDao;

	@Autowired
	private BudgetStandardOrderDAO bsoDao;

	@Autowired
	private BudgetNonStandardOrderDAO bnsoDao;

	@Override
	public String allotTicket(String json) {
		JSONArray arr = JSONArray.fromObject(json);
		Set<String> ticket_orders = new HashSet<String>();
		for (int i = 0; i < arr.size(); i++) {

			JSONObject obj = arr.getJSONObject(i);
			String ticket_source = obj.getString("ticket_source");
			String ticket_source_pk = obj.getString("ticket_source_pk");

			String ticket_cost = obj.getString("ticket_cost");
			String ticket_PNR = obj.getString("ticket_PNR");
			String passenger_pks = obj.getString("passenger_pks");
			String pkkk[] = passenger_pks.split(",");

			// 保存机票供应商应付款
			AirTicketPayableBean airTicketPayable = new AirTicketPayableBean();
			airTicketPayable.setSupplier_employee_pk(ticket_source_pk);
			airTicketPayable.setBudget_payable(
					null != ticket_cost ? new BigDecimal(ticket_cost).multiply(new BigDecimal(pkkk.length))
							: BigDecimal.ZERO);
			airTicketPayable.setPNR(ticket_PNR);
			airTicketPayable.setBudget_balance(
					null != ticket_cost ? new BigDecimal(ticket_cost).multiply(new BigDecimal(pkkk.length))
							: BigDecimal.ZERO);
			airTicketPayable.setPaid(BigDecimal.ZERO);

			airTicketPayableDao.insert(airTicketPayable);

			BigDecimal cost = new BigDecimal(ticket_cost);
			JSONArray ticket_info = obj.getJSONArray("ticket_info");

			for (int j = 0; j < ticket_info.size(); j++) {
				JSONObject info = ticket_info.getJSONObject(j);
				int ticket_index = info.getInt("ticket_index");
				String ticket_date = info.getString("ticket_date");
				String ticket_number = info.getString("ticket_number");
				String from_to_time = info.getString("from_to_time");
				String from_to_city = info.getString("from_to_city");
				String from_airport = info.getString("from_airport");
				String to_airport = info.getString("to_airport");
				String terminal = info.getString("terminal");

				for (String pk : pkkk) {
					if (SimpletinyString.isEmpty(pk))
						continue;
					PassengerTicketInfoBean pti = new PassengerTicketInfoBean();

					pti.setTicket_cost(cost);
					pti.setTicket_source(ticket_source);
					pti.setTicket_source_pk(ticket_source_pk);
					pti.setPNR(ticket_PNR);
					pti.setTicket_index(ticket_index);
					pti.setTicket_date(ticket_date);
					pti.setTicket_number(ticket_number);
					pti.setFrom_to_time(from_to_time);
					pti.setFrom_to_city(from_to_city);
					pti.setFrom_airport(from_airport);
					pti.setTo_airport(to_airport);
					pti.setTerminal(terminal);
					pti.setPassenger_pk(pk);
					pti.setBase_pk(airTicketPayable.getPk());
					dao.insert(pti);

					AirTicketNameListBean atnl = airTicketNameListDAO.selectByPrimaryKey(pk);
					if (j == 0) {
						// 更新机票订单机票款
						AirTicketOrderBean ato = airTicketOrderDao.selectByPrimaryKey(atnl.getTicket_order_pk());
						ato.setTicket_cost(ato.getTicket_cost().add(cost));
						airTicketOrderDao.update(ato);

						ticket_orders.add(atnl.getTicket_order_pk());
					}

					// 校验名单是否已经完成操作所有航段
					List<PassengerAllotDto> allots = airTicketNameListDAO.selectPassengerAllotByPassengerPk(pk);
					boolean done = true;
					for (PassengerAllotDto allot : allots) {
						if (allot.getIs_allot().equals("N")) {
							done = false;
							break;
						}
					}
					if (done) {
						atnl.setStatus("Y");
						airTicketNameListDAO.update(atnl);
					}
				}

			}
		}

		// 验证ticketorder 是否完成所有乘客的出票
		for (String order_pk : ticket_orders) {
			AirTicketOrderBean order = airTicketOrderDao.selectByPrimaryKey(order_pk);
			List<PassengerAllotDto> passenger_allots = airTicketNameListDAO.selectPassengerAllotByOrderPk(order_pk);
			boolean done = true;
			for (PassengerAllotDto pad : passenger_allots) {
				if (pad.getIs_allot().equals("N")) {
					done = false;
					break;
				}
			}
			// 如果完成了出票，更新order状态，并更新产品订单机票款项
			if (done) {
				order.setCost_done_flg("Y");
				airTicketOrderDao.update(order);
				OrderDto sale_order = orderDao.selectByTeamNumber(order.getTeam_number());

				if (sale_order.getStandard_flg().equals("Y")) {
					BudgetStandardOrderBean standardOrder = bsoDao.selectByPrimaryKey(sale_order.getPk());
					standardOrder.setAir_ticket_cost(order.getTicket_cost());
					bsoDao.update(standardOrder);
				} else {
					BudgetNonStandardOrderBean nonStandardOrder = bnsoDao.selectByPrimaryKey(sale_order.getPk());
					nonStandardOrder.setAir_ticket_cost(order.getTicket_cost());
					bnsoDao.update(nonStandardOrder);
				}
			}
		}
		return SUCCESS;
	}

}