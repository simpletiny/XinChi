package com.xinchi.backend.product.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.backend.product.dao.ProductNeedDAO;
import com.xinchi.backend.product.dao.ProductOrderDAO;
import com.xinchi.backend.product.dao.ProductOrderNameAirNeedDAO;
import com.xinchi.backend.product.dao.ProductOrderNameDAO;
import com.xinchi.backend.product.dao.ProductOrderNameFlightSegmentDAO;
import com.xinchi.backend.product.dao.ProductOrderTeamNumberDAO;
import com.xinchi.backend.product.service.ProductOrderService;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.dao.AirTicketNeedDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ProductNeedDto;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.bean.ProductOrderNameAirNeedBean;
import com.xinchi.bean.ProductOrderNameBean;
import com.xinchi.bean.ProductOrderNameFlightSegmentBean;
import com.xinchi.bean.ProductOrderTeamNumberBean;
import com.xinchi.bean.SaleOrderBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
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
	private OrderDAO orderDao;

	@Autowired
	private AirTicketNeedDAO airTicketNeedDao;

	@Autowired
	private ProductOrderTeamNumberDAO productOrderTeamNumberDao;

	@Autowired
	private NumberService numberService;

	@Autowired
	private ProductOrderNameDAO productOrderNameDao;

	@Override
	public String createProductOrder(String json) {

		JSONObject jsonObj = JSONObject.fromObject(json);
		String comment = jsonObj.getString("comment");

		String isCombine = jsonObj.getString("is_combine");
		JSONArray sale_order_status = jsonObj.getJSONArray("sale_order_status");

		List<String> team_numbers = new ArrayList<>();

		Map<String, String> t_d = new HashMap<>();
		for (int i = 0; i < sale_order_status.size(); i++) {
			JSONObject sale_obj = sale_order_status.getJSONObject(i);

			String team_number = sale_obj.getString("team_number");
			String is_only_dijie = sale_obj.getString("is_only_dijie");

			team_numbers.add(team_number);

			t_d.put(team_number, is_only_dijie);
		}

		// 生成产品订单
		String product_order_number = "";
		// 如果合并订单
		List<OrderDto> orders = orderDao.selectByTeamNumbers(team_numbers);
		int adult_count = 0;
		int special_count = 0;

		for (OrderDto order : orders) {
			adult_count += order.getAdult_count();
			special_count += order.getSpecial_count() == null ? 0 : order.getSpecial_count();
		}

		// 如果是合并订单
		if (isCombine.equals("Y")) {
			product_order_number = jsonObj.getString("product_order_number");
			ProductOrderBean product_order = dao.selectByOrderNumber(product_order_number);

			product_order.setSingle_flg("N");
			String combine_comment = StringUtils.left(product_order.getComment() + ";" + comment, 200);
			product_order.setComment(combine_comment);
			product_order.setAdult_count(product_order.getAdult_count() + adult_count);
			product_order.setSpecial_count(product_order.getSpecial_count() + special_count);

			// 更新产品订单信息
			dao.update(product_order);
		}
		// 如果是新生成订单
		else {
			// 生成产品订单
			product_order_number = numberService.generateProductOrderNumber();
			ProductOrderBean productOrder = new ProductOrderBean();
			productOrder.setOrder_number(product_order_number);

			// 是否为合单
			if (sale_order_status.size() > 1) {
				productOrder.setSingle_flg("N");
			}

			OrderDto o = orders.get(0);
			int days = o.getDays();
			String departure_date = o.getDeparture_date();
			String product_name = o.getProduct_name();
			String product_model = o.getProduct_model();
			String product_manager_number = o.getProduct_manager_number();
			String passenger_captain = o.getPassenger_captain();
			String standard_flg = o.getStandard_flg();
			String product_pk = o.getProduct_pk();

			productOrder.setDays(days);
			productOrder.setAdult_count(adult_count);
			productOrder.setSpecial_count(special_count);
			productOrder.setDeparture_date(departure_date);
			productOrder.setProduct_name(product_name);
			productOrder.setProduct_model(product_model);
			productOrder.setProduct_manager_number(product_manager_number);
			productOrder.setPassenger_captain(passenger_captain);
			productOrder.setProduct_pk(product_pk);

			productOrder.setComment(comment);

			productOrder.setStandard_flg(standard_flg);

			dao.insert(productOrder);
		}
		// 生成产品订单名单信息
		for (String team_number : t_d.keySet()) {
			String only_dijie = t_d.get(team_number);

			List<SaleOrderNameListBean> names = orderNameListDao.selectByTeamNumber(team_number);

			for (SaleOrderNameListBean name : names) {
				ProductOrderNameBean p_name = new ProductOrderNameBean();
				p_name.setName_pk(name.getPk());
				p_name.setProduct_order_number(product_order_number);
				p_name.setTeam_number(team_number);
				if (only_dijie.equals("Y")) {
					p_name.setOperate_status("D");
				}

				productOrderNameDao.insert(p_name);
			}
		}

		// 生成产品订单订单号和团号对应关系表
		for (String team_number : team_numbers) {

			ProductOrderTeamNumberBean aoatn = new ProductOrderTeamNumberBean();

			aoatn.setProduct_order_number(product_order_number);
			aoatn.setTeam_number(team_number);
			productOrderTeamNumberDao.insert(aoatn);
		}
		// 更新销售订单操作标识
		for (OrderDto order : orders) {
			SaleOrderBean sale_order = new SaleOrderBean();
			sale_order.setPk(order.getPk());
			sale_order.setOperate_flg(SimpletinyString.replaceCharFromLeft(order.getOperate_flg(),
					ResourcesConstants.ORDER_OPERATE_STATUS_ORDERED));
			orderDao.update(sale_order);
		}
		return SUCCESS;
	}

	@Override
	public String rollBackOrder(String order_number) {
		ProductOrderBean product_order = dao.selectByOrderNumber(order_number);
		// 检测是否已经发送票务需求
		ProductOrderNameBean option = new ProductOrderNameBean();
		option.setProduct_order_number(order_number);
		option.setOperate_status("Y");
		List<ProductOrderNameBean> names = productOrderNameDao.selectByParam(option);
		if (null != names && names.size() > 0) {
			List<String> name_pks = new ArrayList<>();
			for (ProductOrderNameBean name : names) {
				name_pks.add(name.getPk());
			}
			List<ProductOrderNameAirNeedBean> needs = productOrderNameAirNeedDao.selectByNamePks(name_pks);

			Set<String> need_pks = new HashSet<>();
			for (ProductOrderNameAirNeedBean need : needs) {
				need_pks.add(need.getNeed_pk());
			}

			List<AirTicketNeedBean> air_needs = airTicketNeedDao.selectByPks(new ArrayList<>(need_pks));
			for (AirTicketNeedBean atn : air_needs) {
				// 如果票务已经生成票务订单，则不能打回。
				if (atn.getOrdered().equals("Y")) {
					return "airlock";
				}
			}
			for (AirTicketNeedBean atn : air_needs) {
				// 如果没有生成票务订单
				if (atn != null) {
					// 删除票务需求和产品名单之间的对应关系
					productOrderNameAirNeedDao.deleteByNeedPk(atn.getPk());
					// 删除产品名单票务信息
					productOrderNameFlightSegmentDao.deleteByNeedPk(atn.getPk());
					// 删除票务需求
					airTicketNeedDao.delete(atn.getPk());
				}
			}
		}
		// 删除产品名单
		productOrderNameDao.deleteByProductOrderNumber(order_number);

		// 删除产品订单
		dao.delete(product_order.getPk());
		// 团号信息
		List<String> team_numbers = productOrderTeamNumberDao.selectTeamNumbersByOrderNumber(order_number);
		// 更新销售订单操作标识
		List<OrderDto> orders = orderDao.selectByTeamNumbers(team_numbers);
		for (OrderDto order : orders) {
			SaleOrderBean sale_order = new SaleOrderBean();
			sale_order.setPk(order.getPk());
			sale_order.setOperate_flg(
					ResourcesConstants.ORDER_OPERATE_STATUS_NO + "," + ResourcesConstants.ORDER_OPERATE_STATUS_NO);
			orderDao.update(sale_order);
		}
		// 删除产品订单号和团号对应关系
		productOrderTeamNumberDao.deleteByOrderNumber(order_number);
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

	@Override
	public List<OrderDto> searchSaleOrderInfoByProductOrderInfo(String order_number, String supplier_employee_pk) {
		List<ProductOrderTeamNumberBean> potns = new ArrayList<ProductOrderTeamNumberBean>();
		potns = productOrderTeamNumberDao.selectByOrderNumber(order_number);
		if (null == potns || potns.size() == 0)
			return null;
		List<String> team_numbers = new ArrayList<String>();
		for (ProductOrderTeamNumberBean potn : potns) {
			team_numbers.add(potn.getTeam_number());
		}

		OrderDto option = new OrderDto();
		option.setTeam_numbers(team_numbers);
		option.setSupplier_employee_pk(supplier_employee_pk);

		return orderDao.selectPayableInfoByParam(option);
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

	@Override
	public String changeOrderLock(String team_number, String lock_flg) {
		OrderDto order = orderDao.selectByTeamNumber(team_number);
		SaleOrderBean sale_order = new SaleOrderBean();
		sale_order.setPk(order.getPk());
		sale_order.setLock_flg(SimpletinyString.replaceCharFromLeft(order.getLock_flg(), lock_flg, 1));
		orderDao.update(sale_order);
		return SUCCESS;
	}

	@Override
	public String isAllOrdersLocked(String order_number) {
		String numbers[] = order_number.split(",");
		for (String number : numbers) {
			List<ProductOrderTeamNumberBean> potns = new ArrayList<ProductOrderTeamNumberBean>();
			potns = productOrderTeamNumberDao.selectByOrderNumber(number);

			if (null == potns || potns.size() == 0)
				continue;

			List<String> team_numbers = new ArrayList<String>();
			for (ProductOrderTeamNumberBean potn : potns) {
				team_numbers.add(potn.getTeam_number());
			}

			List<OrderDto> orders = orderDao.selectByTeamNumbers(team_numbers);

			for (OrderDto order : orders) {
				if (order.getLock_flg().substring(0, 1).equals("N")) {
					return "no," + number;
				}
			}
		}

		return "yes";
	}

	@Override
	public ProductOrderBean selectByOrderNumber(String order_number) {
		return dao.selectByOrderNumber(order_number);
	}

	@Autowired
	private AirTicketNameListDAO airTicketNameListDao;

	@Override
	public List<AirTicketNameListBean> searchTicketInfoByOrderNumber(String order_number) {
		List<ProductOrderTeamNumberBean> potns = new ArrayList<ProductOrderTeamNumberBean>();
		potns = productOrderTeamNumberDao.selectByOrderNumber(order_number);

		if (null == potns || potns.size() == 0)
			return null;

		List<String> team_numbers = new ArrayList<String>();
		for (ProductOrderTeamNumberBean potn : potns) {
			team_numbers.add(potn.getTeam_number());
		}

		return airTicketNameListDao.selectWithInfoByTeamNumbers(team_numbers);
	}

	@Override
	public List<ProductOrderBean> selectByParam(ProductOrderBean option) {
		return dao.selectByParam(option);
	}

	@Override
	public List<ProductOrderNameBean> searchProductOrderNameByPage(Page<ProductOrderNameBean> page) {
		return productOrderNameDao.selectByPage(page);
	}

	@Autowired
	private ProductOrderNameFlightSegmentDAO productOrderNameFlightSegmentDao;

	@Autowired
	private ProductOrderNameAirNeedDAO productOrderNameAirNeedDao;

	@Override
	public String sendAirTicketNeed(String json) {
		JSONObject jsonObj = JSONObject.fromObject(json);
		String has_ticket = jsonObj.getString("has_ticket");
		List<String> name_pks = new ArrayList<String>();
		JSONArray json_name_pks = jsonObj.getJSONArray("name_pks");
		for (int i = 0; i < json_name_pks.size(); i++) {
			name_pks.add(json_name_pks.getString(i));
		}
		List<ProductOrderNameBean> names = productOrderNameDao.selectByPks(name_pks);
		ProductOrderNameBean captain = names.get(0);
		// 如果有票务信息，则发送票务需求
		if (has_ticket.equals("YES")) {
			String air_comment = jsonObj.getString("air_comment");

			String first_from_to = "";
			int fly_day = 0;

			JSONArray data = jsonObj.getJSONArray("data");

			for (int i = 0; i < data.size(); i++) {
				JSONObject obj = data.getJSONObject(i);
				int flight_index = obj.getInt("flight_index");
				int start_day = obj.getInt("start_day");
				String from_to_city = obj.getString("from_to_city");

				if (SimpletinyString.isEmpty(first_from_to) && flight_index == 1) {
					first_from_to += from_to_city;
					fly_day = start_day;
				}
			}
			int adult_count = 0;
			int special_count = 0;
			for (ProductOrderNameBean name : names) {
				if (isAdult(name)) {
					adult_count++;
				} else {
					special_count++;
				}
			}

			// 保存票务需求
			AirTicketNeedBean atn = new AirTicketNeedBean();
			String first_ticket_date = "";
			String ticket_client_number = "";

			atn.setPassenger_captain(captain.getName());
			atn.setComment(air_comment);
			atn.setProduct_name(captain.getProduct_name());
			atn.setDeparture_date(captain.getDeparture_date());
			atn.setAdult_cnt(adult_count);
			atn.setSpecial_cnt(special_count);
			atn.setFirst_from_to(first_from_to);
			atn.setProduct_order_number(captain.getProduct_order_number());
			ticket_client_number = captain.getProduct_manager_number();

			first_ticket_date = DateUtil.addDate(captain.getDeparture_date(), fly_day - 1);
			atn.setTicket_client_number(ticket_client_number);
			atn.setFirst_ticket_date(first_ticket_date);
			String need_pk = airTicketNeedDao.insert(atn);

			// 保存产品订单名单航段信息
			for (int i = 0; i < data.size(); i++) {
				JSONObject obj = data.getJSONObject(i);

				int flight_index = obj.getInt("flight_index");
				String flight_number = obj.getString("flight_number");
				int start_day = obj.getInt("start_day");
				String from_to_city = obj.getString("from_to_city");

				ProductOrderNameFlightSegmentBean flight = new ProductOrderNameFlightSegmentBean();

				flight.setTicket_index(flight_index);
				flight.setTicket_date(DateUtil.addDate(captain.getDeparture_date(), start_day - 1));
				flight.setFrom_to_city(from_to_city);
				flight.setTicket_number(flight_number);
				flight.setNeed_pk(need_pk);
				flight.setAir_comment(air_comment);
				productOrderNameFlightSegmentDao.insert(flight);
			}

			// 更新名单状态
			for (ProductOrderNameBean name : names) {
				// 保存名单和需求对应关系
				ProductOrderNameAirNeedBean name_need = new ProductOrderNameAirNeedBean();
				name_need.setName_pk(name.getPk());
				name_need.setNeed_pk(need_pk);
				productOrderNameAirNeedDao.insert(name_need);
				// 更新名单操作信息
				name.setOperate_status("Y");
				productOrderNameDao.update(name);
			}
		}
		// 如果没有票务需求，则标记名单为单地接
		else {
			for (ProductOrderNameBean name : names) {
				name.setOperate_status("D");
				productOrderNameDao.update(name);
			}
		}
		return SUCCESS;
	}

	private boolean isAdult(ProductOrderNameBean name) {
		if (name.getId_type().equals("P")) {
			return true;
		} else if (name.getId_type().equals("I")) {
			if (name.getId().length() != 18 || !Character.isDigit(name.getId().charAt(0)))
				return true;
			String birthDateString = name.getId().substring(6, 14);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate birthDate = LocalDate.parse(birthDateString, formatter);

			DateTimeFormatter checkDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate checkDate = LocalDate.parse(name.getDeparture_date(), checkDateFormatter);
			// 计算年龄
			int age = Period.between(birthDate, checkDate).getYears();
			return age >= 18;
		} else {
			return true;
		}
	}

	@Override
	public Map<String, List<ProductOrderNameFlightSegmentBean>> searchAirNeedByNamePk(String name_pk) {
		List<ProductOrderNameAirNeedBean> nans = productOrderNameAirNeedDao.selectByNamePk(name_pk);
		Map<String, List<ProductOrderNameFlightSegmentBean>> result = new HashMap<>();
		for (ProductOrderNameAirNeedBean nan : nans) {
			String need_pk = nan.getNeed_pk();
			List<ProductOrderNameFlightSegmentBean> nfs = productOrderNameFlightSegmentDao.selectByNeedPk(need_pk);
			result.put(DateUtil.fromUnixTime(nan.getCreate_time(), DateUtil.YYYY_MM_DD_HH_MM), nfs);
		}

		return result;
	}

	@Override
	public List<ProductOrderNameBean> selectProductOrderNameByAirNeedPk(String air_need_pk) {
		return productOrderNameDao.selectByNeedPk(air_need_pk);
	}

	@Override
	public String deleteSendedAirNeed(String air_need_pk) {
		AirTicketNeedBean air_need = airTicketNeedDao.selectByPk(air_need_pk);
		if (air_need.getOrdered().equals("Y")) {
			return "票务已操作，无法删除！";
		} else {
			// 删除票务需求
			airTicketNeedDao.delete(air_need_pk);
			// 删除票务需求和名单对应信息
			productOrderNameAirNeedDao.deleteByNeedPk(air_need_pk);
			// 删除已发送航段信息
			productOrderNameFlightSegmentDao.deleteByNeedPk(air_need_pk);
			// 查验名单是否还有票务需求，如果没有了则把名单操作状态更新至待发票务
			List<ProductOrderNameBean> product_order_names = productOrderNameDao.selectByNeedPk(air_need_pk);

			for (ProductOrderNameBean name : product_order_names) {
				List<ProductOrderNameAirNeedBean> nans = productOrderNameAirNeedDao.selectByNamePk(name.getPk());
				if (null == nans || nans.size() < 1) {
					ProductOrderNameBean update_name = new ProductOrderNameBean();
					update_name.setPk(name.getPk());
					update_name.setOperate_status("N");
					update_name.setTicked("N");
					productOrderNameDao.update(update_name);
				}
			}

			return SUCCESS;
		}
	}

	@Override
	public List<SaleOrderNameListBean> searchSaleOrderNameListByProductOrderNumbers(List<String> order_numbers) {
		List<String> team_numbers = new ArrayList<String>();
		for (String order_number : order_numbers) {
			List<String> tns = productOrderTeamNumberDao.selectTeamNumbersByOrderNumber(order_number);
			if (null != tns && tns.size() > 0) {
				team_numbers.addAll(tns);
			}
		}
		return orderNameListDao.selectByTeamNumbers(team_numbers);
	}

	@Override
	/**
	 * A:完全出票。B：部分出票。C：有未发送。D：未出票
	 */
	public String searchProductOrderTicketStatusByOrderNumber(String product_order_number) {
		ProductOrderNameBean option = new ProductOrderNameBean();
		option.setProduct_order_number(product_order_number);
		List<ProductOrderNameBean> orderNames = productOrderNameDao.selectByParam(option);
		List<String> operate_statuses = new ArrayList<>();
		List<String> tickeds = new ArrayList<>();
		Set<String> team_numbers = new HashSet<>();
		for (ProductOrderNameBean name : orderNames) {
			if (!name.getOperate_status().equals("D")) {
				operate_statuses.add(name.getOperate_status());
				tickeds.add(name.getTicked());
			}
			team_numbers.add(name.getTeam_number());
		}

		List<String> t_ns = new ArrayList<>(team_numbers);

		if (operate_statuses.contains("N")) {
			return "C";
		}

		if (tickeds.contains("N") && tickeds.contains("Y")) {
			return "B";
		}

		if (tickeds.contains("N") && !tickeds.contains("Y")) {
			return "D";
		}

		List<String> air_statuses = new ArrayList<>();
		List<AirTicketNameListBean> airNames = airTicketNameListDao.selectByTeamNumbers(t_ns);

		if (null == airNames || airNames.size() < 1) {
			return "D";
		}

		for (AirTicketNameListBean name : airNames) {
			air_statuses.add(name.getStatus());
		}

		if (air_statuses.contains("I") && (air_statuses.contains("Y") || air_statuses.contains("C"))) {
			return "B";
		}

		if (air_statuses.contains("I") && !(air_statuses.contains("Y") || air_statuses.contains("C"))) {
			return "D";
		}
		if (!air_statuses.contains("I") && (air_statuses.contains("Y") || air_statuses.contains("C"))) {
			return "A";
		}
		return "NO";
	}
}