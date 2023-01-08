package com.xinchi.backend.order.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.backend.order.dao.OrderReportDAO;
import com.xinchi.backend.order.dao.OrderTicketInfoDAO;
import com.xinchi.backend.order.service.BudgetNonStandardOrderService;
import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.sys.dao.BaseDataDAO;
import com.xinchi.backend.ticket.dao.AirNeedTeamNumberDAO;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.dao.AirTicketNeedDAO;
import com.xinchi.backend.user.service.UserService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirNeedTeamNumberBean;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.BaseDataBean;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.SaleOrderTicketInfoBean;
import com.xinchi.bean.TeamReportBean;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class BudgetNonStandardOrderServiceImpl implements BudgetNonStandardOrderService {

	@Autowired
	private BudgetNonStandardOrderDAO dao;

	@Override
	public String createOrder(BudgetNonStandardOrderBean bean, String json) {
		// 保存确认文件
		if (!SimpletinyString.isEmpty(bean.getConfirm_file())) {
			saveFile(bean);
		}
		String order_pk = DBCommonUtil.genPk();
		String passenger_captain = "";
		bean.setPk(order_pk);

		JSONArray nameList = JSONArray.fromObject(json);
		for (int i = 0; i < nameList.size(); i++) {
			JSONObject obj = JSONObject.fromObject(nameList.get(i));
			String chairman = obj.getString("chairman");
			int name_index = obj.getInt("index");
			String name = obj.getString("name");
			String sex = obj.getString("sex");
			String cellphone_A = obj.getString("cellphone_A");
			String cellphone_B = obj.getString("cellphone_B");
			String id = obj.getString("id");

			SaleOrderNameListBean passenger = new SaleOrderNameListBean();
			passenger.setName(name);
			passenger.setChairman(chairman);
			if (!SimpletinyString.isEmpty(chairman) && chairman.equals("Y")) {
				passenger_captain = name;
			}
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setCellphone_A(cellphone_A);
			passenger.setCellphone_B(cellphone_B);
			passenger.setId(id);
			passenger.setOrder_pk(bean.getPk());
			nameListDao.insert(passenger);
		}

		bean.setPassenger_captain(passenger_captain);
		dao.insertWithPk(bean);

		return SUCCESS;
	}

	@Autowired
	private OrderTicketInfoDAO orderTicketInfoDao;

	@Override
	public String createOnlyTicketOrder(BudgetNonStandardOrderBean bean, String json) {
		// 保存确认文件
		if (!SimpletinyString.isEmpty(bean.getConfirm_file())) {
			saveFile(bean);
		}

		String order_pk = DBCommonUtil.genPk();
		String passenger_captain = "";
		bean.setPk(order_pk);
		bean.setProduct_name("单机票");

		JSONObject obj = JSONObject.fromObject(json);

		// 保存名单信息
		JSONArray nameList = obj.getJSONArray("name_json");
		for (int i = 0; i < nameList.size(); i++) {
			JSONObject name_obj = JSONObject.fromObject(nameList.get(i));
			String chairman = name_obj.getString("chairman");
			int name_index = name_obj.getInt("index");
			String name = name_obj.getString("name");
			String sex = name_obj.getString("sex");
			String cellphone_A = name_obj.getString("cellphone_A");
			String cellphone_B = name_obj.getString("cellphone_B");
			String id = name_obj.getString("id");

			SaleOrderNameListBean passenger = new SaleOrderNameListBean();
			passenger.setName(name);
			passenger.setChairman(chairman);
			if (!SimpletinyString.isEmpty(chairman) && chairman.equals("Y")) {
				passenger_captain = name;
			}
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setCellphone_A(cellphone_A);
			passenger.setCellphone_B(cellphone_B);
			passenger.setId(id);
			passenger.setOrder_pk(bean.getPk());
			nameListDao.insert(passenger);
		}
		bean.setPassenger_captain(passenger_captain);

		// 保存航班信息
		JSONArray ticketList = obj.getJSONArray("ticket_json");
		String air_comment = obj.getString("air_comment");
		for (int i = 0; i < ticketList.size(); i++) {
			JSONObject ticket_obj = JSONObject.fromObject(ticketList.get(i));

			int index = ticket_obj.getInt("index");
			String date = ticket_obj.getString("date");
			String from_city = ticket_obj.getString("from_city");
			String to_city = ticket_obj.getString("to_city");

			SaleOrderTicketInfoBean soti = new SaleOrderTicketInfoBean();
			soti.setTicket_index(index);
			soti.setTicket_date(date);
			soti.setFrom_city(from_city);
			soti.setTo_city(to_city);
			soti.setOrder_pk(order_pk);

			if (index == 1)
				soti.setComment(air_comment);

			orderTicketInfoDao.insert(soti);
		}

		dao.insertWithPk(bean);

		return SUCCESS;
	}

	@Autowired
	private NumberService numberService;
	@Autowired
	private ReceivableDAO receivableDao;

	@Autowired
	private OrderNameListDAO nameListDao;

	@Autowired
	private BaseDataDAO baseDataDao;
	@Autowired
	private OrderReportDAO orderReportDao;

	@Autowired
	private UserService userService;

	@Override
	public String update(BudgetNonStandardOrderBean bean, String json) {
		BudgetNonStandardOrderBean old = dao.selectByPrimaryKey(bean.getPk());

		if (bean.getConfirm_flg().equals("Y")) {

			// 判断是否有信用余额确认订单
			boolean canConfirm = userService.hasEnoughCreditToConfirm(old.getReceivable_first_flg(),
					old.getCreate_user(), old.getTeam_number(), bean.getReceivable());

			if (!canConfirm) {
				return "noenoughcredit";
			}

			if (SimpletinyString.isEmpty(bean.getTeam_number())) {
				bean.setTeam_number(numberService.generateTeamNumber());
			}

			bean.setLock_flg("Y");

			String departureDate = bean.getDeparture_date();
			int days = bean.getDays();
			int people_count = bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count());
			String returnDate = DateUtil.addDate(departureDate, days - 1);

			// 如果已经生成了应收款，则更新应收款
			if (old.getReceivable_first_flg().equals("Y")) {
				ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(bean.getTeam_number());

				receivable.setDeparture_date(bean.getDeparture_date());
				receivable.setReturn_date(returnDate);
				receivable.setProduct(bean.getProduct_name());
				receivable.setPeople_count(people_count);
				receivable.setBudget_receivable(bean.getReceivable());
				BigDecimal received = receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived();
				receivable.setBudget_balance(bean.getReceivable().subtract(received));
				receivable.setSales(old.getCreate_user());
				receivable.setCreate_user(old.getCreate_user());

				receivableDao.update(receivable);
			}
			// 如果没有生成应收款，则生成应收款
			else {
				ReceivableBean receivable = new ReceivableBean();
				receivable.setTeam_number(bean.getTeam_number());
				receivable.setFinal_flg("N");
				receivable.setClient_employee_pk(bean.getClient_employee_pk());

				receivable.setDeparture_date(bean.getDeparture_date());
				receivable.setReturn_date(returnDate);
				receivable.setProduct(bean.getProduct_name());
				receivable.setPeople_count(people_count);
				receivable.setBudget_receivable(bean.getReceivable());

				receivable.setBudget_balance(bean.getReceivable());
				receivable.setReceived(BigDecimal.ZERO);
				receivable.setSales(old.getCreate_user());
				receivable.setCreate_user(old.getCreate_user());

				receivableDao.insert(receivable);
			}

			// 生成team_report基础数据
			TeamReportBean tr = new TeamReportBean();
			tr.setTeam_number(bean.getTeam_number());

			BaseDataBean option = baseDataDao.selectByPk(ResourcesConstants.BASE_DATA_PK_TEAM);
			BigDecimal sale_cost = new BigDecimal(0);
			BigDecimal sys_cost = new BigDecimal(option.getExt2()).multiply(new BigDecimal(people_count)).setScale(2,
					BigDecimal.ROUND_UP);

			tr.setSale_cost(sale_cost);
			tr.setSys_cost(sys_cost);

			orderReportDao.insert(tr);
		}

		bean.setCreate_user(old.getCreate_user());
		if (!SimpletinyString.isEmpty(bean.getConfirm_file())) {
			if (!old.getConfirm_file().equals(bean.getConfirm_file())) {
				deleteFile(old);
				saveFile(bean);
			}
		} else {
			deleteFile(old);
		}

		// 修改名单
		// 删除之前的名单
		nameListDao.deleteByOrderPk(bean.getPk());

		String passenger_captain = "";
		// 保存现在的名单
		JSONArray nameList = JSONArray.fromObject(json);
		for (int i = 0; i < nameList.size(); i++) {
			JSONObject obj = JSONObject.fromObject(nameList.get(i));
			String chairman = obj.getString("chairman");
			int name_index = obj.getInt("index");
			String name = obj.getString("name");
			String sex = obj.getString("sex");
			String cellphone_A = obj.getString("cellphone_A");
			String cellphone_B = obj.getString("cellphone_B");
			String id = obj.getString("id");

			SaleOrderNameListBean passenger = new SaleOrderNameListBean();
			passenger.setName(name);
			passenger.setChairman(chairman);
			if (!SimpletinyString.isEmpty(chairman) && chairman.equals("Y")) {
				passenger_captain = name;
			}
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setCellphone_A(cellphone_A);
			passenger.setCellphone_B(cellphone_B);
			passenger.setId(id);
			passenger.setOrder_pk(bean.getPk());
			if (bean.getConfirm_flg().equals("Y"))
				passenger.setTeam_number(bean.getTeam_number());
			nameListDao.insert(passenger);
		}

		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		return SUCCESS;
	}

	@Autowired
	private AirTicketNeedDAO airTicketNeedDao;

	@Autowired
	private AirNeedTeamNumberDAO airNeedTeamNumberDao;

	@Override
	public String updateOnlyTicketOrder(BudgetNonStandardOrderBean bean, String json) {
		String order_pk = bean.getPk();
		BudgetNonStandardOrderBean old = dao.selectByPrimaryKey(order_pk);
		// 判断是否有信用余额确认订单
		if (bean.getConfirm_flg().equals("Y")) {
			boolean canConfirm = userService.hasEnoughCreditToConfirm(old.getReceivable_first_flg(),
					old.getCreate_user(), old.getTeam_number(), bean.getReceivable());

			if (!canConfirm) {
				return "noenoughcredit";
			}

			if (SimpletinyString.isEmpty(bean.getTeam_number())) {
				bean.setTeam_number(numberService.generateTeamNumber());
			}
		}

		bean.setCreate_user(old.getCreate_user());
		if (!SimpletinyString.isEmpty(bean.getConfirm_file())) {
			if (!old.getConfirm_file().equals(bean.getConfirm_file())) {
				deleteFile(old);
				saveFile(bean);
			}
		} else {
			deleteFile(old);
		}

		// 修改名单
		// 删除之前的名单
		nameListDao.deleteByOrderPk(order_pk);

		JSONObject obj = JSONObject.fromObject(json);

		// 保存现在的名单信息
		String passenger_captain = "";
		JSONArray nameList = obj.getJSONArray("name_json");
		for (int i = 0; i < nameList.size(); i++) {
			JSONObject name_obj = JSONObject.fromObject(nameList.get(i));
			String chairman = name_obj.getString("chairman");
			int name_index = name_obj.getInt("index");
			String name = name_obj.getString("name");
			String sex = name_obj.getString("sex");
			String cellphone_A = name_obj.getString("cellphone_A");
			String cellphone_B = name_obj.getString("cellphone_B");
			String id = name_obj.getString("id");

			SaleOrderNameListBean passenger = new SaleOrderNameListBean();
			passenger.setName(name);
			passenger.setChairman(chairman);
			if (!SimpletinyString.isEmpty(chairman) && chairman.equals("Y")) {
				passenger_captain = name;
			}
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setCellphone_A(cellphone_A);
			passenger.setCellphone_B(cellphone_B);
			passenger.setId(id);
			passenger.setOrder_pk(bean.getPk());
			if (bean.getConfirm_flg().equals("Y")) {
				passenger.setTeam_number(bean.getTeam_number());
			}
			nameListDao.insert(passenger);
		}
		bean.setPassenger_captain(passenger_captain);

		// 删除之前的航班信息
		orderTicketInfoDao.deleteByOrderPk(order_pk);

		String first_from_to = "";
		String first_ticket_date = "";
		// 保存现在的航班信息
		JSONArray ticketList = obj.getJSONArray("ticket_json");
		String air_comment = obj.getString("air_comment");
		for (int i = 0; i < ticketList.size(); i++) {
			JSONObject ticket_obj = JSONObject.fromObject(ticketList.get(i));

			int index = ticket_obj.getInt("index");
			String date = ticket_obj.getString("date");
			String from_city = ticket_obj.getString("from_city");
			String to_city = ticket_obj.getString("to_city");

			SaleOrderTicketInfoBean soti = new SaleOrderTicketInfoBean();
			soti.setTicket_index(index);
			soti.setTicket_date(date);
			soti.setFrom_city(from_city);
			soti.setTo_city(to_city);
			soti.setOrder_pk(order_pk);
			if (index == 1) {
				soti.setComment(air_comment);
				first_from_to += from_city + "-" + to_city;
				// fly_day = start_day;
				first_ticket_date = date;
			}

			if (bean.getConfirm_flg().equals("Y")) {
				soti.setTeam_number(bean.getTeam_number());
			}

			orderTicketInfoDao.insert(soti);
		}

		if (bean.getConfirm_flg().equals("Y")) {
			// 判断是否有信用余额确认订单
			boolean canConfirm = userService.hasEnoughCreditToConfirm(old.getReceivable_first_flg(),
					old.getCreate_user(), old.getTeam_number(), bean.getReceivable());

			if (!canConfirm) {
				return "noenoughcredit";
			}

			String departureDate = bean.getDeparture_date();
			int days = bean.getDays();
			String returnDate = DateUtil.addDate(departureDate, days - 1);
			int people_count = bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count());
			if (SimpletinyString.isEmpty(bean.getConfirm_type()) || bean.getConfirm_type().equals("null")) {
				// 因为不设计产品，第一次确认名单状态标记为产品确认状态，所以产品操作状态直接标记为已操作
				bean.setName_confirm_status("3");
				bean.setOperate_flg("I");
				// 如果已经生成了应收款，则更新应收款
				if (old.getReceivable_first_flg().equals("Y")) {
					ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(bean.getTeam_number());

					receivable.setDeparture_date(bean.getDeparture_date());
					receivable.setReturn_date(returnDate);
					receivable.setProduct("单机票");
					receivable.setPeople_count(people_count);
					receivable.setBudget_receivable(bean.getReceivable());
					BigDecimal received = receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived();
					receivable.setBudget_balance(bean.getReceivable().subtract(received));
					receivable.setSales(old.getCreate_user());
					receivable.setCreate_user(old.getCreate_user());

					receivableDao.update(receivable);
				}
				// 生成应收款
				else {

					ReceivableBean receivable = new ReceivableBean();
					receivable.setTeam_number(bean.getTeam_number());
					receivable.setFinal_flg("N");
					receivable.setClient_employee_pk(bean.getClient_employee_pk());

					receivable.setDeparture_date(bean.getDeparture_date());
					receivable.setReturn_date(returnDate);
					receivable.setProduct("单机票");
					receivable.setPeople_count(people_count);
					receivable.setBudget_receivable(bean.getReceivable());

					receivable.setBudget_balance(bean.getReceivable());
					receivable.setReceived(BigDecimal.ZERO);
					receivable.setSales(old.getCreate_user());
					receivable.setCreate_user(old.getCreate_user());

					receivableDao.insert(receivable);
				}

				// 生成team_report基础数据
				TeamReportBean tr = new TeamReportBean();
				tr.setTeam_number(bean.getTeam_number());

				BaseDataBean option = baseDataDao.selectByPk(ResourcesConstants.BASE_DATA_PK_TEAM);
				BigDecimal sale_cost = new BigDecimal(0);
				BigDecimal sys_cost = new BigDecimal(option.getExt2()).multiply(new BigDecimal(people_count))
						.setScale(2, BigDecimal.ROUND_UP);

				tr.setSale_cost(sale_cost);
				tr.setSys_cost(sys_cost);

				orderReportDao.insert(tr);

				// 生成票务需求
				AirTicketNeedBean atn = new AirTicketNeedBean();

				String ticket_client_number = "";

				atn.setPassenger_captain(passenger_captain);
				atn.setComment(air_comment);
				atn.setProduct_name("单机票");
				atn.setDeparture_date(bean.getDeparture_date());
				atn.setAdult_cnt(bean.getAdult_count());
				atn.setSpecial_cnt((bean.getSpecial_count() == null ? 0 : bean.getSpecial_count()));
				atn.setFirst_from_to(first_from_to);
				// 因为单机票没有产品订单，直接写入团号信息
				atn.setProduct_order_number(bean.getTeam_number());

				UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
						.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
				ticket_client_number = sessionBean.getUser_number();

				atn.setTicket_client_number(ticket_client_number);
				atn.setFirst_ticket_date(first_ticket_date);

				String need_pk = airTicketNeedDao.insert(atn);
				// 生成团号和票务需求对应关系表
				AirNeedTeamNumberBean ant = new AirNeedTeamNumberBean();
				ant.setNeed_pk(need_pk);
				ant.setTeam_number(bean.getTeam_number());
				airNeedTeamNumberDao.insert(ant);

			} else if (bean.getConfirm_type().equals("edit")) {
				// 更新应收款
				ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(bean.getTeam_number());
				receivable.setDeparture_date(bean.getDeparture_date());
				receivable.setReturn_date(returnDate);
				receivable.setPeople_count(
						bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count()));
				receivable.setBudget_receivable(bean.getReceivable());

				receivable.setBudget_balance(bean.getReceivable()
						.subtract((receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived())));
				receivableDao.update(receivable);

				// 更新票务需求
				AirTicketNeedBean atn = airTicketNeedDao.selectByProductOrderNumber(bean.getTeam_number());

				atn.setPassenger_captain(passenger_captain);
				atn.setComment(air_comment);
				atn.setDeparture_date(bean.getDeparture_date());
				atn.setAdult_cnt(bean.getAdult_count());
				atn.setSpecial_cnt((bean.getSpecial_count() == null ? 0 : bean.getSpecial_count()));
				atn.setFirst_from_to(first_from_to);
				atn.setFirst_ticket_date(first_ticket_date);
				airTicketNeedDao.update(atn);
			}
		}

		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		return SUCCESS;
	}

	@Autowired
	private AirTicketNameListDAO airTicketNameListDao;

	@Override
	public String updateConfirmedNonStandardOrder(BudgetNonStandardOrderBean bean, String json) {
		BudgetNonStandardOrderBean old = dao.selectByPrimaryKey(bean.getPk());
		bean.setCreate_user(old.getCreate_user());
		if (!SimpletinyString.isEmpty(bean.getConfirm_file())) {
			if (!old.getConfirm_file().equals(bean.getConfirm_file())) {
				deleteFile(old);
				saveFile(bean);
			}
		} else {
			deleteFile(old);
		}

		// 更新名单
		// 删除之前的名单
		nameListDao.deleteByTeamNumber(bean.getTeam_number());
		String passenger_captain = "";
		// 保存现在的名单
		JSONArray nameList = JSONArray.fromObject(json);
		for (int i = 0; i < nameList.size(); i++) {
			JSONObject obj = JSONObject.fromObject(nameList.get(i));
			String chairman = obj.getString("chairman");
			int name_index = obj.getInt("index");
			String name = obj.getString("name");
			String sex = obj.getString("sex");
			String cellphone_A = obj.getString("cellphone_A");
			String cellphone_B = obj.getString("cellphone_B");
			String id = obj.getString("id");

			SaleOrderNameListBean passenger = new SaleOrderNameListBean();
			passenger.setName(name);
			passenger.setChairman(chairman);
			if (!SimpletinyString.isEmpty(chairman) && chairman.equals("Y")) {
				passenger_captain = name;
			}
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setCellphone_A(cellphone_A);
			passenger.setCellphone_B(cellphone_B);
			passenger.setId(id);
			passenger.setOrder_pk(bean.getPk());
			passenger.setTeam_number(bean.getTeam_number());
			nameListDao.insert(passenger);
		}

		String departureDate = bean.getDeparture_date();
		int days = bean.getDays();
		String returnDate = DateUtil.addDate(departureDate, days - 1);
		int people_count = bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count());
		// 更新应收款
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(bean.getTeam_number());
		receivable.setClient_employee_pk(bean.getClient_employee_pk());

		receivable.setDeparture_date(bean.getDeparture_date());
		receivable.setReturn_date(returnDate);
		receivable.setProduct(bean.getProduct_name());
		receivable.setPeople_count(people_count);
		receivable.setBudget_receivable(bean.getReceivable());

		receivable.setBudget_balance(bean.getReceivable()
				.subtract(receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived()));
		receivable.setSales(old.getCreate_user());

		receivableDao.update(receivable);
		bean.setLock_flg("Y");
		bean.setPassenger_captain(passenger_captain);

		dao.update(bean);

		// 如果已经生成待出票名单，修改待出票名单
		if (old.getName_confirm_status().equals("4")) {
			// 获取票务名单
			List<AirTicketNameListBean> atns = airTicketNameListDao.selectByTeamNumber(bean.getTeam_number());
			// 获取最新名单
			List<SaleOrderNameListBean> names = nameListDao.selectByOrderPk(bean.getPk());

			if (atns.size() != names.size()) {
				return "conflict";
			}

			for (int i = names.size() - 1; i >= 0; i--) {

				SaleOrderNameListBean name = names.get(i);
				for (int j = atns.size() - 1; j >= 0; j--) {
					AirTicketNameListBean atn = atns.get(j);
					if (name.getTeam_number().equals(atn.getTeam_number()) && name.getId().equals(atn.getId())
							&& name.getName().equals(atn.getName())) {
						names.remove(i);
						atns.remove(j);
						break;
					}
				}
			}

			for (int j = atns.size() - 1; j >= 0; j--) {
				AirTicketNameListBean atn = atns.get(j);
				SaleOrderNameListBean name = names.get(j);

				atn.setName(name.getName());
				atn.setId(name.getId());
				airTicketNameListDao.update(atn);
			}
		}

		// 更新team_report数据
		TeamReportBean tr = orderReportDao.selectTeamReportByTn(bean.getTeam_number());
		BaseDataBean option = baseDataDao.selectByPk(ResourcesConstants.BASE_DATA_PK_TEAM);
		BigDecimal sys_cost = new BigDecimal(option.getExt2()).multiply(new BigDecimal(people_count)).setScale(2,
				BigDecimal.ROUND_UP);
		tr.setSys_cost(sys_cost);
		orderReportDao.updateTeamReport(tr);

		return SUCCESS;
	}

	private void saveFile(BudgetNonStandardOrderBean bean) {
		String user_number = "";
		if (null == bean.getCreate_user()) {
			UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
					.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
			user_number = sessionBean.getUser_number();
		} else {
			user_number = bean.getCreate_user();
		}

		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		String fileFolder = PropertiesUtil.getProperty("clientConfirmFileFolder");
		File sourceFile = new File(tempFolder + File.separator + bean.getConfirm_file());
		File destfile = new File(fileFolder + File.separator + user_number + File.separator + bean.getConfirm_file());
		try {
			FileUtils.copyFile(sourceFile, destfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sourceFile.delete();
	}

	private void deleteFile(BudgetNonStandardOrderBean old) {
		String user_number = old.getCreate_user();
		String fileFolder = PropertiesUtil.getProperty("clientConfirmFileFolder");
		File oldFile = new File(fileFolder + File.separator + user_number + File.separator + old.getConfirm_file());
		oldFile.delete();
	}

	@Autowired
	private ReceivableService receivableService;

	@Override
	public String delete(String order_pk) {
		BudgetNonStandardOrderBean old = dao.selectByPrimaryKey(order_pk);
		String result = "";
		// 如果已经生成了应收款，则删除应收款
		if (old.getReceivable_first_flg().equals("Y")) {
			result = receivableService.deleteByTeamNumber(old.getTeam_number());
		}

		if (!result.equals(SUCCESS))
			return result;

		deleteFile(old);
		dao.delete(order_pk);
		// 删除名单
		nameListDao.deleteByOrderPk(order_pk);

		// 如果是单机票订单，删除机票信息
		if (old.getIndependent_flg().equals("A")) {
			orderTicketInfoDao.deleteByOrderPk(order_pk);
		}
		return SUCCESS;
	}

	@Override
	public BudgetNonStandardOrderBean selectByPrimaryKey(String id) {
		BudgetNonStandardOrderBean order = dao.selectByPrimaryKey(id);
		// AirTicketOrderBean ticketOrder =
		// airTicketOrderService.selectBySaleOrderPk(id);
		// if (null != ticketOrder && ticketOrder.getLock_flg().equals("1"))
		// order.setName_list_lock("1");
		return order;
	}

	@Override
	public List<BudgetNonStandardOrderBean> selectByParam(BudgetNonStandardOrderBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public String updateComment(BudgetNonStandardOrderBean bean) {
		dao.update(bean);
		return SUCCESS;
	}

	@Override
	public BudgetNonStandardOrderBean selectByTeamNumber(String team_number) {
		return dao.selectByTeamNumber(team_number);
	}

	@Override
	public String rollBackCOrder(String order_pk) {
		BudgetNonStandardOrderBean order = dao.selectByPrimaryKey(order_pk);

		// 首先查询是否产品已经在操作中
		if (!order.getOperate_flg().equals("N")) {
			return "product";
		}

		// 删除应收款
		receivableDao.deleteByTeamNumber(order.getTeam_number());

		order.setTeam_number("");
		order.setConfirm_flg("N");
		dao.update(order);

		return SUCCESS;
	}

}