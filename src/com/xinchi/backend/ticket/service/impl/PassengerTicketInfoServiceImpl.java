package com.xinchi.backend.ticket.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.payable.dao.AirTicketPaidDetailDAO;
import com.xinchi.backend.payable.dao.AirTicketPayableDAO;
import com.xinchi.backend.ticket.dao.AirTicketChangeLogDAO;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.dao.AirTicketOrderDAO;
import com.xinchi.backend.ticket.dao.PassengerTicketInfoDAO;
import com.xinchi.backend.ticket.service.PassengerTicketInfoService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.AirTicketPaidDetailBean;
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

			int people_count = pkkk.length;

			// 保存机票供应商应付款
			AirTicketPayableBean airTicketPayable = new AirTicketPayableBean();
			String payable_pk = DBCommonUtil.genPk();
			airTicketPayable.setPk(payable_pk);
			airTicketPayable.setSupplier_employee_pk(ticket_source_pk);
			airTicketPayable.setBudget_payable(null != sum_cost ? new BigDecimal(sum_cost) : BigDecimal.ZERO);
			airTicketPayable.setPNR(ticket_PNR);
			airTicketPayable.setBudget_balance(null != sum_cost ? new BigDecimal(sum_cost) : BigDecimal.ZERO);
			airTicketPayable.setPaid(BigDecimal.ZERO);
			airTicketPayable.setPayable_type(ResourcesConstants.TICKET_PAYABLE_TYPE_COST);

			AirTicketNameListBean passenger = airTicketNameListDao.selectByPrimaryKey(pkkk[0]);
			airTicketPayable.setPassenger(passenger.getName());
			airTicketPayable.setPeople_count(people_count);

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
							ticket_date + SimpletinyString.left(passenger.getName(), 4) + people_count + "人机票款。");
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
					pti.setBase_pk(payable_pk);
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
			if (!SimpletinyString.isEmpty(ticket_charges)) {
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
						payable_charges.setPayable_type(ResourcesConstants.TICKET_PAYABLE_TYPE_CHARGES);

						payable_charges.setComment(payable_charges.getFirst_date()
								+ SimpletinyString.left(passenger.getName(), 4) + people_count + "人票务手续费。");

						airTicketPayableDao.insert(payable_charges);

					} catch (Exception e) {
						e.printStackTrace();
						return FAIL;
					}
				}
			}
			airTicketPayableDao.insertWithPk(airTicketPayable);

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

				// 更新销售订单
				if (sale_order.getStandard_flg().equals("Y")) {
					BudgetStandardOrderBean standardOrder = bsoDao.selectByPrimaryKey(sale_order.getPk());
					standardOrder.setAir_ticket_cost(airTicketCost);
					// 更新订单票务操作状态为已出票
					standardOrder.setOperate_flg(SimpletinyString.replaceCharFromRight(standardOrder.getOperate_flg(),
							ResourcesConstants.AIR_OPERATE_STATUS_YES, 1));
					bsoDao.update(standardOrder);
				} else {
					BudgetNonStandardOrderBean nonStandardOrder = bnsoDao.selectByPrimaryKey(sale_order.getPk());
					nonStandardOrder.setAir_ticket_cost(airTicketCost);
					nonStandardOrder.setOperate_flg(SimpletinyString.replaceCharFromRight(
							nonStandardOrder.getOperate_flg(), ResourcesConstants.AIR_OPERATE_STATUS_YES, 1));
					bnsoDao.update(nonStandardOrder);
				}
			}

			for (String tn : unDoneTns) {
				OrderDto sale_order = orderDao.selectByTeamNumber(tn);
				// 更新销售订单
				if (sale_order.getStandard_flg().equals("Y")) {
					BudgetStandardOrderBean standardOrder = bsoDao.selectByPrimaryKey(sale_order.getPk());
					// 更新订单票务操作状态为出票中
					standardOrder.setOperate_flg(SimpletinyString.replaceCharFromRight(standardOrder.getOperate_flg(),
							ResourcesConstants.AIR_OPERATE_STATUS_ING, 1));
					bsoDao.update(standardOrder);
				} else {
					BudgetNonStandardOrderBean nonStandardOrder = bnsoDao.selectByPrimaryKey(sale_order.getPk());
					nonStandardOrder.setOperate_flg(SimpletinyString.replaceCharFromRight(
							nonStandardOrder.getOperate_flg(), ResourcesConstants.AIR_OPERATE_STATUS_ING, 1));
					bnsoDao.update(nonStandardOrder);
				}
			}

		}

		return SUCCESS;
	}

	@Autowired
	private AirTicketPaidDetailDAO airTicketPaidDetailDao;

	@Autowired
	private AirTicketChangeLogDAO airTicketChangeLogDao;

	@Override
	public String rollBackNameDone(List<String> passenger_pks) {

		List<AirTicketNameListBean> names = airTicketNameListDao.selectByPks(passenger_pks.toArray(new String[0]));
		Set<String> team_numbers = new HashSet<>();
		Set<String> payable_pks = new HashSet<>();
		Map<String, Set<String>> order_number_payables = new HashMap<>();

		for (AirTicketNameListBean name : names) {
			Set<String> current_payable_pks = new HashSet<>();
			List<PassengerTicketInfoBean> infos = dao.selectByPassengerPk(name.getPk());
			for (PassengerTicketInfoBean info : infos) {
				current_payable_pks.add(info.getBase_pk());
			}
			payable_pks.addAll(current_payable_pks);

			String order_number = name.getOrder_number();

			if (order_number_payables.keySet().contains(order_number)) {
				order_number_payables.get(order_number).addAll(current_payable_pks);
			} else {
				order_number_payables.put(order_number, current_payable_pks);
			}
		}
		Map<String, BigDecimal> payable_moneys = new HashMap<>();

		Set<String> name_pks = new HashSet<>();
		for (String payable_pk : payable_pks) {
			AirTicketPayableBean payable = airTicketPayableDao.selectByPrimaryKey(payable_pk);
			BigDecimal money = BigDecimal.ZERO;
			// 删除应付款
			if (SimpletinyString.isEmpty(payable.getRelated_pk())) {
				List<AirTicketPaidDetailBean> paids = airTicketPaidDetailDao.selectByPayablePk(payable_pk);
				if (null != paids && paids.size() > 0) {
					return "首航日期为：" + payable.getFirst_date() + "，首航段为：" + payable.getFrom_to_city() + "乘客为："
							+ payable.getPassenger() + "的应付款，已存在已付款，请处理后再进行操作！";
				}

				money = money.add(payable.getBudget_payable());
				airTicketPayableDao.delete(payable_pk);
			} else {
				List<AirTicketPayableBean> payables = airTicketPayableDao.selectByRelatedPk(payable.getRelated_pk());
				for (AirTicketPayableBean p : payables) {
					List<AirTicketPaidDetailBean> paids = airTicketPaidDetailDao.selectByPayablePk(payable_pk);
					if (null != paids && paids.size() > 0) {
						return "首航日期为：" + p.getFirst_date() + "，首航段为：" + p.getFrom_to_city() + "乘客为：" + p.getPassenger()
								+ "的应付款，已存在已付款，请处理后再进行操作！";
					}

					money = money.add(p.getBudget_payable());
				}
				airTicketPayableDao.deleteByRelatedPk(payable.getRelated_pk());
			}
			payable_moneys.put(payable_pk, money);

			// 删除详细的乘客信息
			List<PassengerTicketInfoBean> infos = dao.selectAllByPayablePk(payable_pk);
			for (PassengerTicketInfoBean info : infos) {
				name_pks.add(info.getPassenger_pk());
				dao.delete(info.getPk());
			}
		}

		// 更新订单机票款
		for (String order_number : order_number_payables.keySet()) {
			AirTicketOrderBean ato = airTicketOrderDao.selectByOrderNumber(order_number);
			Set<String> current_payable_pks = order_number_payables.get(order_number);
			BigDecimal cost = BigDecimal.ZERO;
			for (String p : current_payable_pks) {
				cost = cost.add(payable_moneys.get(p));
			}

			ato.setTicket_cost(ato.getTicket_cost().subtract(cost));
			ato.setCost_done_flg("N");
			ato.setStatus("I");
			airTicketOrderDao.update(ato);
		}

		List<AirTicketNameListBean> allNames = airTicketNameListDao.selectByPks(name_pks.toArray(new String[0]));
		Map<String, String> statuses = new HashMap<>();

		Map<String, BigDecimal> change_pks = new HashMap<>();
		// 更新名单到待出票状态
		for (AirTicketNameListBean name : allNames) {
			team_numbers.add(name.getTeam_number());
			List<PassengerTicketInfoBean> infos = dao.selectByPassengerPk(name.getPk());
			String key = name.getTeam_number();
			if (null != infos && infos.size() > 0) {
				if (!statuses.keySet().contains(key)
						|| !statuses.get(key).equals(ResourcesConstants.AIR_OPERATE_STATUS_ING)) {
					statuses.put(key, ResourcesConstants.AIR_OPERATE_STATUS_ING);
				}

			} else {
				if (!statuses.keySet().contains(key)) {
					statuses.put(key, ResourcesConstants.AIR_OPERATE_STATUS_ORDERD);
				}
			}
			// 如果有航变，删除航变
			if (name.getStatus().equals("C")) {
				String change_pk = name.getChange_pk();
				change_pks.put(change_pk,
						(change_pks.get(change_pk) == null ? BigDecimal.ZERO : change_pks.get(change_pk))
								.add(name.getChange_cost()));
				airTicketChangeLogDao.delete(name.getChange_pk());
				name.setChange_pk("");
				name.setChange_cost(BigDecimal.ZERO);
			}
			name.setStatus("I");
			airTicketNameListDAO.update(name);
		}

		// 修改航变应付款
		for (String change_pk : change_pks.keySet()) {
			List<AirTicketPayableBean> pses = airTicketPayableDao.selectByRelatedPk(change_pk);
			AirTicketPayableBean ps = pses.get(0) == null ? new AirTicketPayableBean() : pses.get(0);
			List<AirTicketNameListBean> lastNames = airTicketNameListDao.selectByChangePk(change_pk);

			// 如果所有航变信息都被打回了，则删除航变应付款，否则更新航变应付款
			if (null != lastNames && lastNames.size() > 0) {
				ps.setBudget_payable(ps.getBudget_payable().subtract(change_pks.get(change_pk)));
				ps.setBudget_balance(ps.getBudget_balance().subtract(change_pks.get(change_pk)));
				airTicketPayableDao.update(ps);
			} else {
				airTicketPayableDao.delete(ps.getPk());
			}
		}

		// 更新销售订单
		for (String team_number : team_numbers) {
			String operate_flg = statuses.get(team_number);
			// 销售订单机票款设置为0
			OrderDto sale_order = orderDao.selectByTeamNumber(team_number);
			if (sale_order.getStandard_flg().equals("Y")) {
				BudgetStandardOrderBean standardOrder = bsoDao.selectByPrimaryKey(sale_order.getPk());
				standardOrder.setOperate_flg(
						SimpletinyString.replaceCharFromRight(sale_order.getOperate_flg(), operate_flg, 1));
				standardOrder.setAir_ticket_cost(BigDecimal.ZERO);
				bsoDao.update(standardOrder);
			} else {
				BudgetNonStandardOrderBean nonStandardOrder = bnsoDao.selectByPrimaryKey(sale_order.getPk());
				nonStandardOrder.setOperate_flg(
						SimpletinyString.replaceCharFromRight(sale_order.getOperate_flg(), operate_flg, 1));
				nonStandardOrder.setAir_ticket_cost(BigDecimal.ZERO);
				bnsoDao.update(nonStandardOrder);
			}
		}

		TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();

		if (transactionStatus != null && transactionStatus.isNewTransaction()) {
			transactionStatus.setRollbackOnly();
		}
		return SUCCESS;

	}

	@Override
	public List<PassengerTicketInfoBean> selectByPassengerPk(String passenger_pk) {
		return dao.selectByPassengerPk(passenger_pk);
	}

}