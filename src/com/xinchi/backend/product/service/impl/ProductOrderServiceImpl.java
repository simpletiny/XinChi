package com.xinchi.backend.product.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.backend.product.dao.ProductNeedDAO;
import com.xinchi.backend.product.dao.ProductOrderAirInfoDAO;
import com.xinchi.backend.product.dao.ProductOrderDAO;
import com.xinchi.backend.product.dao.ProductOrderTeamNumberDAO;
import com.xinchi.backend.product.service.ProductOrderService;
import com.xinchi.backend.ticket.dao.AirNeedTeamNumberDAO;
import com.xinchi.backend.ticket.dao.AirTicketNeedDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirNeedTeamNumberBean;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ProductNeedDto;
import com.xinchi.bean.ProductOrderAirInfoBean;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.bean.ProductOrderTeamNumberBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ProductOrderServiceImpl implements ProductOrderService {

	@Autowired
	private ProductNeedDAO needDao;

	@Override
	public List<ProductNeedDto> selectNeedByPage(Page<ProductNeedDto> page) {

		return needDao.selectByPage(page);
	}

	@Autowired
	private ProductOrderDAO dao;

	@Autowired
	private ProductOrderAirInfoDAO airInfoDao;

	@Autowired
	private OrderDAO orderDao;

	@Autowired
	private AirTicketNeedDAO airTicketNeedDao;

	@Autowired
	private AirNeedTeamNumberDAO airNeedTeamNumberDao;

	@Autowired
	private ProductOrderTeamNumberDAO productOrderTeamNumberDao;

	@Autowired
	private BudgetStandardOrderDAO bsoDao;

	@Autowired
	private BudgetNonStandardOrderDAO bnsoDao;

	@Autowired
	private NumberService numberService;

	@Autowired
	private ProductDAO productDao;

	@Override
	public String createProductOrder(String json) {

		JSONObject jsonObj = JSONObject.fromObject(json);

		String air_comment = jsonObj.getString("air_comment");
		String comment = jsonObj.getString("comment");
		String team_numbers = jsonObj.getString("team_numbers");
		String has_ticket = jsonObj.getString("has_ticket");

		// 生成产品订单
		String product_order_number = numberService.generateProductOrderNumber();

		ProductOrderBean productOrder = new ProductOrderBean();
		productOrder.setOrder_number(product_order_number);

		String[] ts = team_numbers.split(",");

		if (ts.length > 1) {
			productOrder.setSingle_flg("N");
		}
		List<OrderDto> orders = orderDao.selectByTeamNumbers(Arrays.asList(ts));
		int adult_count = 0;
		int special_count = 0;

		OrderDto o = orders.get(0);
		int days = o.getDays();
		String departure_date = o.getDeparture_date();
		String product_name = o.getProduct_name();
		String product_model = "";
		String product_manager_number = o.getProduct_manager_number();
		String passenger_captain = o.getPassenger_captain();
		String standard_flg = o.getStandard_flg();

		String product_pk = o.getProduct_pk();
		if (!SimpletinyString.isEmpty(product_pk)) {
			ProductBean product = productDao.selectByPrimaryKey(product_pk);
			product_model = product.getProduct_model();
		}

		for (OrderDto order : orders) {
			adult_count += order.getAdult_count();
			special_count += order.getSpecial_count() == null ? 0 : order.getSpecial_count();
		}

		productOrder.setDays(days);
		productOrder.setAdult_count(adult_count);
		productOrder.setSpecial_count(special_count);
		productOrder.setDeparture_date(departure_date);
		productOrder.setProduct_name(product_name);
		productOrder.setProduct_model(product_model);
		productOrder.setProduct_manager_number(product_manager_number);
		productOrder.setPassenger_captain(passenger_captain);
		productOrder.setProduct_pk(product_pk);

		productOrder.setAir_comment(air_comment);
		productOrder.setComment(comment);

		productOrder.setStandard_flg(standard_flg);

		dao.insert(productOrder);

		String first_from_to = "";
		int fly_day = 0;

		JSONArray arr = jsonObj.getJSONArray("data");

		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			int flight_index = obj.getInt("flight_index");

			int start_day = obj.getInt("start_day");
			String start_city = obj.getString("start_city");
			String end_city = obj.getString("end_city");

			if (SimpletinyString.isEmpty(first_from_to) && flight_index == 1) {
				first_from_to += start_city + "-" + end_city;
				fly_day = start_day;
			}

			ProductOrderAirInfoBean info = new ProductOrderAirInfoBean();

			info.setProduct_order_number(product_order_number);
			info.setFlight_index(flight_index);
			info.setStart_day(start_day);
			info.setStart_city(start_city);
			info.setEnd_city(end_city);

			airInfoDao.insert(info);
		}

		// 如果有票务信息生成一条票务需求数据
		if (has_ticket.equals("YES")) {

			AirTicketNeedBean atn = new AirTicketNeedBean();

			String first_ticket_date = "";
			String ticket_client_number = "";

			atn.setPassenger_captain(passenger_captain);
			atn.setComment(comment);
			atn.setProduct_name(product_name);
			atn.setDeparture_date(departure_date);
			atn.setAdult_cnt(adult_count);
			atn.setSpecial_cnt(special_count);
			atn.setFirst_from_to(first_from_to);
			atn.setProduct_order_number(product_order_number);

			UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
					.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
			ticket_client_number = sessionBean.getUser_number();

			first_ticket_date = DateUtil.addDate(departure_date, fly_day - 1);
			atn.setTicket_client_number(ticket_client_number);
			atn.setFirst_ticket_date(first_ticket_date);

			String need_pk = airTicketNeedDao.insert(atn);
			for (String team_number : ts) {
				// 生成团号和票务需求对应关系表
				AirNeedTeamNumberBean ant = new AirNeedTeamNumberBean();
				ant.setNeed_pk(need_pk);
				ant.setTeam_number(team_number);
				airNeedTeamNumberDao.insert(ant);

				// 生成产品订单票务信息和团号对应关系表
				ProductOrderTeamNumberBean aoatn = new ProductOrderTeamNumberBean();

				aoatn.setProduct_order_number(product_order_number);
				aoatn.setTeam_number(team_number);
				productOrderTeamNumberDao.insert(aoatn);
			}
		}

		// 更新销售订单操作标识
		for (OrderDto order : orders) {

			if (order.getStandard_flg().equals("Y")) {
				BudgetStandardOrderBean bsOrder = bsoDao.selectByPrimaryKey(order.getPk());
				bsOrder.setOperate_flg(ResourcesConstants.ORDER_OPERATE_STATUS_ORDERED);
				bsoDao.update(bsOrder);
			} else {
				BudgetNonStandardOrderBean bnsOrder = bnsoDao.selectByPrimaryKey(order.getPk());
				bnsOrder.setOperate_flg(ResourcesConstants.ORDER_OPERATE_STATUS_ORDERED);
				bnsoDao.update(bnsOrder);
			}
		}

		return SUCCESS;
	}

	@Override
	public List<ProductOrderBean> selectByPage(Page page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<OrderDto> searchSaleOrderByProductOrderNumber(String order_number) {
		List<ProductOrderTeamNumberBean> potns = new ArrayList<ProductOrderTeamNumberBean>();
		potns = productOrderTeamNumberDao.selectByOrderNumber(order_number);

		if (null == potns || potns.size() == 0)
			return null;

		List<String> team_numbers = new ArrayList<String>();
		for (ProductOrderTeamNumberBean potn : potns) {
			team_numbers.add(potn.getTeam_number());
		}

		return orderDao.selectByTeamNumbers(team_numbers);

	}

	@Autowired
	private OrderNameListDAO orderNameListDao;

	@Override
	public List<SaleOrderNameListBean> searchSaleOrderNameListByProductOrderNumber(String order_number) {
		List<ProductOrderTeamNumberBean> potns = new ArrayList<ProductOrderTeamNumberBean>();
		potns = productOrderTeamNumberDao.selectByOrderNumber(order_number);

		List<String> team_numbers = new ArrayList<String>();

		if (null == potns || potns.size() == 0) {
			team_numbers.add(order_number);
		} else {
			for (ProductOrderTeamNumberBean potn : potns) {
				team_numbers.add(potn.getTeam_number());
			}
		}

		return orderNameListDao.selectByTeamNumbers(team_numbers);
	}
}