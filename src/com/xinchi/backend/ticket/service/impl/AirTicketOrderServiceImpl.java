package com.xinchi.backend.ticket.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.backend.ticket.dao.AirTicketOrderDAO;
import com.xinchi.backend.ticket.dao.AirTicketOrderLegDAO;
import com.xinchi.backend.ticket.service.AirTicketOrderService;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.AirTicketOrderLegBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.SaleOrderNameListBean;
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

	@Override
	public AirTicketOrderBean selectBySaleOrderPk(String pk) {

		return dao.selectBySaleOrderPk(pk);
	}

	@Autowired
	private OrderDAO orderDao;

	@Autowired
	private OrderNameListDAO orderNameListDao;

	@Autowired
	private AirTicketOrderDAO airTicketDao;

	@Autowired
	private AirTicketOrderLegDAO airTicketOrderLegDao;

	@Override
	public String createOrder(String team_number, String json) {
		// 销售订单
		OrderDto order = orderDao.selectByTeamNumber(team_number);

		JSONArray arr = JSONArray.fromObject(json);

		boolean hasLeg = true;

		if (null == arr || arr.size() == 0) {
			hasLeg = false;
		}
		String first_ticket_date = "";
		String first_start_city = "";
		String first_end_city = "";
		AirTicketOrderBean airTicketOrder = new AirTicketOrderBean();
		// 如果有航段信息
		if (hasLeg) {
			// 查看是否有名单
			List<SaleOrderNameListBean> names = orderNameListDao.selectByTeamNumber(team_number);
			if (null == names || names.size() < 1) {
				return "没有名单不能生成订单";
			}
			for (int i = 0; i < arr.size(); i++) {

				JSONObject obj = JSONObject.fromObject(arr.get(i));

				int leg_index = obj.getInt("leg_index");
				String from_city = obj.getString("leg_from");
				String to_city = obj.getString("leg_to");
				String date = obj.getString("leg_date");

				if (leg_index == 1) {
					first_ticket_date = date;
					first_start_city = from_city;
					first_end_city = to_city;
					break;
				}
			}

			airTicketOrder.setClient_number(order.getProduct_manager_number());
			airTicketOrder.setFirst_ticket_date(first_ticket_date);
			airTicketOrder.setFirst_start_city(first_start_city);
			airTicketOrder.setFirst_end_city(first_end_city);
			airTicketOrder.setPeople_count((order.getAdult_count() == null ? 0 : order.getAdult_count())
					+ (order.getSpecial_count() == null ? 0 : order.getSpecial_count()));
			airTicketOrder.setTeam_number(team_number);

			String order_pk = airTicketDao.insert(airTicketOrder);

			for (int i = 0; i < arr.size(); i++) {

				JSONObject obj = JSONObject.fromObject(arr.get(i));

				int leg_index = obj.getInt("leg_index");
				String from_city = obj.getString("leg_from");
				String to_city = obj.getString("leg_to");
				String date = obj.getString("leg_date");

				AirTicketOrderLegBean atol = new AirTicketOrderLegBean();
				atol.setSort_index(leg_index);
				atol.setFrom_city(from_city);
				atol.setTo_city(to_city);
				atol.setDate(date);
				atol.setTicket_order_pk(order_pk);

				airTicketOrderLegDao.insert(atol);

			}

		} else {
			airTicketOrder.setTicket_cost(BigDecimal.ZERO);
			airTicketOrder.setStatus("Y");

			airTicketOrder.setClient_number(order.getProduct_manager_number());
			airTicketOrder.setFirst_ticket_date(first_ticket_date);
			airTicketOrder.setFirst_start_city(first_start_city);
			airTicketOrder.setFirst_end_city(first_end_city);
			airTicketOrder.setPeople_count((order.getAdult_count() == null ? 0 : order.getAdult_count())
					+ (order.getSpecial_count() == null ? 0 : order.getSpecial_count()));
			airTicketOrder.setTeam_number(team_number);

			airTicketDao.insert(airTicketOrder);
		}

		return SUCCESS;
	}
	
	
	@Override
	public List<AirTicketOrderLegBean> selectAirTicketOrderLegByOrderPk(String order_pk) {
		
		return airTicketOrderLegDao.selectByOrderPk(order_pk);
	}

}