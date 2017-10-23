package com.xinchi.backend.ticket.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.order.service.BudgetNonStandardOrderService;
import com.xinchi.backend.order.service.BudgetStandardOrderService;
import com.xinchi.backend.order.service.OrderNameListService;
import com.xinchi.backend.payable.service.AirTicketPayableService;
import com.xinchi.backend.product.service.ProductAirTicketService;
import com.xinchi.backend.product.service.ProductService;
import com.xinchi.backend.ticket.service.AirTicketNameListService;
import com.xinchi.backend.ticket.service.AirTicketNeedService;
import com.xinchi.backend.ticket.service.AirTicketOrderService;
import com.xinchi.backend.ticket.service.PassengerTicketInfoService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.PassengerTicketInfoBean;
import com.xinchi.bean.ProductAirTicketBean;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.TicketAllotDto;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;

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

	private String sale_order_pk;
	private BigDecimal air_ticket_cost;
	private String standard_flg;
	@Autowired
	private BudgetStandardOrderService bsoService;

	@Autowired
	private BudgetNonStandardOrderService bnsoService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductAirTicketService productAirTicketService;

	@Autowired
	private AirTicketOrderService airTicketOrderService;

	public String createTicketOrder() {
		AirTicketOrderBean airTicketOrder = new AirTicketOrderBean();

		if (standard_flg.equals("Y")) {
			BudgetStandardOrderBean bsOrder = bsoService.selectByPrimaryKey(sale_order_pk);
			if (bsOrder.getName_list() == null) {
				resultStr = "没有名单不能生成订单";
				return SUCCESS;
			}
			bsOrder.setAir_ticket_cost(air_ticket_cost);
			bsoService.updateComment(bsOrder);
			String client_number = "";
			// 销售的产品
			ProductBean saleProduct = productService.selectByPrimaryKey(bsOrder.getProduct_pk());
			if (saleProduct.getAir_ticket_charge().equals(ResourcesConstants.PRODUCT_AIR_TICKET_CHARGE_PRODUCT)) {
				client_number = saleProduct.getCreate_user();
			} else if (saleProduct.getAir_ticket_charge().equals(ResourcesConstants.PRODUCT_AIR_TICKET_CHARGE_SALE)) {
				client_number = bsOrder.getCreate_user();
			} else if (saleProduct.getAir_ticket_charge().equals(ResourcesConstants.PRODUCT_AIR_TICKET_CHARGE_NONE)) {
				client_number = "NONE";
			}
			// 产品机票信息
			List<ProductAirTicketBean> productAirTickets = productAirTicketService.selectByProductPk(saleProduct.getPk());
			ProductAirTicketBean firstTicketInfo = productAirTickets.get(0);

			airTicketOrder.setClient_number(client_number);
			airTicketOrder.setTicket_cost(air_ticket_cost);
			airTicketOrder.setFirst_ticket_date(DateUtil.addDate(bsOrder.getDeparture_date(), firstTicketInfo.getStart_day() - 1));
			airTicketOrder.setFirst_start_city(firstTicketInfo.getStart_city());
			airTicketOrder.setFirst_end_city(firstTicketInfo.getEnd_city());
			airTicketOrder.setPeople_count((bsOrder.getAdult_count() == null ? 0 : bsOrder.getAdult_count())
					+ (bsOrder.getSpecial_count() == null ? 0 : bsOrder.getSpecial_count()));
			airTicketOrder.setTeam_number(bsOrder.getTeam_number());
			airTicketOrder.setTour_product_pk(saleProduct.getPk());
			airTicketOrder.setSale_order_pk(bsOrder.getPk());

		} else {
			BudgetNonStandardOrderBean bnsOrder = bnsoService.selectByPrimaryKey(sale_order_pk);
			if (bnsOrder.getName_list() == null) {
				resultStr = "没有名单不能生成订单";
				return SUCCESS;
			}
			bnsOrder.setPk(sale_order_pk);
			bnsOrder.setAir_ticket_cost(air_ticket_cost);
			bnsoService.updateComment(bnsOrder);

			airTicketOrder.setClient_number(bnsOrder.getCreate_user());
			airTicketOrder.setTicket_cost(air_ticket_cost);
			airTicketOrder.setPeople_count((bnsOrder.getAdult_count() == null ? 0 : bnsOrder.getAdult_count())
					+ (bnsOrder.getSpecial_count() == null ? 0 : bnsOrder.getSpecial_count()));
			airTicketOrder.setTeam_number(bnsOrder.getTeam_number());
			airTicketOrder.setSale_order_pk(bnsOrder.getPk());

		}
		airTicketOrder.setSale_standard_flg(standard_flg);
		airTicketOrderService.insert(airTicketOrder);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	private List<String> airTicketOrderPks;

	@Autowired
	private OrderNameListService orderNameListService;

	@Autowired
	private AirTicketNameListService airTicketNameListService;
	private List<AirTicketNameListBean> airTicketNameList;

	/**
	 * 锁定名单操作，其实就是拆分名单
	 * 
	 * @return
	 */
	public String lockAirTicketOrder() {
		List<AirTicketOrderBean> ticketOrders = airTicketOrderService.selectByPks(airTicketOrderPks);
		airTicketNameList = new ArrayList<AirTicketNameListBean>();

		for (AirTicketOrderBean order : ticketOrders) {
			// 已经锁定的不参与
			if (order.getLock_flg().equals("1"))
				continue;

			List<SaleOrderNameListBean> nameList = new ArrayList<SaleOrderNameListBean>();
			if (null == order.getTeam_number()) {
				String names = "";
				if (order.getSale_standard_flg().equals("Y")) {
					BudgetStandardOrderBean bsOrder = bsoService.selectByPrimaryKey(order.getSale_order_pk());
					names = bsOrder.getName_list();
				} else {
					BudgetNonStandardOrderBean bnsOrder = bnsoService.selectByPrimaryKey(order.getSale_order_pk());
					names = bnsOrder.getName_list();
				}
				String[] arrName = names.split(";");
				for (String name : arrName) {

					String[] info = name.split(":");
					if (info.length < 2)
						continue;
					SaleOrderNameListBean nn = new SaleOrderNameListBean();
					nn.setName(info[0].trim());
					nn.setId(info[1].trim());
					nameList.add(nn);
				}

			} else {
				nameList = orderNameListService.selectByTeamNumber(order.getTeam_number());
			}

			for (SaleOrderNameListBean name : nameList) {
				AirTicketNameListBean nn = new AirTicketNameListBean();
				nn.setTeam_number(order.getTeam_number());
				nn.setClient_number(order.getClient_number());
				nn.setFirst_ticket_date(order.getFirst_ticket_date());
				nn.setFirst_start_city(order.getFirst_start_city());
				nn.setFirst_end_city(order.getFirst_end_city());
				nn.setTicket_order_pk(order.getPk());
				nn.setName(name.getName());
				nn.setId(name.getId());
				nn.setSale_product_pk(order.getTour_product_pk());
				airTicketNameList.add(nn);
			}
			order.setLock_flg("1");
			airTicketOrderService.update(order);
		}

		resultStr = airTicketNameListService.insertList(airTicketNameList);
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
		}

		return SUCCESS;
	}

	@Autowired
	private PassengerTicketInfoService passengerTicketInfoService;

	@Autowired
	private AirTicketPayableService airTicketPayableService;

	public String allotTicket() {
		JSONArray arr = JSONArray.fromObject(json);
		List<PassengerTicketInfoBean> ptis = new ArrayList<PassengerTicketInfoBean>();

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
			airTicketPayable.setBudget_payable(null != ticket_cost ? new BigDecimal(ticket_cost) : BigDecimal.ZERO);
			airTicketPayable.setPNR(ticket_PNR);
			airTicketPayable.setBudget_balance(null != ticket_cost ? new BigDecimal(ticket_cost) : BigDecimal.ZERO);
			airTicketPayable.setPaid(BigDecimal.ZERO);

			airTicketPayableService.insert(airTicketPayable);

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
					ptis.add(pti);
				}

			}
		}

		resultStr = passengerTicketInfoService.insertList(ptis);
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

}
