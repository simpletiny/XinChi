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
import com.xinchi.backend.order.service.BudgetNonStandardOrderService;
import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.sale.dao.SaleOrderDAO;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.ticket.dao.AirTicketOrderDAO;
import com.xinchi.backend.ticket.service.AirTicketOrderService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketOrderBean;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.SaleOrderNameListBean;
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
	public String insert(BudgetNonStandardOrderBean bean, String json) {
		// 保存确认文件
		if (!SimpletinyString.isEmpty(bean.getConfirm_file())) {
			saveFile(bean);
		}
		dao.insert(bean);
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
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setCellphone_A(cellphone_A);
			passenger.setCellphone_B(cellphone_B);
			passenger.setId(id);
			passenger.setOrder_pk(bean.getPk());

			nameListDao.insert(passenger);
		}
		return SUCCESS;
	}

	@Autowired
	private NumberService numberService;
	@Autowired
	private SaleOrderDAO budgetOrderDao;
	@Autowired
	private ReceivableDAO receivableDao;

	@Autowired
	private OrderNameListDAO nameListDao;

	@Autowired
	private AirTicketOrderDAO airTicketOrderDao;

	@Autowired
	private AirTicketNameListDAO airTicketNameListDao;

	@Override
	public String update(BudgetNonStandardOrderBean bean, String json) {
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

			SaleOrderNameListBean passenger = new SaleOrderNameListBean();
			passenger.setName(name);
			passenger.setChairman(chairman);
			passenger.setName_index(name_index);
			passenger.setSex(sex);
			passenger.setCellphone_A(cellphone_A);
			passenger.setCellphone_B(cellphone_B);
			passenger.setId(id);
			passenger.setOrder_pk(bean.getPk());

			nameListDao.insert(passenger);
		}

		if (bean.getConfirm_flg().equals("Y")) {
			if (SimpletinyString.isEmpty(bean.getTeam_number())) {
				bean.setTeam_number(numberService.generateTeamNumber());
			}

			// 更新名单的team_number
			List<SaleOrderNameListBean> names = nameListDao.selectByOrderPk(bean.getPk());
			for (SaleOrderNameListBean name : names) {
				name.setTeam_number(bean.getTeam_number());
				nameListDao.update(name);
			}

			// 生成预算单
			BudgetOrderBean budgetOrder = new BudgetOrderBean();
			budgetOrder.setProduct(bean.getProduct_name());
			budgetOrder.setTeam_number(bean.getTeam_number());
			budgetOrder.setDeparture_date(bean.getDeparture_date());
			String departureDate = bean.getDeparture_date();
			int days = bean.getDays();
			String returnDate = DateUtil.addDate(departureDate, days - 1);
			budgetOrder.setDays(days);
			budgetOrder.setReturn_date(returnDate);
			budgetOrder.setComment(bean.getComment());
			budgetOrder.setReceivable(bean.getReceivable());
			budgetOrder.setConfirm_date(bean.getConfirm_date());
			budgetOrder.setOther_payment((bean.getOther_cost() == null ? BigDecimal.ZERO : bean.getOther_cost())
					.add((bean.getFy() == null ? BigDecimal.ZERO : bean.getFy())));

			String other_cost_comment = "";
			if (bean.getFy() != null) {
				other_cost_comment += bean.getOther_cost_comment() + "fy:" + bean.getFy();
			}

			budgetOrder.setPayment_comment(other_cost_comment);
			budgetOrder.setPeople_count(
					bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count()));
			budgetOrder.setClient_employee_pk(bean.getClient_employee_pk());
			budgetOrderDao.insert(budgetOrder);

			// 生成应收款
			ReceivableBean receivable = new ReceivableBean();
			receivable.setTeam_number(bean.getTeam_number());
			receivable.setFinal_flg("N");
			receivable.setClient_employee_pk(bean.getClient_employee_pk());

			receivable.setDeparture_date(bean.getDeparture_date());
			receivable.setReturn_date(returnDate);
			receivable.setProduct(bean.getProduct_name());
			receivable.setPeople_count(budgetOrder.getPeople_count());
			receivable.setBudget_receivable(bean.getReceivable());

			receivable.setBudget_balance(bean.getReceivable());
			receivable.setReceived(BigDecimal.ZERO);
			receivable.setSales(old.getCreate_user());
			receivable.setCreate_user(old.getCreate_user());

			receivableDao.insert(receivable);

			// 更新票务信息团号
			AirTicketOrderBean airTicketOrder = airTicketOrderDao.selectBySaleOrderPk(bean.getPk());

			if (null != airTicketOrder) {
				airTicketOrder.setTeam_number(bean.getTeam_number());
				airTicketOrderDao.update(airTicketOrder);
				AirTicketNameListBean airTicketNameListOption = new AirTicketNameListBean();
				airTicketNameListOption.setTicket_order_pk(airTicketOrder.getPk());
				List<AirTicketNameListBean> airTicketNameList = airTicketNameListDao
						.selectByParam(airTicketNameListOption);

				if (null != airTicketNameList && airTicketNameList.size() > 0) {
					for (AirTicketNameListBean passenger : airTicketNameList) {
						passenger.setTeam_number(bean.getTeam_number());
						airTicketNameListDao.update(passenger);
					}
				}
			}
		}
		dao.update(bean);
		return SUCCESS;
	}

	@Override
	public String updateConfirmedNonStandardOrder(BudgetNonStandardOrderBean bean) {
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
		String[] names = bean.getName_list().split(";");
		if (names.length != 0) {
			for (String str : names) {
				String people[] = str.split(":");
				if (people.length != 2)
					continue;

				SaleOrderNameListBean name = new SaleOrderNameListBean();
				name.setName(people[0].trim());
				name.setId(people[1].trim());
				name.setTeam_number(bean.getTeam_number());
				nameListDao.insert(name);
			}
		}

		// 更新预算单
		BudgetOrderBean budgetOrder = budgetOrderDao.selectBudgetOrderByTeamNumber(bean.getTeam_number());
		budgetOrder.setProduct(bean.getProduct_name());
		budgetOrder.setDeparture_date(bean.getDeparture_date());
		String departureDate = bean.getDeparture_date();
		int days = bean.getDays();
		String returnDate = DateUtil.addDate(departureDate, days - 1);
		budgetOrder.setDays(days);
		budgetOrder.setReturn_date(returnDate);
		budgetOrder.setComment(bean.getComment());
		budgetOrder.setReceivable(bean.getReceivable());
		budgetOrder.setConfirm_date(bean.getConfirm_date());
		budgetOrder.setOther_payment((bean.getOther_cost() == null ? BigDecimal.ZERO : bean.getOther_cost())
				.add((bean.getFy() == null ? BigDecimal.ZERO : bean.getFy())));

		String other_cost_comment = "";
		if (bean.getFy() != null) {
			other_cost_comment += bean.getOther_cost_comment() + "fy:" + bean.getFy();
		}

		budgetOrder.setPayment_comment(other_cost_comment);
		budgetOrder.setPeople_count(
				bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count()));
		budgetOrder.setClient_employee_pk(bean.getClient_employee_pk());
		budgetOrderDao.updateBudgetOrder(budgetOrder);

		// 更新应收款
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(bean.getTeam_number());
		receivable.setClient_employee_pk(bean.getClient_employee_pk());

		receivable.setDeparture_date(bean.getDeparture_date());
		receivable.setReturn_date(returnDate);
		receivable.setProduct(bean.getProduct_name());
		receivable.setPeople_count(budgetOrder.getPeople_count());
		receivable.setBudget_receivable(bean.getReceivable());

		receivable.setBudget_balance(bean.getReceivable()
				.subtract(receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived()));
		receivable.setSales(old.getCreate_user());

		receivableDao.update(receivable);

		dao.update(bean);
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

	@Override
	public String delete(String id) {
		BudgetNonStandardOrderBean old = dao.selectByPrimaryKey(id);
		AirTicketOrderBean ticketOrder = airTicketOrderService.selectBySaleOrderPk(id);
		if (null != ticketOrder && ticketOrder.getLock_flg().equals("1"))
			return "air_ticket_lock";
		deleteFile(old);
		dao.delete(id);
		return SUCCESS;
	}

	@Autowired
	private AirTicketOrderService airTicketOrderService;

	@Override
	public BudgetNonStandardOrderBean selectByPrimaryKey(String id) {
		BudgetNonStandardOrderBean order = dao.selectByPrimaryKey(id);
		AirTicketOrderBean ticketOrder = airTicketOrderService.selectBySaleOrderPk(id);
		if (null != ticketOrder && ticketOrder.getLock_flg().equals("1"))
			order.setName_list_lock("1");
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
		// 删除之前的名单
		nameListDao.deleteByTeamNumber(order.getTeam_number());
		order.setTeam_number("");
		order.setConfirm_flg("N");
		dao.update(order);

		return SUCCESS;
	}

}