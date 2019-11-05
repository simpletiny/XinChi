package com.xinchi.backend.ticket.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.order.service.OrderService;
import com.xinchi.backend.ticket.service.AirTicketNameListService;
import com.xinchi.backend.ticket.service.AirTicketNeedService;
import com.xinchi.backend.ticket.service.AirTicketOrderService;
import com.xinchi.backend.ticket.service.PassengerTicketInfoService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.AirTicketOrderLegBean;
import com.xinchi.bean.OrderAirInfoBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.PassengerAllotDto;
import com.xinchi.bean.ProductOrderAirBaseBean;
import com.xinchi.bean.TicketAllotDto;
import com.xinchi.common.BaseAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TicketAction extends BaseAction {
	private static final long serialVersionUID = 8539114712921019353L;

	@Autowired
	private AirTicketNeedService airTicketNeedService;

	private List<AirTicketNeedBean> airTicketNeeds;
	private AirTicketNeedBean airTicketNeed;

	public String searchAirTicketNeedByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		if (null == airTicketNeed)
			airTicketNeed = new AirTicketNeedBean();
		params.put("bo", airTicketNeed);

		page.setParams(params);
		airTicketNeeds = airTicketNeedService.selectByPage(page);
		return SUCCESS;
	}

	private List<AirTicketOrderBean> airTicketOrders;
	private AirTicketOrderBean airTicketOrder;

	public String searchAirTicketOrderByPage() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", airTicketOrder);

		page.setParams(params);
		airTicketOrders = airTicketOrderService.selectByPage(page);
		return SUCCESS;
	}

	private List<OrderAirInfoBean> order_air_infos;
	private String team_number;

	private ProductOrderAirBaseBean air_base;

	public String searchOrderAirInfoByTeamNumber() {
		order_air_infos = airTicketNeedService.selectOrderAirInfoByTeamNumber(team_number);
		return SUCCESS;
	}

	private String sale_order_pk;
	private BigDecimal air_ticket_cost;
	private String standard_flg;

	@Autowired
	private AirTicketOrderService airTicketOrderService;

	public String createTicketOrder() {

		resultStr = airTicketOrderService.createOrder(team_number, json);

		return SUCCESS;
	}

	private List<String> airTicketOrderPks;

	@Autowired
	private AirTicketNameListService airTicketNameListService;
	private List<AirTicketNameListBean> airTicketNameList;

	/**
	 * 锁定名单操作，其实就是拆分名单
	 * 
	 * @return
	 */
	public String lockAirTicketOrder() {

		resultStr = airTicketOrderService.lockAirTicketOrder(airTicketOrderPks);

		return SUCCESS;
	}

	private AirTicketNameListBean passenger;

	public String searchAirTicketNameListByPage() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", passenger);

		page.setParams(params);
		airTicketNameList = airTicketNameListService.selectByPage(page);
		return SUCCESS;
	}

	private String json;

	private List<TicketAllotDto> ticketAllots;

	public String operatePassengers() {
		ticketAllots = new ArrayList<TicketAllotDto>();

		JSONArray arr = JSONArray.fromObject(json);
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			TicketAllotDto ta = new TicketAllotDto();
			String ticket_source = obj.getString("sourceName");
			String ticket_source_pk = obj.getString("sourcePk");
			ta.setTicket_source(ticket_source);
			ta.setTicket_source_pk(ticket_source_pk);

			String passengerPks = obj.getString("passengerPks");
			String pks[] = passengerPks.split(",");
			List<AirTicketNameListBean> passengers = airTicketNameListService.selectByPks(pks);
			ta.setPassengers(passengers);
			ticketAllots.add(ta);

			List<PassengerAllotDto> airLegs = airTicketNameListService
					.selectPassengerAllotByPassengerPks(Arrays.asList(pks));

			// 去除不同的航段
			List<PassengerAllotDto> compares = new ArrayList<PassengerAllotDto>();
			String compare_passenger_pk = airLegs.get(0).getPassenger_pk();
			for (int j = airLegs.size() - 1; j >= 0; j--) {
				if (airLegs.get(j).getPassenger_pk().equals(compare_passenger_pk)) {
					compares.add(airLegs.get(j));
				}
			}

			for (int k = compares.size() - 1; k >= 0; k--) {
				if (compares.get(k).getIs_allot().equals("Y")) {
					compares.remove(k);
					continue;
				}

				int hasCount = 1;
				for (PassengerAllotDto leg : airLegs) {
					if (compares.get(k).getPassenger_pk().equals(leg.getPassenger_pk())) {
						continue;
					}
					if (compares.get(k).getDate().equals(leg.getDate())
							&& compares.get(k).getFrom_city().equals(leg.getFrom_city())
							&& compares.get(k).getTo_city().equals(leg.getTo_city()) && leg.getIs_allot().equals("N")) {
						hasCount += 1;
					}
				}
				if (hasCount != pks.length) {
					compares.remove(k);
				}
			}
			compares.sort(PassengerAllotDto.Comparators.DATE);
			ta.setAirLegs(compares);

		}

		return SUCCESS;
	}

	@Autowired
	private PassengerTicketInfoService passengerTicketInfoService;

	public String allotTicket() {
		resultStr = passengerTicketInfoService.allotTicket(json);
		return SUCCESS;
	}

	private String order_pk;
	private List<AirTicketOrderLegBean> air_tickets;

	public String searchAirTicketOrderLegByOrderPk() {
		air_tickets = airTicketOrderService.selectAirTicketOrderLegByOrderPk(order_pk);
		return SUCCESS;
	}

	private List<String> passenger_pks;

	/**
	 * 判断操作的名单是否存在相同票务航段
	 * 
	 * @return
	 */
	public String checkSameAirLeg() {
		resultStr = passengerTicketInfoService.checkSameAirLeg(passenger_pks);
		return SUCCESS;
	}

	private List<String> team_numbers;

	@Autowired
	private OrderService orderService;

	/**
	 * 检查销售是否已经确认名单信息
	 * 
	 * @return
	 */
	public String checkSaleConfirm() {
		Set<String> ts = new HashSet<String>();
		for (String team_number : team_numbers) {
			OrderDto order = orderService.selectByTeamNumber(team_number);
			if (Integer.valueOf(order.getName_confirm_status()) < 5) {
				ts.add(team_number);
			}
		}
		if (ts.size() > 0) {
			resultStr = "";
			for (String t : ts) {
				resultStr += t + ",";
			}
		} else {
			resultStr = SUCCESS;
		}
		return SUCCESS;
	}

	public List<AirTicketNeedBean> getAirTicketNeeds() {
		return airTicketNeeds;
	}

	public void setAirTicketNeeds(List<AirTicketNeedBean> airTicketNeeds) {
		this.airTicketNeeds = airTicketNeeds;
	}

	public AirTicketNeedBean getAirTicketNeed() {
		return airTicketNeed;
	}

	public void setAirTicketNeed(AirTicketNeedBean airTicketNeed) {
		this.airTicketNeed = airTicketNeed;
	}

	public String getSale_order_pk() {
		return sale_order_pk;
	}

	public void setSale_order_pk(String sale_order_pk) {
		this.sale_order_pk = sale_order_pk;
	}

	public BigDecimal getAir_ticket_cost() {
		return air_ticket_cost;
	}

	public void setAir_ticket_cost(BigDecimal air_ticket_cost) {
		this.air_ticket_cost = air_ticket_cost;
	}

	public String getStandard_flg() {
		return standard_flg;
	}

	public void setStandard_flg(String standard_flg) {
		this.standard_flg = standard_flg;
	}

	public List<AirTicketOrderBean> getAirTicketOrders() {
		return airTicketOrders;
	}

	public void setAirTicketOrders(List<AirTicketOrderBean> airTicketOrders) {
		this.airTicketOrders = airTicketOrders;
	}

	public AirTicketOrderBean getAirTicketOrder() {
		return airTicketOrder;
	}

	public void setAirTicketOrder(AirTicketOrderBean airTicketOrder) {
		this.airTicketOrder = airTicketOrder;
	}

	public List<String> getAirTicketOrderPks() {
		return airTicketOrderPks;
	}

	public void setAirTicketOrderPks(List<String> airTicketOrderPks) {
		this.airTicketOrderPks = airTicketOrderPks;
	}

	public List<AirTicketNameListBean> getAirTicketNameList() {
		return airTicketNameList;
	}

	public void setAirTicketNameList(List<AirTicketNameListBean> airTicketNameList) {
		this.airTicketNameList = airTicketNameList;
	}

	public AirTicketNameListBean getPassenger() {
		return passenger;
	}

	public void setPassenger(AirTicketNameListBean passenger) {
		this.passenger = passenger;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public List<TicketAllotDto> getTicketAllots() {
		return ticketAllots;
	}

	public void setTicketAllots(List<TicketAllotDto> ticketAllots) {
		this.ticketAllots = ticketAllots;
	}

	public List<OrderAirInfoBean> getOrder_air_infos() {
		return order_air_infos;
	}

	public void setOrder_air_infos(List<OrderAirInfoBean> order_air_infos) {
		this.order_air_infos = order_air_infos;
	}

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public List<AirTicketOrderLegBean> getAir_tickets() {
		return air_tickets;
	}

	public void setAir_tickets(List<AirTicketOrderLegBean> air_tickets) {
		this.air_tickets = air_tickets;
	}

	public String getOrder_pk() {
		return order_pk;
	}

	public void setOrder_pk(String order_pk) {
		this.order_pk = order_pk;
	}

	public List<String> getPassenger_pks() {
		return passenger_pks;
	}

	public void setPassenger_pks(List<String> passenger_pks) {
		this.passenger_pks = passenger_pks;
	}

	public ProductOrderAirBaseBean getAir_base() {
		return air_base;
	}

	public void setAir_base(ProductOrderAirBaseBean air_base) {
		this.air_base = air_base;
	}

	public List<String> getTeam_numbers() {
		return team_numbers;
	}

	public void setTeam_numbers(List<String> team_numbers) {
		this.team_numbers = team_numbers;
	}

}
