package com.xinchi.backend.ticket.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
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
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.ResourcesConstants;
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

	@Autowired
	private AirTicketNameListDAO airTicketNameListDao;

	@Override
	public String allotTicket(String json) {
		JSONArray arr = JSONArray.fromObject(json);
		Set<String> ticket_orders = new HashSet<String>();
		for (int i = 0; i < arr.size(); i++) {

			JSONObject obj = arr.getJSONObject(i);
			String ticket_source = obj.getString("ticket_source");
			String ticket_source_pk = obj.getString("ticket_source_pk");

			String ticket_cost = obj.getString("ticket_cost");
			String ticket_charges = obj.getString("ticket_charges");
			String sum_cost = obj.getString("sum_cost");
			String ticket_PNR = obj.getString("ticket_PNR");
			String passenger_pks = obj.getString("passenger_pks");
			String pkkk[] = passenger_pks.split(",");

			// 保存机票供应商应付款
			AirTicketPayableBean airTicketPayable = new AirTicketPayableBean();
			airTicketPayable.setSupplier_employee_pk(ticket_source_pk);
			airTicketPayable.setBudget_payable(null != sum_cost ? new BigDecimal(sum_cost) : BigDecimal.ZERO);
			airTicketPayable.setPNR(ticket_PNR);
			airTicketPayable.setBudget_balance(null != sum_cost ? new BigDecimal(sum_cost) : BigDecimal.ZERO);
			airTicketPayable.setPaid(BigDecimal.ZERO);
			airTicketPayable.setPayable_type(ResourcesConstants.Ticket_PAYABLE_TYPE_COST);

			AirTicketNameListBean passenger = airTicketNameListDao.selectByPrimaryKey(pkkk[0]);
			airTicketPayable.setPassenger(passenger.getName());

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

				if (ticket_index == 1) {
					airTicketPayable.setFrom_to_city(from_to_city);
					airTicketPayable.setFirst_date(ticket_date);
					airTicketPayable.setComment(
							ticket_date + SimpletinyString.left(passenger.getName(), 4) + pkkk.length + "人机票款。");
				}

				// 保存乘客详细信息
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
			if (null != ticket_charges) {
				BigDecimal charges = new BigDecimal(ticket_charges);
				if (charges.compareTo(BigDecimal.ZERO) == 1) {
					String related_pk = DBCommonUtil.genPk();
					airTicketPayable.setRelated_pk(related_pk);

					AirTicketPayableBean payable_charges = new AirTicketPayableBean();

					try {
						PropertyUtils.copyProperties(payable_charges, airTicketPayable);

						payable_charges.setBudget_payable(charges);
						payable_charges.setBudget_balance(charges);
						payable_charges.setPaid(BigDecimal.ZERO);
						payable_charges.setPayable_type(ResourcesConstants.Ticket_PAYABLE_TYPE_CHARGES);
						payable_charges.setComment(payable_charges.getFirst_date()
								+ SimpletinyString.left(passenger.getName(), 4) + pkkk.length + "人票务手续费。");
						airTicketPayableDao.insert(payable_charges);

					} catch (Exception e) {
						e.printStackTrace();
						return FAIL;
					}
				}
			}
			airTicketPayableDao.insert(airTicketPayable);

		}

		// 验证ticketorder 是否完成所有乘客的出票
		for (String order_pk : ticket_orders) {
			AirTicketOrderBean order = airTicketOrderDao.selectByPrimaryKey(order_pk);
			List<PassengerAllotDto> passenger_allots = airTicketNameListDAO.selectPassengerAllotByOrderPk(order_pk);
			boolean done = true;
			Set<String> unDoneTns = new HashSet<String>();
			Set<String> tns = new HashSet<String>();
			for (PassengerAllotDto pad : passenger_allots) {
				tns.add(pad.getTeam_number());
				if (pad.getIs_allot().equals("N")) {
					done = false;
					unDoneTns.add(pad.getTeam_number());
				}
			}

			// 如果完成了出票，更新票务order状态，并更新产品订单机票款项
			if (done) {
				order.setCost_done_flg("Y");
				order.setStatus("Y");
				airTicketOrderDao.update(order);
			}

			// 更新已经完成出票的销售订单
			for (String unTn : unDoneTns) {
				Iterator<String> it = tns.iterator();
				while (it.hasNext()) {
					if (it.next().equals(unTn)) {
						it.remove();
						break;
					}

				}
			}
			for (String tn : tns) {
				BigDecimal airTicketCost = BigDecimal.ZERO;

				OrderDto sale_order = orderDao.selectByTeamNumber(tn);
				List<AirTicketNameListBean> names = airTicketNameListDao.selectByTeamNumber(tn);
				for (AirTicketNameListBean name : names) {
					List<PassengerTicketInfoBean> ptis = dao.selectByPassengerPk(name.getPk());
					for (PassengerTicketInfoBean pti : ptis) {
						if (pti.getTicket_index() == 1) {
							airTicketCost = airTicketCost.add(pti.getTicket_cost());
						}
					}
				}

				if (sale_order.getStandard_flg().equals("Y")) {
					BudgetStandardOrderBean standardOrder = bsoDao.selectByPrimaryKey(sale_order.getPk());
					standardOrder.setAir_ticket_cost(airTicketCost);
					bsoDao.update(standardOrder);
				} else {
					BudgetNonStandardOrderBean nonStandardOrder = bnsoDao.selectByPrimaryKey(sale_order.getPk());
					nonStandardOrder.setAir_ticket_cost(airTicketCost);
					bnsoDao.update(nonStandardOrder);
				}
			}

		}

		return SUCCESS;
	}

	@Override
	public String rollBackNameDone(List<String> passenger_pks) {

		for (String passenger_pk : passenger_pks) {
			AirTicketNameListBean name = airTicketNameListDao.selectByPrimaryKey(passenger_pk);

			List<PassengerTicketInfoBean> infos = dao.selectByPassengerPk(passenger_pk);

			// 乘客机票合计
			BigDecimal cost = BigDecimal.ZERO;

			if (null != infos && infos.size() > 0) {
				// 重新核算机票应付款
				Set<String> payable_pks = new HashSet<String>();
				for (PassengerTicketInfoBean info : infos) {
					payable_pks.add(info.getBase_pk());
				}

				for (String payable_pk : payable_pks) {
					for (PassengerTicketInfoBean info : infos) {
						if (payable_pk.equals(info.getBase_pk())) {
							AirTicketPayableBean payable = airTicketPayableDao.selectByPrimaryKey(payable_pk);
							payable.setBudget_payable(payable.getBudget_payable().subtract(info.getTicket_cost()));
							payable.setBudget_balance(payable.getBudget_balance().subtract(info.getTicket_cost()));
							airTicketPayableDao.update(payable);
							// 计算乘客机票合计
							cost = cost.add(info.getTicket_cost());
							break;
						}
					}
				}

				// 删除详细的乘客信息
				for (PassengerTicketInfoBean info : infos) {
					dao.delete(info.getPk());
				}

				// 更新机票订单机票款
				AirTicketOrderBean ato = airTicketOrderDao.selectByPrimaryKey(name.getTicket_order_pk());
				ato.setTicket_cost(ato.getTicket_cost().subtract(cost));
				ato.setCost_done_flg("N");
				ato.setStatus("I");
				airTicketOrderDao.update(ato);

				// 更新名单到待出票状态
				name.setStatus("I");
				airTicketNameListDAO.update(name);

				// 销售订单机票款设置为0
				OrderDto sale_order = orderDao.selectByTeamNumber(name.getTeam_number());

				if (sale_order.getStandard_flg().equals("Y")) {
					BudgetStandardOrderBean standardOrder = bsoDao.selectByPrimaryKey(sale_order.getPk());
					standardOrder.setAir_ticket_cost(BigDecimal.ZERO);
					bsoDao.update(standardOrder);
				} else {
					BudgetNonStandardOrderBean nonStandardOrder = bnsoDao.selectByPrimaryKey(sale_order.getPk());
					nonStandardOrder.setAir_ticket_cost(BigDecimal.ZERO);
					bnsoDao.update(nonStandardOrder);
				}
			}

		}

		return SUCCESS;

	}

	@Override
	public List<PassengerTicketInfoBean> selectByPassengerPk(String passenger_pk) {
		return dao.selectByPassengerPk(passenger_pk);
	}

}