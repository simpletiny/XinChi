package com.xinchi.backend.order.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.backend.order.dao.OrderReportDAO;
import com.xinchi.backend.order.dao.OrderTicketInfoDAO;
import com.xinchi.backend.order.service.OrderService;
import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.backend.product.dao.ProductOrderDAO;
import com.xinchi.backend.product.dao.ProductOrderTeamNumberDAO;
import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.backend.sys.dao.BaseDataDAO;
import com.xinchi.backend.ticket.dao.AirNeedTeamNumberDAO;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.dao.AirTicketNeedDAO;
import com.xinchi.backend.ticket.dao.AirTicketOrderDAO;
import com.xinchi.backend.user.service.UserService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirNeedTeamNumberBean;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketNeedBean;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.BaseDataBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.bean.ProductOrderTeamNumberBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.SaleOrderBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.bean.SaleOrderTicketInfoBean;
import com.xinchi.bean.SaleScoreDto;
import com.xinchi.bean.TeamReportBean;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.FileFolder;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.SimpletinyUser;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO dao;

	@Autowired
	private ReceivableDAO receivableDao;

	@Override
	public List<OrderDto> selectTbcByPage(Page<OrderDto> page) {
		return dao.selectTbcByPage(page);
	}

	@Override
	public List<OrderDto> selectCByPage(Page<OrderDto> page) {

		return dao.selectCByPage(page);
	}

	@Override
	public OrderDto selectByTeamNumber(String team_number) {
		return dao.selectByTeamNumber(team_number);
	}

	@Override
	public List<OrderDto> selectFByPage(Page<OrderDto> page) {
		return dao.selectFByPage(page);
	}

	@Override
	public List<OrderDto> selectTbcByParam(OrderDto option) {
		return dao.selectTbcByParam(option);
	}

	@Override
	public OrderDto searchOrderByPk(String order_pk) {

		return dao.searchOrderByPk(order_pk);
	}

	@Override
	public String finalOrder(OrderDto order) {
		SaleOrderBean final_order = dao.selectByPrimaryKey(order.getPk());
		final_order.setFinal_receivable(order.getReceivable());
		final_order.setFinal_type(order.getFinal_type());
		final_order.setFinal_status(ResourcesConstants.FINAL_ORDER_STATUS_ING);
		final_order.setConfirm_flg("F");
		final_order.setFinal_confirm_file(order.getFinal_confirm_file());
		final_order.setFinal_voucher_file(order.getFinal_voucher_file());

		switch (order.getFinal_type()) {
		case "1":
			break;
		case "2":
			final_order.setRaise_money(order.getRaise_money());
			final_order.setRaise_comment(order.getRaise_comment());
			final_order.setReduce_money(order.getReduce_money());
			final_order.setReduce_comment(order.getReduce_comment());
			break;
		case "3":
			final_order.setRaise_money(order.getRaise_money());
			final_order.setRaise_comment(order.getRaise_comment());
			final_order.setReduce_money(order.getReduce_money());
			final_order.setReduce_comment(order.getReduce_comment());
			final_order.setComplain_money(order.getComplain_money());
			final_order.setComplain_reason(order.getComplain_reason());
			final_order.setComplain_solution(order.getComplain_solution());
			break;
		default:
			break;
		}

		dao.update(final_order);
		// 更新应收款
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(final_order.getTeam_number());
		receivable.setFinal_flg("Y");
		receivable.setFinal_receivable(final_order.getFinal_receivable());
		receivable.setFinal_balance(receivable.getFinal_receivable().subtract(receivable.getReceived()));
		receivableDao.update(receivable);
		// 保存决算单文件
		if (!SimpletinyString.isEmpty(final_order.getFinal_confirm_file()))

		{
			FileUtil.saveFile(final_order.getFinal_confirm_file(), FileFolder.CLIENT_FINAL.value(), final_order.getTeam_number());
		}
		// 保存凭证文件
		if (!SimpletinyString.isEmpty(final_order.getFinal_voucher_file())) {
			FileUtil.saveFile(final_order.getFinal_voucher_file(), FileFolder.CLIENT_FINAL_VOUCHER.value(), final_order.getTeam_number());
		}
		return SUCCESS;
	}

	@Autowired
	private OrderReportDAO orderReportDao;

	@Override
	public String rollBackFinalOrder(String order_pk) {
		SaleOrderBean final_order = dao.selectByPrimaryKey(order_pk);
		// 判断单团核算单是否已经审核，如果审核则不允许打回。
		TeamReportBean tr = orderReportDao.selectTeamReportByTn(final_order.getTeam_number());
		if (null != tr && tr.getApproved().equals("Y")) {
			return "approved";
		}
		final_order.setConfirm_flg("Y");
		dao.update(final_order);
		// 更新应收款
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(final_order.getTeam_number());
		receivable.setFinal_flg("N");
		receivable.setFinal_receivable(BigDecimal.ZERO);
		receivable.setFinal_balance(BigDecimal.ZERO);
		receivableDao.update(receivable);
		// 删除相关文件
		String confirm_file = final_order.getFinal_confirm_file();
		String voucher_file = final_order.getFinal_voucher_file();
		String team_number = final_order.getTeam_number();

		FileUtil.deleteFile(confirm_file, FileFolder.CLIENT_FINAL.value(), team_number);
		FileUtil.deleteFile(voucher_file, FileFolder.CLIENT_FINAL_VOUCHER.value(), team_number);
		return SUCCESS;
	}

	@Autowired
	private ReceivedDAO receivedDao;

	@Override
	public String cancelOrder(OrderDto order) {
		OrderDto budget_order = dao.searchOrderByPk(order.getPk());
		SaleOrderBean final_order = new SaleOrderBean();
		final_order.setPk(order.getPk());
		final_order.setFinal_confirm_file(order.getFinal_confirm_file());
		final_order.setFinal_voucher_file(order.getFinal_voucher_file());
		final_order.setFinal_receivable_comment(order.getFinal_receivable_comment());
		final_order.setFinal_receivable(order.getFinal_receivable());
		final_order.setFinal_type("4");
		final_order.setFinal_status(ResourcesConstants.FINAL_ORDER_STATUS_ING);
		final_order.setCancel_flg("Y");
		final_order.setConfirm_flg("F");
		dao.update(final_order);

		// 保存决算单文件
		if (!SimpletinyString.isEmpty(order.getFinal_confirm_file())) {
			FileUtil.saveFile(order.getFinal_confirm_file(), FileFolder.CLIENT_FINAL.value(), budget_order.getTeam_number());
		}
		// 保存凭证文件
		if (!SimpletinyString.isEmpty(order.getFinal_voucher_file())) {
			FileUtil.saveFile(order.getFinal_voucher_file(), FileFolder.CLIENT_FINAL_VOUCHER.value(), budget_order.getTeam_number());
		}

		// 更新应收款
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(budget_order.getTeam_number());
		receivable.setFinal_flg("Y");
		receivable.setFinal_receivable(order.getFinal_receivable());

		// 如果存在单团核算数据，则删除
		// 不删除单团核算数据，会影响到应收款中的退返和决算打回
		// orderReportDao.deleteByTeamNumber(budget_order.getTeam_number());

		// 如果订单存在98清尾，则打回
		ClientReceivedDetailBean option = new ClientReceivedDetailBean();
		option.setTeam_number(budget_order.getTeam_number());

		List<ClientReceivedDetailBean> res = receivedDao.selectByParam(option);
		// 在订单所有的收入记录中查找98清尾
		for (ClientReceivedDetailBean re : res) {
			if (re.getType().equals(ResourcesConstants.RECEIVED_TYPE_TAIL98)) {
				// 应收款已收减去98清尾的金额
				receivable.setReceived(receivable.getReceived().subtract(re.getReceived()));
				// 删除98清尾
				receivedDao.deleteByPk(re.getPk());
			}
		}

		receivable.setFinal_balance(receivable.getFinal_receivable().subtract(receivable.getReceived()));
		receivableDao.update(receivable);
		return SUCCESS;
	}

	@Override
	public List<SaleScoreDto> searchSaleScoreByPage(SaleScoreDto score) {
		return dao.searchSaleScore(score);
	}

	@Override
	public List<SaleScoreDto> searchBackMoneyScoreByPage(Page<SaleScoreDto> page) {
		return dao.searchBackMoneyScoreByPage(page);
	}

	@Override
	public List<SaleScoreDto> searchSaleScoreByParam(SaleScoreDto ssd) {
		return dao.searchSaleScoreByParam(ssd);
	}

	@Override
	public List<OrderDto> selectByParam(OrderDto order) {
		return dao.selectByParam(order);
	}

	@Override
	public List<OrderDto> selectConfirmingOrders() {
		OrderDto orderOption = new OrderDto();
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			orderOption.setSale_number(sessionBean.getUser_number());
		}

		return dao.selectConfirmingOrders(orderOption);
	}

	@Override
	public String confirmNameList(String team_number) {
		OrderDto order = dao.selectByTeamNumber(team_number);
		SaleOrderBean sale_order = new SaleOrderBean();
		sale_order.setPk(order.getPk());

		int old = Integer.valueOf(order.getName_confirm_status());
		if (old < 5) {
			sale_order.setName_confirm_status(String.valueOf(old + 1));
			dao.update(sale_order);
		}
		return SUCCESS;
	}

	@Override
	public List<OrderDto> selectByTeamNumbers(List<String> team_numbers) {

		return dao.selectByTeamNumbers(team_numbers);
	}

	@Override
	public List<OrderDto> selectWithProductByParam(OrderDto order) {
		return dao.selectWithProductByParam(order);
	}

	@Override
	public String rollBackConfirmNameList(String team_number) {
		OrderDto order = dao.selectByTeamNumber(team_number);
		SaleOrderBean sale_order = new SaleOrderBean();
		sale_order.setPk(order.getPk());

		int old = Integer.valueOf(order.getName_confirm_status());
		sale_order.setName_confirm_status(String.valueOf(old - 1));

		dao.update(sale_order);
		return SUCCESS;
	}

	@Override
	public List<SaleScoreDto> search3MonthScoreByUserNumber(String user_number) {

		return dao.search3MonthScoreByUserNumber(user_number);
	}

	@Autowired
	private ReceivedService receivedService;

	@Override
	public List<OrderDto> searchOrderByReceivedRelatedPk(String related_pk) {
		List<ClientReceivedDetailBean> receiveds = receivedService.selectByRelatedPks(related_pk);

		if (null != receiveds && receiveds.size() > 0) {
			List<String> team_numbers = new ArrayList<String>();
			for (ClientReceivedDetailBean crd : receiveds) {
				team_numbers.add(crd.getTeam_number());
			}
			List<OrderDto> orders = dao.selectByTeamNumbers(team_numbers);
			return orders;
		}

		return null;
	}

	@Autowired
	private NumberService numberService;

	@Autowired
	private ProductDAO productDao;

	@Override
	public String createReceivable(String order_pk) {
		OrderDto order = dao.searchOrderByPk(order_pk);
		if (order.getReceivable() == null || order.getReceivable().compareTo(BigDecimal.ZERO) == 0) {
			return "nomoney";
		}

		if (order.getReceivable_first_flg().equals("Y")) {
			return "already";
		}

		// 生成团号和应付款
		String team_number = numberService.generateTeamNumber();
		// 生成应收款
		ReceivableBean receivable = new ReceivableBean();
		receivable.setTeam_number(team_number);
		receivable.setFinal_flg("N");
		receivable.setClient_employee_pk(order.getClient_employee_pk());
		String departureDate = order.getDeparture_date();
		Integer days = order.getDays();
		receivable.setDeparture_date(departureDate);
		if (!SimpletinyString.isEmpty(departureDate) && days != null && days != 0) {
			String returnDate = DateUtil.addDate(departureDate, days - 1);
			receivable.setReturn_date(returnDate);
		}

		if (order.getStandard_flg().equals("Y")) {
			ProductBean product = productDao.selectByPrimaryKey(order.getProduct_pk());
			receivable.setProduct(product.getName());
		} else {
			receivable.setProduct(order.getProduct_name());
		}

		if (order.getAdult_count() != null) {
			int people_count = order.getAdult_count() + (order.getSpecial_count() == null ? 0 : order.getSpecial_count());
			receivable.setPeople_count(people_count);
		}

		receivable.setBudget_receivable(order.getReceivable());

		receivable.setBudget_balance(order.getReceivable());
		receivable.setReceived(BigDecimal.ZERO);
		receivable.setSales(order.getSale_number());
		receivable.setCreate_user(order.getCreate_user_number());

		receivableDao.insert(receivable);

		SaleOrderBean ready_order = dao.selectByPrimaryKey(order_pk);
		ready_order.setTeam_number(team_number);
		ready_order.setReceivable_first_flg("Y");
		dao.update(ready_order);

		// 生成team_report基础数据
		TeamReportBean tr = new TeamReportBean();
		tr.setTeam_number(team_number);
		orderReportDao.insert(tr);

		return SUCCESS;
	}

	@Override
	public String checkCanBeEdit(String order_pk) {
		OrderDto order = dao.searchOrderByPk(order_pk);

		// 检测产品是否锁定
		if (order.getLock_flg().split(",")[0].equals("Y")) {
			return "订单已锁定，请联系产品经理解锁后操作。";
		}

		// // 标准订单
		// if (order.getStandard_flg().equals("Y")) {
		//
		// } else {
		// // 单机票订单
		// if (order.getIndependent_flg().equals("A")) {
		// // 检测产品是否锁定
		// }
		// // 非标订单
		// else {
		// }
		// }

		return SUCCESS;
	}

	@Override
	public OrderDto selectFinalOrderByTeamNumber(String team_number) {
		return dao.selectFinalOrderByTeamNumber(team_number);
	}

	@Override
	public String authorizeAssistant(List<String> order_pks, String assistant_number) {

		for (String order_pk : order_pks) {
			SaleOrderBean sale_order = new SaleOrderBean();
			sale_order.setPk(order_pk);
			sale_order.setAssistant_number(assistant_number);
			dao.update(sale_order);
		}
		return SUCCESS;
	}

	@Override
	public String forwardOrder(List<String> order_pks, String sale_number) {
		for (String order_pk : order_pks) {
			SaleOrderBean order = new SaleOrderBean();
			order.setPk(order_pk);
			order.setSale(sale_number);
			order.setAssistant_number(SimpletinyUser.user().getUser_number());
			dao.update(order);
		}

		return SUCCESS;
	}

	@Override
	public List<SaleScoreDto> searchNonStandardSaleData(SaleScoreDto score) {
		return dao.searchNonStandardSaleData(score);
	}

	@Override
	public List<SaleScoreDto> searchSaleCost(SaleScoreDto score) {
		return dao.searchSaleCost(score);
	}

	@Override
	public List<OrderDto> selectOrderWithNames(List<String> t_ns) {
		return dao.selectOrderWithNames(t_ns);
	}

	@Autowired
	private OrderNameListDAO nameListDao;

	@Override
	public String createBudgetStandardOrder(SaleOrderBean bean, String json) {

		// 保存确认文件
		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			saveFile(bean);
		}

		UserSessionBean user = SimpletinyUser.user();
		if (bean.getDeparture_date().equals(""))
			bean.setDeparture_date(null);
		String order_pk = DBCommonUtil.genPk();
		String passenger_captain = "";
		bean.setPk(order_pk);

		bean.setSale(user.getUser_number());
		bean.setAssistant_number(user.getUser_number());
		bean.setStandard_flg("Y");

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
			String id_type = obj.getString("id_type");
			String age_string = obj.getString("age").trim();
			String as_adult = obj.getString("as_adult");
			int age = age_string.isEmpty() ? 0 : Integer.valueOf(age_string);

			BigDecimal price = SimpletinyString.isEmpty(obj.getString("price")) ? BigDecimal.ZERO : new BigDecimal(obj.getString("price"));

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
			passenger.setOrder_pk(order_pk);
			passenger.setPrice(price);
			passenger.setId_type(id_type);
			passenger.setAge(age);
			passenger.setAs_adult(as_adult);
			nameListDao.insert(passenger);
		}
		bean.setPassenger_captain(passenger_captain);
		dao.insertWithPk(bean);
		return SUCCESS;
	}

	private void saveFile(SaleOrderBean bean) {
		String user_number = "";
		if (null == bean.getCreate_user()) {
			UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
			user_number = sessionBean.getUser_number();
		} else {
			user_number = bean.getCreate_user();
		}

		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		String fileFolder = PropertiesUtil.getProperty("clientConfirmFileFolder");
		File sourceFile = new File(tempFolder + File.separator + bean.getBudget_confirm_file());
		File destfile = new File(fileFolder + File.separator + user_number + File.separator + bean.getBudget_confirm_file());
		try {
			FileUtils.copyFile(sourceFile, destfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sourceFile.delete();
	}

	private void deleteFile(SaleOrderBean old) {
		String user_number = old.getCreate_user();
		String fileFolder = PropertiesUtil.getProperty("clientConfirmFileFolder");
		File oldFile = new File(fileFolder + File.separator + user_number + File.separator + old.getBudget_confirm_file());
		oldFile.delete();
	}

	@Override
	public String createBudgetNonStandardOrder(SaleOrderBean bean, String json) {
		// 保存确认文件
		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			saveFile(bean);
		}

		UserSessionBean user = SimpletinyUser.user();
		String order_pk = DBCommonUtil.genPk();
		String passenger_captain = "";
		bean.setPk(order_pk);
		bean.setStandard_flg("N");
		bean.setSale(user.getUser_number());
		bean.setAssistant_number(user.getUser_number());

		JSONArray nameList = JSONArray.fromObject(json);
		for (int i = 0; i < nameList.size(); i++) {
			JSONObject obj = JSONObject.fromObject(nameList.get(i));
			String chairman = obj.getString("chairman");
			int name_index = obj.getInt("index");
			String name = obj.getString("name");
			String sex = obj.getString("sex");
			String cellphone_A = obj.getString("cellphone_A");
			String cellphone_B = obj.getString("cellphone_B");
			String id_type = obj.getString("id_type");
			String age_string = obj.getString("age");
			int age = age_string.isEmpty() ? 0 : Integer.valueOf(age_string);
			String id = obj.getString("id");

			SaleOrderNameListBean passenger = new SaleOrderNameListBean();
			passenger.setName(name);
			passenger.setChairman(chairman);
			if (!SimpletinyString.isEmpty(chairman) && chairman.equals("Y")) {
				passenger_captain = name;
			}
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setAge(age);
			passenger.setId_type(id_type);
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
	public String createOnlyTicketOrder(SaleOrderBean bean, String json) {
		// 保存确认文件
		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			saveFile(bean);
		}

		String order_pk = DBCommonUtil.genPk();
		String passenger_captain = "";
		bean.setPk(order_pk);
		UserSessionBean user = SimpletinyUser.user();

		bean.setSale(user.getUser_number());
		bean.setAssistant_number(user.getUser_number());

		bean.setProduct_name("单机票");
		bean.setStandard_flg("N");
		// 产品经理变为指定方式 2023-11-9 09:22:32
		// bean.setProduct_manager(ResourcesConstants.UNREAL_USER_NUMBER_ONLY_TICKET);

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
			String id_type = name_obj.getString("id_type");
			String age_string = name_obj.getString("age");
			int age = age_string.isEmpty() ? 0 : Integer.valueOf(age_string);
			String id = name_obj.getString("id");

			SaleOrderNameListBean passenger = new SaleOrderNameListBean();
			passenger.setName(name);
			passenger.setChairman(chairman);
			if (!SimpletinyString.isEmpty(chairman) && chairman.equals("Y")) {
				passenger_captain = name;
			}
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setAge(age);
			passenger.setId_type(id_type);
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
	private ReceivableService receivableService;

	@Override
	public String deleteTbcOrder(String order_pk) {
		SaleOrderBean old = dao.selectByPrimaryKey(order_pk);

		// 如果已经生成了应收款，则删除应收款
		if (old.getReceivable_first_flg().equals("Y")) {
			String result = receivableService.deleteByTeamNumber(old.getTeam_number());
			if (!result.equals(SUCCESS))
				return result;

			orderReportDao.deleteByTeamNumber(old.getTeam_number());
		}

		deleteFile(old);
		dao.deleteByPk(order_pk);
		return SUCCESS;
	}

	public String deleteByPk(String order_pk) {
		dao.deleteByPk(order_pk);
		return SUCCESS;
	}

	@Override
	public String updateBudgetStandardOrder(SaleOrderBean bean, String json) {
		SaleOrderBean old = dao.selectByPrimaryKey(bean.getPk());
		// 如果已经生成了应收款，则更新应收款
		if (old.getReceivable_first_flg().equals("Y")) {
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(bean.getTeam_number());

			String departureDate = bean.getDeparture_date();
			Integer days = bean.getDays();
			receivable.setDeparture_date(departureDate);
			if (!SimpletinyString.isEmpty(departureDate) && days != null && days != 0) {
				String returnDate = DateUtil.addDate(departureDate, days - 1);
				receivable.setReturn_date(returnDate);
			}
			if (bean.getAdult_count() != null) {
				int people_count = bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count());
				receivable.setPeople_count(people_count);
			}

			receivable.setBudget_receivable(bean.getReceivable());
			BigDecimal received = receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived();
			receivable.setBudget_balance(bean.getReceivable().subtract(received));

			receivableDao.update(receivable);
		}

		// bean.setCreate_user(old.getCreate_user());
		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			if (!old.getBudget_confirm_file().equals(bean.getBudget_confirm_file())) {
				deleteFile(old);
				saveFile(bean);
			}
		} else {
			deleteFile(old);
		}
		String passenger_captain = "";
		// 修改名单
		// 删除之前的名单
		nameListDao.deleteByOrderPk(bean.getPk());
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
			String id_type = obj.getString("id_type");
			String as_adult = obj.getString("as_adult");
			String age_string = obj.getString("age");
			int age = age_string.isEmpty() ? 0 : Integer.valueOf(age_string);

			BigDecimal price = SimpletinyString.isEmpty(obj.getString("price")) ? BigDecimal.ZERO : new BigDecimal(obj.getString("price"));

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
			passenger.setPrice(price);
			passenger.setTeam_number(bean.getTeam_number());
			passenger.setAge(age);
			passenger.setId_type(id_type);
			passenger.setAs_adult(as_adult);
			nameListDao.insert(passenger);
		}

		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		return SUCCESS;
	}

	@Override
	public String updateOnlyTicketOrder(SaleOrderBean bean, String json) {
		String order_pk = bean.getPk();
		SaleOrderBean old = dao.selectByPrimaryKey(order_pk);

		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			if (!old.getBudget_confirm_file().equals(bean.getBudget_confirm_file())) {
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

			String id_type = name_obj.getString("id_type");
			String age_string = name_obj.getString("age");
			int age = age_string.isEmpty() ? 0 : Integer.valueOf(age_string);

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
			passenger.setTeam_number(old.getTeam_number());
			passenger.setAge(age);
			passenger.setId_type(id_type);

			nameListDao.insert(passenger);
		}

		// 删除之前的航班信息
		orderTicketInfoDao.deleteByOrderPk(order_pk);
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
			soti.setComment(air_comment);
			soti.setTeam_number(old.getTeam_number());
			orderTicketInfoDao.insert(soti);
		}

		// 如果已生成应收款，则更新应收款
		if (old.getReceivable_first_flg().equals("Y")) {
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(old.getTeam_number());

			String departureDate = bean.getDeparture_date();
			Integer days = bean.getDays();
			receivable.setDeparture_date(departureDate);
			if (!SimpletinyString.isEmpty(departureDate) && days != null && days != 0) {
				String returnDate = DateUtil.addDate(departureDate, days - 1);
				receivable.setReturn_date(returnDate);
			}
			if (bean.getAdult_count() != null) {
				int people_count = bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count());
				receivable.setPeople_count(people_count);
			}

			receivable.setBudget_receivable(bean.getReceivable() == null ? BigDecimal.ZERO : bean.getReceivable());
			BigDecimal received = receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived();
			receivable.setBudget_balance(receivable.getBudget_receivable().subtract(received));
			receivableDao.update(receivable);
		}

		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		return SUCCESS;
	}

	@Override
	public String updateBudgetNonStandardOrder(SaleOrderBean bean, String json) {
		SaleOrderBean old = dao.selectByPrimaryKey(bean.getPk());
		// 如果是已确认订单，不能在这里修改
		if (old.getConfirm_flg().equals("Y"))
			return FAIL;
		// 如果已经生成了应收款，则更新应收款
		if (old.getReceivable_first_flg().equals("Y")) {
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(old.getTeam_number());

			receivable.setProduct(bean.getProduct_name());
			String departureDate = bean.getDeparture_date();
			Integer days = bean.getDays();
			receivable.setDeparture_date(departureDate);
			if (!SimpletinyString.isEmpty(departureDate) && days != null && days != 0) {
				String returnDate = DateUtil.addDate(departureDate, days - 1);
				receivable.setReturn_date(returnDate);
			}
			if (bean.getAdult_count() != null) {
				int people_count = bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count());
				receivable.setPeople_count(people_count);
			}

			receivable.setBudget_receivable(bean.getReceivable());
			BigDecimal received = receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived();
			receivable.setBudget_balance(bean.getReceivable().subtract(received));

			receivableDao.update(receivable);
		}

		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			if (!old.getBudget_confirm_file().equals(bean.getBudget_confirm_file())) {
				deleteFile(old);
				saveFile(bean);
			}
		} else {
			deleteFile(old);
		}

		// 修改名单
		// 删除之前的名单
		nameListDao.deleteByOrderPk(old.getPk());

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
			String id_type = obj.getString("id_type");
			String age_string = obj.getString("age");
			int age = age_string.isEmpty() ? 0 : Integer.valueOf(age_string);

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
			passenger.setTeam_number(old.getTeam_number());
			passenger.setAge(age);
			passenger.setId_type(id_type);
			nameListDao.insert(passenger);
		}

		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		return SUCCESS;
	}

	@Autowired
	private UserService userService;
	@Autowired
	private BaseDataDAO baseDataDao;
	@Autowired
	private EmployeeService employeeService;

	@Override
	public String confirmStandardOrder(SaleOrderBean bean, String json) {
		SaleOrderBean old = dao.selectByPrimaryKey(bean.getPk());
		// 判断是否有信用余额确认订单
		if (!userService.hasEnoughCreditToConfirm(old.getReceivable_first_flg(), old.getSale(), old.getTeam_number(), bean.getReceivable()))
			return "noenoughcredit";

		ProductBean product = productDao.selectByPrimaryKey(bean.getProduct_pk());
		bean.setProduct_name(product.getName());
		bean.setProduct_model(product.getProduct_model());
		String departureDate = bean.getDeparture_date();
		int days = bean.getDays();
		String returnDate = DateUtil.addDate(departureDate, days - 1);
		int special_count = bean.getSpecial_count() == null ? 0 : bean.getSpecial_count();
		int people_count = bean.getAdult_count() + special_count;

		// 生成团号
		if (SimpletinyString.isEmpty(bean.getTeam_number())) {
			bean.setTeam_number(numberService.generateTeamNumber());
		}

		// 若果没有生成应收款，则生成应收款
		if (old.getReceivable_first_flg().equals("N")) {
			ReceivableBean receivable = new ReceivableBean();
			receivable.setTeam_number(bean.getTeam_number());
			receivable.setFinal_flg("N");
			receivable.setClient_employee_pk(bean.getClient_employee_pk());

			receivable.setDeparture_date(departureDate);
			receivable.setReturn_date(returnDate);
			receivable.setProduct(product.getName());
			receivable.setPeople_count(people_count);
			receivable.setBudget_receivable(bean.getReceivable());

			receivable.setBudget_balance(bean.getReceivable());
			receivable.setReceived(BigDecimal.ZERO);
			receivable.setSales(old.getSale());
			receivableDao.insert(receivable);
		}
		// 如果已经生成了应收款，则更新应收款
		else if (old.getReceivable_first_flg().equals("Y")) {
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(bean.getTeam_number());
			receivable.setDeparture_date(departureDate);
			receivable.setReturn_date(returnDate);
			receivable.setPeople_count(people_count);
			receivable.setBudget_receivable(bean.getReceivable());
			BigDecimal received = receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived();
			receivable.setBudget_balance(bean.getReceivable().subtract(received));
			receivableDao.update(receivable);
		}

		// 确认后锁定订单
		bean.setLock_flg("Y,N");
		String product_value = product.getProduct_value().floatValue() + ","
				+ (null == product.getProduct_child_value() ? "0" : product.getProduct_child_value().floatValue());
		bean.setProduct_value(product_value);
		// 保存产品线路和产品立奖信息
		bean.setProduct_line(product.getLocation());
		String product_bonus = product.getAdult_instant_bonus().toPlainString() + "," + product.getChild_instant_bonus().toPlainString();
		bean.setProduct_bonus(product_bonus);

		// 生成或者更新team_report基础数据
		TeamReportBean tr = orderReportDao.selectTeamReportByTn(bean.getTeam_number());
		boolean exists = null != tr;
		if (!exists) {
			tr = new TeamReportBean();
		}

		tr.setTeam_number(bean.getTeam_number());

		BaseDataBean option = baseDataDao.selectByPk(ResourcesConstants.BASE_DATA_PK_TEAM);

		// 修改系统费用计算逻辑
		BigDecimal sale_cost = new BigDecimal(option.getExt1()).multiply(product.getProduct_child_value().multiply(new BigDecimal(special_count))
				.add(product.getProduct_value().multiply(new BigDecimal(bean.getAdult_count())))).setScale(2, BigDecimal.ROUND_UP);

		BigDecimal sys_cost = new BigDecimal(option.getExt2()).multiply(new BigDecimal(people_count)).setScale(2, BigDecimal.ROUND_UP);

		tr.setSale_cost(sale_cost);
		tr.setSys_cost(sys_cost);
		if (!exists) {
			orderReportDao.insert(tr);
		} else {
			orderReportDao.updateTeamReport(tr);
		}
		// bean.setCreate_user(old.getCreate_user());
		// 保存新的确认单
		if (!old.getBudget_confirm_file().equals(bean.getBudget_confirm_file())) {
			deleteFile(old);
			saveFile(bean);
		}

		String passenger_captain = "";
		// 修改名单
		// 删除之前的名单
		nameListDao.deleteByOrderPk(bean.getPk());
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
			String as_adult = obj.getString("as_adult");
			BigDecimal price = SimpletinyString.isEmpty(obj.getString("price")) ? BigDecimal.ZERO : new BigDecimal(obj.getString("price"));
			String id_type = obj.getString("id_type");
			String age_string = obj.getString("age");
			int age = age_string.isEmpty() ? 0 : Integer.valueOf(age_string);

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
			passenger.setPrice(price);
			passenger.setTeam_number(bean.getTeam_number());
			passenger.setAge(age);
			passenger.setId_type(id_type);
			passenger.setAs_adult(as_adult);
			nameListDao.insert(passenger);
		}
		bean.setName_confirm_status(ResourcesConstants.NAME_CONFIRM_STATUS_YES);
		bean.setPassenger_captain(passenger_captain);
		bean.setConfirm_flg("Y");
		bean.setDo_confirm_date(DateUtil.today());
		dao.update(bean);
		employeeService.makePublicToSales(bean.getClient_employee_pk(), old.getSale());
		return SUCCESS;
	}

	@Autowired
	private AirTicketNeedDAO airTicketNeedDao;

	@Autowired
	private AirNeedTeamNumberDAO airNeedTeamNumberDao;

	@Override
	public String confirmOnlyTicketOrder(SaleOrderBean bean, String json) {
		String order_pk = bean.getPk();
		SaleOrderBean old = dao.selectByPrimaryKey(order_pk);
		// 判断是否有信用余额确认订单
		boolean canConfirm = userService.hasEnoughCreditToConfirm(old.getReceivable_first_flg(), old.getSale(), old.getTeam_number(),
				bean.getReceivable());

		if (!canConfirm) {
			return "noenoughcredit";
		}

		bean.setTeam_number(SimpletinyString.isEmpty(old.getTeam_number()) ? numberService.generateTeamNumber() : old.getTeam_number());
		bean.setCreate_user(old.getCreate_user());

		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			if (!old.getBudget_confirm_file().equals(bean.getBudget_confirm_file())) {
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

			String id_type = name_obj.getString("id_type");
			String age_string = name_obj.getString("age");
			int age = age_string.isEmpty() ? 0 : Integer.valueOf(age_string);

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
			passenger.setAge(age);
			passenger.setId_type(id_type);
			passenger.setOrder_pk(bean.getPk());
			passenger.setTeam_number(bean.getTeam_number());
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
			soti.setComment(air_comment);
			if (index == 1) {
				first_from_to += from_city + "-" + to_city;
				// fly_day = start_day;
				first_ticket_date = date;
			}
			soti.setTeam_number(bean.getTeam_number());
			orderTicketInfoDao.insert(soti);
		}

		String departureDate = bean.getDeparture_date();
		int days = bean.getDays();
		String returnDate = DateUtil.addDate(departureDate, days - 1);
		int people_count = bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count());

		// 因为不设计产品，第一次确认名单状态标记为产品确认状态，所以产品操作状态直接标记为已操作
		bean.setName_confirm_status(ResourcesConstants.NAME_CONFIRM_STATUS_YES);
		bean.setOperate_flg("I,N");
		// 如果没有生成应收款，则生成应收款
		if (old.getReceivable_first_flg().equals("N")) {
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
			receivable.setSales(old.getSale());
			receivableDao.insert(receivable);

			// 生成team_report基础数据
			TeamReportBean tr = new TeamReportBean();
			tr.setTeam_number(bean.getTeam_number());
			BigDecimal sale_cost = new BigDecimal(0);
			// 非标订单 1人30元，2人起一律50元一单。
			BigDecimal sys_cost = new BigDecimal(people_count > 1 ? 30 : 10);

			tr.setSale_cost(sale_cost);
			tr.setSys_cost(sys_cost);
			orderReportDao.insert(tr);
		}
		// 如果有应收款，更新应收款
		else {
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(bean.getTeam_number());
			receivable.setDeparture_date(departureDate);
			receivable.setReturn_date(returnDate);
			receivable.setPeople_count(people_count);

			receivable.setBudget_receivable(bean.getReceivable() == null ? BigDecimal.ZERO : bean.getReceivable());
			BigDecimal received = receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived();
			receivable.setBudget_balance(receivable.getBudget_receivable().subtract(received));

			receivableDao.update(receivable);

			// 更新team_report基础数据
			TeamReportBean tr = orderReportDao.selectTeamReportByTn(bean.getTeam_number());
			BigDecimal sale_cost = new BigDecimal(0);
			// 非标订单 1人30元，2人起一律50元一单。改为1人10元，30元
			BigDecimal sys_cost = new BigDecimal(people_count > 1 ? 30 : 10);
			tr.setSale_cost(sale_cost);
			tr.setSys_cost(sys_cost);
			orderReportDao.updateTeamReport(tr);
		}

		// 生成票务需求
		AirTicketNeedBean atn = new AirTicketNeedBean();

		atn.setPassenger_captain(passenger_captain);
		atn.setComment(air_comment);
		atn.setProduct_name("单机票");
		atn.setDeparture_date(bean.getDeparture_date());
		atn.setAdult_cnt(bean.getAdult_count());
		atn.setSpecial_cnt((bean.getSpecial_count() == null ? 0 : bean.getSpecial_count()));
		atn.setFirst_from_to(first_from_to);
		// 因为单机票没有产品订单，直接写入团号信息
		atn.setProduct_order_number(bean.getTeam_number());

		atn.setTicket_client_number(old.getSale());
		atn.setFirst_ticket_date(first_ticket_date);

		String need_pk = airTicketNeedDao.insert(atn);
		// 生成团号和票务需求对应关系表
		AirNeedTeamNumberBean ant = new AirNeedTeamNumberBean();
		ant.setNeed_pk(need_pk);
		ant.setTeam_number(bean.getTeam_number());
		airNeedTeamNumberDao.insert(ant);

		bean.setConfirm_flg("Y");
		bean.setDo_confirm_date(DateUtil.today());
		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		employeeService.makePublicToSales(bean.getClient_employee_pk(), old.getSale());
		return SUCCESS;
	}

	@Override
	public String confirmBudgetNonStandardOrder(SaleOrderBean bean, String json) {
		SaleOrderBean old = dao.selectByPrimaryKey(bean.getPk());
		// 如果是已确认订单，不能在这里修改
		if (old.getConfirm_flg().equals("Y"))
			return FAIL;

		String departureDate = bean.getDeparture_date();
		int days = bean.getDays();
		int people_count = bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count());
		String returnDate = DateUtil.addDate(departureDate, days - 1);

		// 判断是否有信用余额确认订单
		boolean canConfirm = userService.hasEnoughCreditToConfirm(old.getReceivable_first_flg(), old.getSale(), old.getTeam_number(),
				bean.getReceivable());

		if (!canConfirm) {
			return "noenoughcredit";
		}

		bean.setTeam_number(SimpletinyString.isEmpty(old.getTeam_number()) ? numberService.generateTeamNumber() : old.getTeam_number());

		bean.setLock_flg("Y,N");
		bean.setConfirm_flg("Y");
		bean.setDo_confirm_date(DateUtil.today());

		// 如果没有生成应收款，则生成应收款
		if (old.getReceivable_first_flg().equals("N")) {

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
			receivable.setSales(old.getSale());
			receivableDao.insert(receivable);
		}
		// 如果已经生成了应收款，则更新应收款
		else {
			ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(bean.getTeam_number());

			receivable.setProduct(bean.getProduct_name());
			receivable.setDeparture_date(departureDate);
			receivable.setReturn_date(returnDate);
			receivable.setPeople_count(people_count);

			receivable.setBudget_receivable(bean.getReceivable());
			BigDecimal received = receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived();
			receivable.setBudget_balance(bean.getReceivable().subtract(received));
			receivableDao.update(receivable);
		}

		// 生成或者更新team_report基础数据
		TeamReportBean tr = orderReportDao.selectTeamReportByTn(bean.getTeam_number());

		if (null == tr) {
			tr = new TeamReportBean();
			tr.setTeam_number(bean.getTeam_number());
			BigDecimal sale_cost = new BigDecimal(0);
			// 非标订单 1人30元，2人起一律50元一单。
			BigDecimal sys_cost = new BigDecimal(people_count > 1 ? 50 : 30);

			tr.setSale_cost(sale_cost);
			tr.setSys_cost(sys_cost);

			orderReportDao.insert(tr);
		} else {
			BigDecimal sale_cost = new BigDecimal(0);
			// 非标订单 1人30元，2人起一律50元一单。
			BigDecimal sys_cost = new BigDecimal(people_count > 1 ? 50 : 30);
			tr.setSale_cost(sale_cost);
			tr.setSys_cost(sys_cost);
			orderReportDao.updateTeamReport(tr);
		}

		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			if (!old.getBudget_confirm_file().equals(bean.getBudget_confirm_file())) {
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

			String id_type = obj.getString("id_type");
			String age_string = obj.getString("age");
			int age = age_string.isEmpty() ? 0 : Integer.valueOf(age_string);

			SaleOrderNameListBean passenger = new SaleOrderNameListBean();
			passenger.setName(name);
			passenger.setChairman(chairman);
			if (!SimpletinyString.isEmpty(chairman) && chairman.equals("Y")) {
				passenger_captain = name;
			}
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setAge(age);
			passenger.setId_type(id_type);
			passenger.setCellphone_A(cellphone_A);
			passenger.setCellphone_B(cellphone_B);
			passenger.setId(id);
			passenger.setOrder_pk(bean.getPk());
			passenger.setTeam_number(bean.getTeam_number());
			nameListDao.insert(passenger);
		}
		bean.setName_confirm_status(ResourcesConstants.NAME_CONFIRM_STATUS_YES);
		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		employeeService.makePublicToSales(bean.getClient_employee_pk(), old.getSale());
		return SUCCESS;
	}

	@Autowired
	private ProductOrderTeamNumberDAO productOrderTeamNumberDao;

	@Autowired
	private ProductOrderDAO productOrderDao;

	@Autowired
	private AirTicketOrderDAO airTicketOrderDao;

	@Autowired
	private AirTicketNameListDAO airTicketNameListDao;

	@Override
	public String updateConfirmedNonStandardOrder(SaleOrderBean bean, String json) {
		List<Object> result = checkModifyRights(bean, json);
		String msg = (String) result.get(0);
		if (!msg.equals(SUCCESS)) {
			return msg;
		}

		Set<SaleOrderNameListBean> addNameList = (Set<SaleOrderNameListBean>) result.get(1);
		Set<SaleOrderNameListBean> deleteNameList = (Set<SaleOrderNameListBean>) result.get(2);
		Set<SaleOrderNameListBean> modifyNameList = (Set<SaleOrderNameListBean>) result.get(3);
		Set<SaleOrderNameListBean> normalModifyNames = (Set<SaleOrderNameListBean>) result.get(4);
		String passenger_captain = (String) result.get(5);

		SaleOrderBean old = dao.selectByPrimaryKey(bean.getPk());
		String team_number = old.getTeam_number();
		bean.setCreate_user(old.getCreate_user());
		String operate_flg = old.getOperate_flg();

		String air_operate_flg = operate_flg.split(",")[1];

		String order_lock_flg = "Y,";
		String name_lock_flg = "N,";
		if (air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_NO)) {
			order_lock_flg += "N";
			name_lock_flg += "N";
		} else {
			order_lock_flg += "Y";
			name_lock_flg += "Y";
		}

		bean.setLock_flg(order_lock_flg);

		// 更新确认件
		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			if (!old.getBudget_confirm_file().equals(bean.getBudget_confirm_file())) {
				deleteFile(old);
				saveFile(bean);
			}
		} else {
			deleteFile(old);
		}

		// 更新应收款
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(team_number);
		String departureDate = bean.getDeparture_date();
		int days = bean.getDays();
		String returnDate = DateUtil.addDate(departureDate, days - 1);
		int special_count = bean.getSpecial_count() == null ? 0 : bean.getSpecial_count();
		int people_count = bean.getAdult_count() + special_count;
		receivable.setProduct(bean.getProduct_name());
		receivable.setDeparture_date(bean.getDeparture_date());
		receivable.setReturn_date(returnDate);
		receivable.setPeople_count(people_count);
		receivable.setBudget_receivable(bean.getReceivable());
		receivable.setBudget_balance(bean.getReceivable().subtract(receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived()));
		receivableDao.update(receivable);

		List<ProductOrderTeamNumberBean> relations = productOrderTeamNumberDao.selectByTeamNumber(team_number);

		if (null != relations && relations.size() > 0) {
			int add_adult_count = bean.getAdult_count() - old.getAdult_count();
			int add_special_count = special_count - (old.getSpecial_count() == null ? 0 : old.getSpecial_count());

			ProductOrderTeamNumberBean pot = relations.get(0);
			// 更新product_order
			ProductOrderBean po = productOrderDao.selectByOrderNumber(pot.getProduct_order_number());
			po.setAdult_count(po.getAdult_count() + add_adult_count);
			po.setSpecial_count(po.getSpecial_count() + add_special_count);
			productOrderDao.update(po);
		}

		// 更新team_report数据
		TeamReportBean tr = orderReportDao.selectTeamReportByTn(team_number);
		BigDecimal sys_cost = new BigDecimal(people_count > 1 ? 50 : 30);
		tr.setSys_cost(sys_cost);
		orderReportDao.updateTeamReport(tr);

		// 新增的名单
		for (SaleOrderNameListBean n : addNameList) {
			n.setLock_flg(name_lock_flg);
			nameListDao.insert(n);
		}

		// 删除的名单
		for (SaleOrderNameListBean n : deleteNameList) {
			nameListDao.delete(n.getPk());
			if (!air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_NO)) {
				AirTicketNameListBean deleteAirName = airTicketNameListDao.selectByBasePk(n.getPk());

				deleteAirName.setDelete_flg("Y");
				deleteAirName.setLock_flg(name_lock_flg.split(",")[1]);
				airTicketNameListDao.update(deleteAirName);
			}
		}

		// 修改的名单
		for (SaleOrderNameListBean n : modifyNameList) {
			n.setLock_flg(name_lock_flg);
			nameListDao.update(n);
			if (!air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_NO)) {
				AirTicketNameListBean modifyAirName = airTicketNameListDao.selectByBasePk(n.getPk());

				modifyAirName.setName(n.getName());
				modifyAirName.setId(n.getId());
				modifyAirName.setCellphone_A(n.getCellphone_A());
				modifyAirName.setCellphone_B(n.getCellphone_B());
				modifyAirName.setChairman(n.getChairman());
				modifyAirName.setLock_flg(name_lock_flg.split(",")[1]);
				modifyAirName.setAge(n.getAge());
				modifyAirName.setId_type(n.getId_type());
				airTicketNameListDao.update(modifyAirName);
			}
		}

		// 普通修改的名单
		for (SaleOrderNameListBean n : normalModifyNames) {
			nameListDao.update(n);
			if (!air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_NO)) {
				AirTicketNameListBean modifyAirName = airTicketNameListDao.selectByBasePk(n.getPk());
				if (null != modifyAirName) {
					modifyAirName.setCellphone_A(n.getCellphone_A());
					modifyAirName.setCellphone_B(n.getCellphone_B());
					modifyAirName.setAge(n.getAge());
					modifyAirName.setId_type(n.getId_type());
					airTicketNameListDao.update(modifyAirName);
				}
			}
		}

		// 更新订单信息
		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		return SUCCESS;
	}

	/**
	 * 检测是否有相应的修改权限
	 *
	 * @param bean
	 * @param json
	 * @return
	 */
	private List<Object> checkModifyRights(SaleOrderBean bean, String json) {
		List<Object> result = new ArrayList<Object>();
		SaleOrderBean old = dao.selectByPrimaryKey(bean.getPk());

		String product_lock_flg = old.getLock_flg().split(",")[0];
		String air_lock_flg = old.getLock_flg().split(",")[1];

		String product_operate_flg = old.getOperate_flg().split(",")[0];
		String air_operate_flg = old.getOperate_flg().split(",")[1];

		final String msg_a = "{0}已{1}，不能{2}！请联系{0}经理。";

		// 检测是否可以修改出团日期
		if (!old.getDeparture_date().equals(bean.getDeparture_date())) {
			if (!air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_NO)) {
				result.add(MessageFormat.format(msg_a, "票务", "操作", "修改出团日期"));
				return result;
			}

			if (product_lock_flg.equals("Y")) {
				result.add(MessageFormat.format(msg_a, "产品", "锁定", "修改出团日期"));
				return result;
			}

			if (!product_operate_flg.equals(ResourcesConstants.ORDER_OPERATE_STATUS_NO)) {
				result.add(MessageFormat.format(msg_a, "产品", "操作", "修改出团日期"));
				return result;
			}
		}

		// 检测是否可以修改名单
		List<SaleOrderNameListBean> oldNameList = nameListDao.selectByOrderPk(bean.getPk());
		List<SaleOrderNameListBean> newNameList = new ArrayList<SaleOrderNameListBean>();
		// 新增名单
		Set<SaleOrderNameListBean> addNames = new HashSet<SaleOrderNameListBean>();

		JSONArray nameList = old.getIndependent_flg().equals("A") ? JSONObject.fromObject(json).getJSONArray("name_json")
				: JSONArray.fromObject(json);

		String passenger_captain = "";
		for (int i = 0; i < nameList.size(); i++) {
			JSONObject obj = JSONObject.fromObject(nameList.get(i));
			String chairman = obj.getString("chairman");
			int name_index = obj.getInt("index");
			String name = obj.getString("name");
			String sex = obj.getString("sex");
			String cellphone_A = obj.getString("cellphone_A");
			String cellphone_B = obj.getString("cellphone_B");
			String id = obj.getString("id");
			String pk = obj.getString("pk");
			String id_type = obj.getString("id_type");
			String age_string = obj.getString("age");
			int age = age_string.isEmpty() ? 0 : Integer.valueOf(age_string);

			String lock_flg = obj.getString("lock_flg");

			SaleOrderNameListBean passenger = new SaleOrderNameListBean();
			passenger.setName(name);
			passenger.setChairman(chairman);
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setCellphone_A(cellphone_A);
			passenger.setCellphone_B(cellphone_B);
			passenger.setId(id);
			passenger.setOrder_pk(bean.getPk());
			passenger.setTeam_number(old.getTeam_number());
			passenger.setLock_flg(lock_flg);
			passenger.setAge(age);
			passenger.setId_type(id_type);

			if (!SimpletinyString.isEmpty(chairman) && chairman.equals("Y")) {
				passenger_captain = name;
			}

			// 如果pk为空则是新增名单
			if (SimpletinyString.isEmpty(pk)) {
				addNames.add(passenger);
			} else {
				passenger.setPk(pk);
			}
			newNameList.add(passenger);
		}

		// 如果有新增名单
		if (addNames.size() > 0) {
			if (air_lock_flg.equals("Y")) {
				result.add(MessageFormat.format(msg_a, "票务", "锁定", "添加游客"));
				return result;
			}
			if (product_lock_flg.equals("Y")) {
				result.add(MessageFormat.format(msg_a, "产品", "锁定", "添加游客"));
				return result;
			}

			if (!product_operate_flg.equals(ResourcesConstants.ORDER_OPERATE_STATUS_NO)) {
				result.add(MessageFormat.format(msg_a, "产品", "操作", "添加游客"));
				return result;
			}
		}

		// 修改的名单
		Set<SaleOrderNameListBean> modifyNames = new HashSet<SaleOrderNameListBean>();
		// 普通修改
		Set<SaleOrderNameListBean> normalModifyNames = new HashSet<SaleOrderNameListBean>();
		// 删除的名单
		Set<SaleOrderNameListBean> deleteNames = new HashSet<SaleOrderNameListBean>();
		for (SaleOrderNameListBean n : oldNameList) {
			boolean isDelete = true;
			for (SaleOrderNameListBean newn : newNameList) {
				if (n.getPk().equals(newn.getPk())) {
					if (!n.equals(newn)) {
						modifyNames.add(newn);
					} else {
						if (n.normalHashCode() != newn.normalHashCode())
							normalModifyNames.add(newn);
					}
					isDelete = false;
				}
			}
			if (isDelete) {
				deleteNames.add(n);
			}
		}

		// 如果modifyNames不为空，说明有修改名单。
		if (modifyNames.size() > 0) {
			// 只能修改已经解锁的名单
			for (SaleOrderNameListBean n : modifyNames) {
				if (n.getLock_flg().split(",")[1].equals("Y")) {
					result.add("只能修改票务解锁的游客！");
					return result;
				}
			}
			// 如果已出票，则只能删除名单
			if (air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_YES)) {
				result.add(MessageFormat.format(msg_a, "票务", "出票", "修改名单内容"));
				return result;
			}
			if (product_lock_flg.equals("Y")) {
				result.add(MessageFormat.format(msg_a, "产品", "锁定", "修改名单内容"));
				return result;
			}
			if (!product_operate_flg.equals(ResourcesConstants.ORDER_OPERATE_STATUS_NO)) {
				result.add(MessageFormat.format(msg_a, "产品", "操作", "修改名单内容"));
				return result;
			}
		}

		// 如果deleteNames不为空，说明有删除名单。
		if (deleteNames.size() > 0) {
			// 只能删除已经解锁的名单
			for (SaleOrderNameListBean n : deleteNames) {
				if (n.getLock_flg().split(",")[1].equals("Y")) {
					result.add("只能删除票务解锁的游客！");
					return result;
				}
			}
			if (product_lock_flg.equals("Y")) {
				result.add(MessageFormat.format(msg_a, "产品", "锁定", "删除名单"));
				return result;
			}
			if (!product_operate_flg.equals(ResourcesConstants.ORDER_OPERATE_STATUS_NO)) {
				result.add(MessageFormat.format(msg_a, "产品", "操作", "删除名单"));
				return result;
			}
		}
		result.add(SUCCESS);
		result.add(addNames);
		result.add(deleteNames);
		result.add(modifyNames);
		result.add(normalModifyNames);
		result.add(passenger_captain);
		return result;
	}

	@Override
	public String updateConfirmedStandardOrder(SaleOrderBean bean, String json) {

		List<Object> result = checkModifyRights(bean, json);
		String msg = (String) result.get(0);
		if (!msg.equals(SUCCESS)) {
			return msg;
		}

		Set<SaleOrderNameListBean> addNameList = (Set<SaleOrderNameListBean>) result.get(1);
		Set<SaleOrderNameListBean> deleteNameList = (Set<SaleOrderNameListBean>) result.get(2);
		Set<SaleOrderNameListBean> modifyNameList = (Set<SaleOrderNameListBean>) result.get(3);
		Set<SaleOrderNameListBean> normalModifyNames = (Set<SaleOrderNameListBean>) result.get(4);
		String passenger_captain = (String) result.get(5);

		SaleOrderBean old = dao.selectByPrimaryKey(bean.getPk());
		String team_number = old.getTeam_number();

		bean.setCreate_user(old.getCreate_user());
		String operate_flg = old.getOperate_flg();

		String air_operate_flg = operate_flg.split(",")[1];

		String order_lock_flg = "Y,";
		String name_lock_flg = "N,";
		if (air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_NO)) {
			order_lock_flg += "N";
			name_lock_flg += "N";
		} else {
			order_lock_flg += "Y";
			name_lock_flg += "Y";
		}

		bean.setLock_flg(order_lock_flg);
		// 更新确认件
		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			if (!old.getBudget_confirm_file().equals(bean.getBudget_confirm_file())) {
				deleteFile(old);
				saveFile(bean);
			}
		} else {
			deleteFile(old);
		}

		// 更新应收款
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(team_number);
		String departureDate = bean.getDeparture_date();
		int days = bean.getDays();
		String returnDate = DateUtil.addDate(departureDate, days - 1);
		int special_count = bean.getSpecial_count() == null ? 0 : bean.getSpecial_count();
		int people_count = bean.getAdult_count() + special_count;
		receivable.setDeparture_date(bean.getDeparture_date());
		receivable.setReturn_date(returnDate);
		receivable.setPeople_count(people_count);
		receivable.setBudget_receivable(bean.getReceivable());
		receivable.setBudget_balance(bean.getReceivable().subtract(receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived()));
		receivableDao.update(receivable);

		List<ProductOrderTeamNumberBean> relations = productOrderTeamNumberDao.selectByTeamNumber(team_number);

		if (null != relations && relations.size() > 0) {
			int add_adult_count = bean.getAdult_count() - old.getAdult_count();
			int add_special_count = special_count - (old.getSpecial_count() == null ? 0 : old.getSpecial_count());

			ProductOrderTeamNumberBean pot = relations.get(0);
			// 更新product_order
			ProductOrderBean po = productOrderDao.selectByOrderNumber(pot.getProduct_order_number());
			po.setAdult_count(po.getAdult_count() + add_adult_count);
			po.setSpecial_count(po.getSpecial_count() + add_special_count);

			productOrderDao.update(po);

			// 更新air_ticket_need 2024-07-15注释掉，现在的票务需求与销售订单无关
			// AirTicketNeedBean atn =
			// airTicketNeedDao.selectByProductOrderNumber(pot.getProduct_order_number());
			// if (null != atn) {
			//
			// atn.setAdult_cnt(po.getAdult_count());
			// atn.setSpecial_cnt(po.getSpecial_count());
			// airTicketNeedDao.update(atn);
			//
			// // 更新air_ticket_order
			// AirTicketOrderBean ato = airTicketOrderDao.selectByNeedPk(atn.getPk());
			// if (null != ato) {
			// ato.setPeople_count(po.getAdult_count() + po.getSpecial_count());
			// airTicketOrderDao.update(ato);
			// }
			// }
		}

		// 更新team_report数据
		TeamReportBean tr = orderReportDao.selectTeamReportByTn(team_number);
		BaseDataBean option = baseDataDao.selectByPk(ResourcesConstants.BASE_DATA_PK_TEAM);
		String[] product_value = old.getProduct_value().split(",");

		if (product_value.length == 2) {
			BigDecimal sale_cost = new BigDecimal(option.getExt1()).multiply(new BigDecimal(product_value[1]).multiply(new BigDecimal(special_count))
					.add(new BigDecimal(product_value[0]).multiply(new BigDecimal(bean.getAdult_count())))).setScale(2, BigDecimal.ROUND_UP);
			tr.setSale_cost(sale_cost);
		}

		BigDecimal sys_cost = new BigDecimal(option.getExt2()).multiply(new BigDecimal(people_count)).setScale(2, BigDecimal.ROUND_UP);
		tr.setSys_cost(sys_cost);
		orderReportDao.updateTeamReport(tr);

		// 新增的名单
		for (SaleOrderNameListBean n : addNameList) {
			n.setLock_flg(name_lock_flg);
			nameListDao.insert(n);
		}

		// 删除的名单
		for (SaleOrderNameListBean n : deleteNameList) {
			nameListDao.delete(n.getPk());
			if (!air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_NO)) {
				AirTicketNameListBean deleteAirName = airTicketNameListDao.selectByBasePk(n.getPk());

				deleteAirName.setDelete_flg("Y");
				deleteAirName.setLock_flg(name_lock_flg.split(",")[1]);
				airTicketNameListDao.update(deleteAirName);
			}
		}

		// 修改的名单
		for (SaleOrderNameListBean n : modifyNameList) {
			n.setLock_flg(name_lock_flg);
			nameListDao.update(n);
			if (!air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_NO)) {
				AirTicketNameListBean modifyAirName = airTicketNameListDao.selectByBasePk(n.getPk());

				modifyAirName.setName(n.getName());
				modifyAirName.setId(n.getId());
				modifyAirName.setCellphone_A(n.getCellphone_A());
				modifyAirName.setCellphone_B(n.getCellphone_B());
				modifyAirName.setChairman(n.getChairman());
				modifyAirName.setLock_flg(name_lock_flg.split(",")[1]);
				modifyAirName.setAge(n.getAge());
				modifyAirName.setId_type(n.getId_type());
				airTicketNameListDao.update(modifyAirName);
			}
		}

		// 普通修改的名单
		for (SaleOrderNameListBean n : normalModifyNames) {
			nameListDao.update(n);
			if (!air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_NO)) {
				AirTicketNameListBean modifyAirName = airTicketNameListDao.selectByBasePk(n.getPk());
				if (null != modifyAirName) {
					modifyAirName.setCellphone_A(n.getCellphone_A());
					modifyAirName.setCellphone_B(n.getCellphone_B());
					modifyAirName.setAge(n.getAge());
					modifyAirName.setId_type(n.getId_type());
					airTicketNameListDao.update(modifyAirName);
				}
			}
		}
		// 更新订单信息
		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		return SUCCESS;
	}

	@Override
	public String updateConfirmedOnlyTicketOrder(SaleOrderBean bean, String json) {
		List<Object> result = checkModifyRights(bean, json);
		String msg = (String) result.get(0);
		if (!msg.equals(SUCCESS)) {
			return msg;
		}

		Set<SaleOrderNameListBean> addNameList = (Set<SaleOrderNameListBean>) result.get(1);
		Set<SaleOrderNameListBean> deleteNameList = (Set<SaleOrderNameListBean>) result.get(2);
		Set<SaleOrderNameListBean> modifyNameList = (Set<SaleOrderNameListBean>) result.get(3);
		Set<SaleOrderNameListBean> normalModifyNames = (Set<SaleOrderNameListBean>) result.get(4);
		String passenger_captain = (String) result.get(5);

		String order_pk = bean.getPk();
		SaleOrderBean old = dao.selectByPrimaryKey(order_pk);
		String team_number = old.getTeam_number();
		String operate_flg = old.getOperate_flg();
		String air_operate_flg = operate_flg.split(",")[1];

		String order_lock_flg = "N,";
		String name_lock_flg = "N,";

		JSONObject obj = JSONObject.fromObject(json);

		// 判断是否能够修改机票信息
		List<SaleOrderTicketInfoBean> oldSotis = orderTicketInfoDao.selectByOrderPk(order_pk);
		Set<SaleOrderTicketInfoBean> newSotis = new HashSet<SaleOrderTicketInfoBean>();

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
			soti.setComment(air_comment);

			soti.setTeam_number(team_number);
			newSotis.add(soti);
		}

		if (air_operate_flg.equals(ResourcesConstants.AIR_OPERATE_STATUS_NO)) {
			String first_from_to = "";
			String first_ticket_date = "";

			// 删除之前的航班信息
			orderTicketInfoDao.deleteByOrderPk(order_pk);
			for (SaleOrderTicketInfoBean soti : newSotis) {
				if (soti.getTicket_index() == 1) {
					first_from_to += soti.getFrom_city() + "-" + soti.getTo_city();
					first_ticket_date = soti.getTicket_date();
				}
				orderTicketInfoDao.insert(soti);
			}

			// 更新票务需求
			AirTicketNeedBean atn = airTicketNeedDao.selectByProductOrderNumber(team_number);
			atn.setPassenger_captain(passenger_captain);
			atn.setComment(air_comment);
			atn.setDeparture_date(bean.getDeparture_date());
			atn.setAdult_cnt(bean.getAdult_count());
			atn.setSpecial_cnt((bean.getSpecial_count() == null ? 0 : bean.getSpecial_count()));
			atn.setFirst_from_to(first_from_to);
			atn.setFirst_ticket_date(first_ticket_date);
			airTicketNeedDao.update(atn);

			// 更新air_ticket_order
			AirTicketOrderBean ato = airTicketOrderDao.selectByNeedPk(atn.getPk());
			if (null != ato) {
				ato.setPeople_count(atn.getAdult_cnt() + atn.getSpecial_cnt());
				airTicketOrderDao.update(ato);
			}

			order_lock_flg += "N";
			name_lock_flg += "N";

			// 新增的名单
			for (SaleOrderNameListBean n : addNameList) {
				n.setLock_flg(name_lock_flg);
				nameListDao.insert(n);
			}

			// 删除的名单
			for (SaleOrderNameListBean n : deleteNameList) {
				nameListDao.delete(n.getPk());
			}

			// 修改的名单
			for (SaleOrderNameListBean n : modifyNameList) {
				n.setLock_flg(name_lock_flg);
				nameListDao.update(n);
			}
		} else {
			for (SaleOrderTicketInfoBean soti : oldSotis) {
				if (!newSotis.contains(soti)) {
					return "票务已操作，不允许修改航班信息！";
				}
			}
			order_lock_flg += "Y";
			name_lock_flg += "Y";

			// 新增的名单
			List<AirTicketNameListBean> atns = airTicketNameListDao.selectByTeamNumber(team_number);
			AirTicketNameListBean atnl = (atns == null || atns.size() == 0) ? new AirTicketNameListBean() : atns.get(0);
			for (SaleOrderNameListBean n : addNameList) {
				n.setLock_flg(name_lock_flg);
				String name_pk = nameListDao.insert(n);

				AirTicketNameListBean nn = new AirTicketNameListBean();
				nn.setTeam_number(team_number);
				nn.setClient_number(atnl.getClient_number());
				nn.setFirst_ticket_date(atnl.getFirst_ticket_date());
				nn.setFirst_start_city(atnl.getFirst_start_city());
				nn.setFirst_end_city(atnl.getFirst_end_city());
				nn.setTicket_order_pk(atnl.getTicket_order_pk());
				nn.setName(n.getName());
				nn.setId(n.getId());
				nn.setAge(n.getAge());
				nn.setId_type(n.getId_type());
				nn.setOrder_number(atnl.getOrder_number());
				nn.setCellphone_A(n.getCellphone_A());
				nn.setCellphone_B(n.getCellphone_B());
				nn.setChairman(n.getChairman());
				nn.setLock_flg(name_lock_flg.split(",")[1]);
				nn.setBase_pk(name_pk);
				airTicketNameListDao.insert(nn);
			}

			// 删除的名单
			for (SaleOrderNameListBean n : deleteNameList) {
				nameListDao.delete(n.getPk());

				AirTicketNameListBean deleteAirName = airTicketNameListDao.selectByBasePk(n.getPk());

				deleteAirName.setDelete_flg("Y");
				deleteAirName.setLock_flg(name_lock_flg.split(",")[1]);
				airTicketNameListDao.update(deleteAirName);

			}

			// 修改的名单
			for (SaleOrderNameListBean n : modifyNameList) {
				n.setLock_flg(name_lock_flg);
				nameListDao.update(n);
				AirTicketNameListBean modifyAirName = airTicketNameListDao.selectByBasePk(n.getPk());

				modifyAirName.setName(n.getName());
				modifyAirName.setId(n.getId());
				modifyAirName.setCellphone_A(n.getCellphone_A());
				modifyAirName.setCellphone_B(n.getCellphone_B());
				modifyAirName.setAge(n.getAge());
				modifyAirName.setId_type(n.getId_type());
				modifyAirName.setChairman(n.getChairman());
				modifyAirName.setLock_flg(name_lock_flg.split(",")[1]);
				airTicketNameListDao.update(modifyAirName);
			}

			int special_count = old.getSpecial_count() == null ? 0 : old.getSpecial_count();

			int add_adult_count = bean.getAdult_count() - old.getAdult_count();
			int add_special_count = special_count - (old.getSpecial_count() == null ? 0 : old.getSpecial_count());

			// 更新air_ticket_need 现在的人数
			AirTicketNeedBean atn = airTicketNeedDao.selectByProductOrderNumber(team_number);
			if (null != atn) {
				atn.setAdult_cnt(atn.getAdult_cnt() + add_adult_count);
				atn.setSpecial_cnt(atn.getSpecial_cnt() + add_special_count);
				airTicketNeedDao.update(atn);
				// 更新air_ticket_order
				AirTicketOrderBean ato = airTicketOrderDao.selectByNeedPk(atn.getPk());
				if (null != ato) {
					ato.setPeople_count(atn.getAdult_cnt() + atn.getSpecial_cnt());
					airTicketOrderDao.update(ato);
				}
			}
		}

		// 普通修改的名单
		for (SaleOrderNameListBean n : normalModifyNames) {
			n.setLock_flg(name_lock_flg);
			nameListDao.update(n);
			AirTicketNameListBean modifyAirName = airTicketNameListDao.selectByBasePk(n.getPk());
			if (null != modifyAirName) {
				modifyAirName.setCellphone_A(n.getCellphone_A());
				modifyAirName.setCellphone_B(n.getCellphone_B());
				modifyAirName.setAge(n.getAge());
				modifyAirName.setId_type(n.getId_type());
				airTicketNameListDao.update(modifyAirName);
			}
		}

		if (!SimpletinyString.isEmpty(bean.getBudget_confirm_file())) {
			if (!old.getBudget_confirm_file().equals(bean.getBudget_confirm_file())) {
				deleteFile(old);
				saveFile(bean);
			}
		} else {
			deleteFile(old);
		}
		// 更新应收款
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(team_number);

		String departureDate = bean.getDeparture_date();
		Integer days = bean.getDays();
		receivable.setDeparture_date(departureDate);
		String returnDate = DateUtil.addDate(departureDate, days - 1);
		receivable.setReturn_date(returnDate);
		int people_count = bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count());
		receivable.setPeople_count(people_count);

		receivable.setBudget_receivable(bean.getReceivable() == null ? BigDecimal.ZERO : bean.getReceivable());
		BigDecimal received = receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived();
		receivable.setBudget_balance(receivable.getBudget_receivable().subtract(received));
		receivableDao.update(receivable);

		bean.setLock_flg(order_lock_flg);
		bean.setCreate_user(old.getCreate_user());
		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		return SUCCESS;
	}

	@Override
	public String update(SaleOrderBean sale_order) {
		dao.update(sale_order);
		return SUCCESS;
	}
}