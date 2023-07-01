package com.xinchi.backend.ticket.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.backend.ticket.dao.AirNeedTeamNumberDAO;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.dao.AirTicketNeedDAO;
import com.xinchi.backend.ticket.dao.AirTicketOrderDAO;
import com.xinchi.backend.ticket.dao.AirTicketOrderLegDAO;
import com.xinchi.backend.ticket.dao.PassengerTicketInfoDAO;
import com.xinchi.backend.ticket.service.AirTicketOrderService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirNeedTeamNumberBean;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.AirTicketOrderLegBean;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.PassengerTicketInfoBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.tools.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class AirTicketOrderServiceImpl implements AirTicketOrderService {

	@Autowired
	private AirTicketOrderDAO dao;

	@Override
	public void insert(AirTicketOrderBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(AirTicketOrderBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public AirTicketOrderBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<AirTicketOrderBean> selectByParam(AirTicketOrderBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<AirTicketOrderBean> selectByPage(Page<AirTicketOrderBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<AirTicketOrderBean> selectByPks(List<String> airTicketOrderPks) {
		return dao.selectByPks(airTicketOrderPks);
	}

	@Autowired
	private OrderNameListDAO orderNameListDao;

	@Autowired
	private AirTicketOrderDAO airTicketDao;

	@Autowired
	private AirTicketOrderLegDAO airTicketOrderLegDao;

	@Autowired
	private AirTicketNeedDAO airTicketNeedDao;

	@Autowired
	private AirNeedTeamNumberDAO airNeedTeamNumberDao;

	@Autowired
	private NumberService numberService;

	@Autowired
	private OrderDAO orderDao;
	@Autowired
	private BudgetNonStandardOrderDAO bnsoDao;
	@Autowired
	private BudgetStandardOrderDAO bsoDao;

	@Autowired
	private OrderNameListDAO saleOrderNameListDao;

	@Override
	public String createOrder(String json) {

		JSONObject obj = JSONObject.fromObject(json);

		String[] need_pks = obj.getString("need_pk").split(",");

		BigDecimal ticket_price = new BigDecimal(obj.getString("ticket_price"));
		BigDecimal ticket_special_price = new BigDecimal(obj.getString("ticket_special_price"));
		JSONArray arr = obj.getJSONArray("legJson");
		boolean hasLeg = null != arr && arr.size() > 0;

		for (String need_pk : need_pks) {
			AirTicketNeedBean atn = airTicketNeedDao.selectByPk(need_pk);

			String first_start_city = "";
			String first_end_city = "";
			AirTicketOrderBean airTicketOrder = new AirTicketOrderBean();

			airTicketOrder.setPrice(ticket_price);
			airTicketOrder.setSpecial_price(ticket_special_price);
			airTicketOrder.setNeed_pk(need_pk);

			int adult_cnt = atn.getAdult_cnt() == null ? 0 : atn.getAdult_cnt();
			int special_cnt = atn.getSpecial_cnt() == null ? 0 : atn.getSpecial_cnt();

			// BigDecimal ticket_cost = ticket_price.multiply(new BigDecimal(adult_cnt))
			// .add(ticket_special_price.multiply(new BigDecimal(special_cnt)));
			//
			// airTicketOrder.setTicket_cost(ticket_cost);
			airTicketOrder.setPeople_count(adult_cnt + special_cnt);
			airTicketOrder.setOrder_number(numberService.generateTicketOrderNumber());
			airTicketOrder.setClient_number(atn.getTicket_client_number());
			airTicketOrder.setFirst_ticket_date(atn.getFirst_ticket_date());
			airTicketOrder.setPassenger_captain(atn.getPassenger_captain());
			airTicketOrder.setProduct_name(atn.getProduct_name());
			airTicketOrder.setDeparture_date(atn.getDeparture_date());
			airTicketOrder.setComment(atn.getComment());
			// 如果有航段信息
			if (hasLeg) {
				// 查看是否有名单
				List<AirNeedTeamNumberBean> ants = airNeedTeamNumberDao.selectByNeedPk(need_pk);
				List<String> team_numbers = new ArrayList<String>();
				for (AirNeedTeamNumberBean ant : ants) {
					team_numbers.add(ant.getTeam_number());
				}
				List<SaleOrderNameListBean> names = orderNameListDao.selectByTeamNumbers(team_numbers);

				if (null == names || names.size() < 1) {
					return "没有名单不能生成订单";
				}

				for (int i = 0; i < arr.size(); i++) {

					JSONObject inobj = JSONObject.fromObject(arr.get(i));

					int leg_index = inobj.getInt("leg_index");
					String from_city = inobj.getString("leg_from");
					String to_city = inobj.getString("leg_to");

					if (leg_index == 1) {
						first_start_city = from_city;
						first_end_city = to_city;
						break;
					}
				}

				airTicketOrder.setFirst_start_city(first_start_city);
				airTicketOrder.setFirst_end_city(first_end_city);

				String order_pk = airTicketDao.insert(airTicketOrder);

				for (int i = 0; i < arr.size(); i++) {

					JSONObject inobj = JSONObject.fromObject(arr.get(i));

					int leg_index = inobj.getInt("leg_index");
					String from_city = inobj.getString("leg_from");
					String to_city = inobj.getString("leg_to");
					String date = inobj.getString("leg_date");
					String ticket_number = inobj.getString("ticket_number");
					String start_time = inobj.getString("start_time");
					String end_time = inobj.getString("end_time");

					String add_day_flg = inobj.getString("add_day_flg");
					String start_place = inobj.getString("start_place");
					String end_place = inobj.getString("end_place");

					AirTicketOrderLegBean atol = new AirTicketOrderLegBean();
					atol.setSort_index(leg_index);
					atol.setFrom_city(from_city);
					atol.setTo_city(to_city);
					atol.setDate(date);
					atol.setTicket_order_pk(order_pk);
					atol.setStart_time(start_time);
					atol.setEnd_time(end_time);
					atol.setStart_place(start_place);
					atol.setEnd_place(end_place);
					atol.setTicket_number(ticket_number);
					atol.setAdd_day_flg(add_day_flg);

					airTicketOrderLegDao.insert(atol);
				}

				// 更新销售订单
				for (String team_number : team_numbers) {
					OrderDto order = orderDao.selectByTeamNumber(team_number);
					if (order.getStandard_flg().equals("Y")) {
						BudgetStandardOrderBean bso = new BudgetStandardOrderBean();
						bso.setPk(order.getPk());
						bso.setOperate_flg(SimpletinyString.replaceCharFromRight(order.getOperate_flg(),
								ResourcesConstants.AIR_OPERATE_STATUS_ORDERD));
						bso.setLock_flg(SimpletinyString.replaceCharFromRight(order.getLock_flg(), "Y"));
						bso.setName_confirm_status("4");
						bsoDao.update(bso);

					} else {
						BudgetNonStandardOrderBean bnso = new BudgetNonStandardOrderBean();
						bnso.setPk(order.getPk());
						bnso.setOperate_flg(SimpletinyString.replaceCharFromRight(order.getOperate_flg(),
								ResourcesConstants.AIR_OPERATE_STATUS_ORDERD));
						bnso.setLock_flg(SimpletinyString.replaceCharFromRight(order.getLock_flg(), "Y"));
						bnso.setName_confirm_status("4");
						bnsoDao.update(bnso);
					}
				}

				// 生成待出票名单
				for (SaleOrderNameListBean name : names) {
					// 更新销售名单锁定状态
					name.setLock_flg(SimpletinyString.replaceCharFromRight(name.getLock_flg(), "Y", 1));
					saleOrderNameListDao.update(name);

					AirTicketNameListBean nn = new AirTicketNameListBean();
					nn.setTeam_number(name.getTeam_number());
					nn.setClient_number(atn.getTicket_client_number());
					nn.setFirst_ticket_date(atn.getFirst_ticket_date());
					nn.setFirst_start_city(first_start_city);
					nn.setFirst_end_city(first_end_city);
					nn.setTicket_order_pk(order_pk);
					nn.setName(name.getName());
					nn.setId(name.getId());
					nn.setOrder_number(airTicketOrder.getOrder_number());
					nn.setCellphone_A(name.getCellphone_A());
					nn.setCellphone_B(name.getCellphone_B());
					nn.setChairman(name.getChairman());
					nn.setLock_flg("Y");
					nn.setBase_pk(name.getPk());
					nn.setAge(name.getAge());
					nn.setId_type(name.getId_type());
					airTicketNameListDao.insert(nn);
				}

			} else {
				airTicketOrder.setFirst_start_city(first_start_city);
				airTicketOrder.setFirst_end_city(first_end_city);

				airTicketDao.insert(airTicketOrder);
			}

			atn.setOrdered("Y");
			airTicketNeedDao.update(atn);
		}

		return SUCCESS;
	}

	@Autowired
	private PassengerTicketInfoDAO passengerTicketInfoDao;

	@Override
	public String rollBackOrder(String order_pk) {
		// 票务订单
		AirTicketOrderBean order = dao.selectByPrimaryKey(order_pk);

		// 查看该票务订单下是否有出票的名单
		// 该订单下的名单
		List<AirTicketNameListBean> names = airTicketNameListDao.selectByOrderNumber(order.getOrder_number());
		for (AirTicketNameListBean name : names) {

			List<PassengerTicketInfoBean> infos = passengerTicketInfoDao.selectByPassengerPk(name.getPk());
			if (infos != null && infos.size() > 0) {
				return "namelock";
			}
		}

		// 更新票务需求状态
		AirTicketNeedBean atn = airTicketNeedDao.selectByPk(order.getNeed_pk());
		atn.setOrdered("N");
		airTicketNeedDao.update(atn);

		// 删除待出票名单
		airTicketNameListDao.deleteByOrderNumber(order.getOrder_number());

		// 更新销售订单到产品确认状态
		List<AirNeedTeamNumberBean> ants = airNeedTeamNumberDao.selectByNeedPk(order.getNeed_pk());
		List<String> team_numbers = new ArrayList<String>();
		for (AirNeedTeamNumberBean ant : ants) {
			team_numbers.add(ant.getTeam_number());
		}

		for (String team_number : team_numbers) {
			OrderDto sale_order = orderDao.selectByTeamNumber(team_number);
			if (sale_order.getStandard_flg().equals("Y")) {
				BudgetStandardOrderBean bso = new BudgetStandardOrderBean();
				bso.setPk(sale_order.getPk());
				bso.setName_confirm_status(ResourcesConstants.NAME_CONFIRM_STATUS_PRODUCTYES);
				bso.setOperate_flg(SimpletinyString.replaceCharFromRight(sale_order.getOperate_flg(),
						ResourcesConstants.AIR_OPERATE_STATUS_NO, 1));
				bsoDao.update(bso);
			} else {
				BudgetNonStandardOrderBean bnso = new BudgetNonStandardOrderBean();
				bnso.setPk(sale_order.getPk());
				bnso.setName_confirm_status(ResourcesConstants.NAME_CONFIRM_STATUS_PRODUCTYES);
				bnso.setOperate_flg(SimpletinyString.replaceCharFromRight(sale_order.getOperate_flg(),
						ResourcesConstants.AIR_OPERATE_STATUS_NO, 1));
				bnsoDao.update(bnso);
			}
		}

		// 删除航段信息
		List<AirTicketOrderLegBean> legs = airTicketOrderLegDao.selectByOrderPk(order_pk);
		for (AirTicketOrderLegBean leg : legs) {
			airTicketOrderLegDao.delete(leg.getPk());
		}

		// 删除票务订单
		dao.delete(order_pk);

		return SUCCESS;
	}

	@Override
	public List<AirTicketOrderLegBean> selectAirTicketOrderLegByOrderPk(String order_pk) {

		return airTicketOrderLegDao.selectByOrderPk(order_pk);
	}

	@Autowired
	private AirTicketNameListDAO airTicketNameListDao;

	@Override
	public String lockAirTicketOrder(List<String> airTicketOrderPks) {

		// List<AirTicketOrderBean> ticketOrders = dao.selectByPks(airTicketOrderPks);
		// for (AirTicketOrderBean order : ticketOrders) {
		// List<SaleOrderNameListBean> nameList = new
		// ArrayList<SaleOrderNameListBean>();
		// nameList = orderNameListDao.selectByTeamNumber(order.getTeam_number());
		//
		// for (SaleOrderNameListBean name : nameList) {
		// AirTicketNameListBean nn = new AirTicketNameListBean();
		// nn.setTeam_number(order.getTeam_number());
		// nn.setClient_number(order.getClient_number());
		// nn.setFirst_ticket_date(order.getFirst_ticket_date());
		// nn.setFirst_start_city(order.getFirst_start_city());
		// nn.setFirst_end_city(order.getFirst_end_city());
		// nn.setTicket_order_pk(order.getPk());
		// nn.setName(name.getName());
		// nn.setId(name.getId());
		// airTicketNameListDao.insert(nn);
		// }
		// order.setStatus("Y");
		// dao.update(order);

		// // 更新销售待确认名单
		// OrderDto saleOrder = orderDao.selectByTeamNumber(order.getTeam_number());
		// if (saleOrder.getStandard_flg().equals("Y")) {
		// BudgetStandardOrderBean bso = new BudgetStandardOrderBean();
		// bso.setPk(saleOrder.getPk());
		// bso.setName_confirm_status(ResourcesConstants.NAME_CONFIRM_STATUS_TICKETING);
		// bsoDao.update(bso);
		// } else {
		// BudgetNonStandardOrderBean bnso = new BudgetNonStandardOrderBean();
		// bnso.setPk(saleOrder.getPk());
		// bnso.setName_confirm_status(ResourcesConstants.NAME_CONFIRM_STATUS_TICKETING);
		// bnsoDao.update(bnso);
		// }
		// }

		return SUCCESS;
	}

}