package com.xinchi.backend.order.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderNameListDAO;
import com.xinchi.backend.order.service.BudgetStandardOrderService;
import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.ticket.dao.AirTicketNameListDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.SaleOrderNameListBean;
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
public class BudgetStandardOrderServiceImpl implements BudgetStandardOrderService {

	@Autowired
	private BudgetStandardOrderDAO dao;

	@Override
	public String createOrder(BudgetStandardOrderBean bean, String json) {
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
			BigDecimal price = SimpletinyString.isEmpty(obj.getString("price")) ? BigDecimal.ZERO
					: new BigDecimal(obj.getString("price"));

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

			nameListDao.insert(passenger);
		}
		bean.setPassenger_captain(passenger_captain);
		dao.insertWithPk(bean);
		return SUCCESS;
	}

	@Autowired
	private NumberService numberService;

	@Autowired
	private ProductDAO productDao;

	@Autowired
	private ReceivableDAO receivableDao;

	@Autowired
	private OrderNameListDAO nameListDao;

	@Autowired
	private AirTicketNameListDAO airTicketNameListDao;

	@Override
	public String update(BudgetStandardOrderBean bean, String json) {
		BudgetStandardOrderBean old = dao.selectByPrimaryKey(bean.getPk());
		bean.setCreate_user(old.getCreate_user());
		if (!SimpletinyString.isEmpty(bean.getConfirm_file())) {
			if (!old.getConfirm_file().equals(bean.getConfirm_file())) {
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
			BigDecimal price = SimpletinyString.isEmpty(obj.getString("price")) ? BigDecimal.ZERO
					: new BigDecimal(obj.getString("price"));

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

			nameListDao.insert(passenger);
		}

		if (bean.getConfirm_flg().equals("Y")) {
			if (SimpletinyString.isEmpty(bean.getTeam_number())) {
				bean.setTeam_number(numberService.generateTeamNumber());
			}
			// 确认后锁定订单
			bean.setLock_flg("Y");

			// 更新名单的team_number
			List<SaleOrderNameListBean> names = nameListDao.selectByOrderPk(bean.getPk());
			for (SaleOrderNameListBean name : names) {
				name.setTeam_number(bean.getTeam_number());
				nameListDao.update(name);
			}

			ProductBean product = productDao.selectByPrimaryKey(bean.getProduct_pk());
			String departureDate = bean.getDeparture_date();
			int days = bean.getDays();
			String returnDate = DateUtil.addDate(departureDate, days - 1);
			// 生成应收款
			ReceivableBean receivable = new ReceivableBean();
			receivable.setTeam_number(bean.getTeam_number());
			receivable.setFinal_flg("N");
			receivable.setClient_employee_pk(bean.getClient_employee_pk());

			receivable.setDeparture_date(bean.getDeparture_date());
			receivable.setReturn_date(returnDate);
			receivable.setProduct(product.getName());
			receivable.setPeople_count(
					bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count()));
			receivable.setBudget_receivable(bean.getReceivable());

			receivable.setBudget_balance(bean.getReceivable());
			receivable.setReceived(BigDecimal.ZERO);
			receivable.setSales(old.getCreate_user());
			receivable.setCreate_user(old.getCreate_user());

			receivableDao.insert(receivable);

		}
		bean.setPassenger_captain(passenger_captain);
		dao.update(bean);
		return SUCCESS;
	}

	@Override
	public String updateConfirmedStandardOrder(BudgetStandardOrderBean bean, String json) {
		BudgetStandardOrderBean old = dao.selectByPrimaryKey(bean.getPk());
		bean.setCreate_user(old.getCreate_user());
		bean.setLock_flg("Y");
		if (!SimpletinyString.isEmpty(bean.getConfirm_file())) {
			if (!old.getConfirm_file().equals(bean.getConfirm_file())) {
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
			BigDecimal price = SimpletinyString.isEmpty(obj.getString("price")) ? BigDecimal.ZERO
					: new BigDecimal(obj.getString("price"));

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

			nameListDao.insert(passenger);
		}

		// 更新应收款
		ReceivableBean receivable = receivableDao.selectReceivableByTeamNumber(bean.getTeam_number());
		receivable.setClient_employee_pk(bean.getClient_employee_pk());
		ProductBean product = productDao.selectByPrimaryKey(bean.getProduct_pk());
		String departureDate = bean.getDeparture_date();
		int days = bean.getDays();
		String returnDate = DateUtil.addDate(departureDate, days - 1);

		receivable.setDeparture_date(bean.getDeparture_date());
		receivable.setReturn_date(returnDate);
		receivable.setProduct(product.getName());
		receivable.setPeople_count(
				bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count()));
		receivable.setBudget_receivable(bean.getReceivable());
		receivable.setBudget_balance(bean.getReceivable()
				.subtract(receivable.getReceived() == null ? BigDecimal.ZERO : receivable.getReceived()));
		receivableDao.update(receivable);

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

		return SUCCESS;
	}

	private void saveFile(BudgetStandardOrderBean bean) {
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

	private void deleteFile(BudgetStandardOrderBean old) {
		String user_number = old.getCreate_user();
		String fileFolder = PropertiesUtil.getProperty("clientConfirmFileFolder");
		File oldFile = new File(fileFolder + File.separator + user_number + File.separator + old.getConfirm_file());
		oldFile.delete();
	}

	@Override
	public String delete(String id) {
		BudgetStandardOrderBean old = dao.selectByPrimaryKey(id);
		// AirTicketOrderBean ticketOrder =
		// airTicketOrderService.selectBySaleOrderPk(id);
		// if (null != ticketOrder && ticketOrder.getLock_flg().equals("1"))
		// return "air_ticket_lock";
		deleteFile(old);
		dao.delete(id);
		return SUCCESS;
	}

	@Override
	public BudgetStandardOrderBean selectByPrimaryKey(String id) {
		BudgetStandardOrderBean order = dao.selectByPrimaryKey(id);
		// AirTicketOrderBean ticketOrder =
		// airTicketOrderService.selectBySaleOrderPk(id);
		// if (null != ticketOrder && ticketOrder.getLock_flg().equals("1"))
		// order.setName_list_lock("1");

		return order;
	}

	@Override
	public List<BudgetStandardOrderBean> selectByParam(BudgetStandardOrderBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public String updateComment(BudgetStandardOrderBean bean) {
		dao.update(bean);
		return SUCCESS;
	}

	@Override
	public BudgetStandardOrderBean selectByTeamNumber(String team_number) {
		return dao.selectByTeamNumber(team_number);
	}

	@Override
	public String rollBackCOrder(String order_pk) {
		BudgetStandardOrderBean order = dao.selectByPrimaryKey(order_pk);
		// 不删除之前的名单
		// nameListDao.deleteByTeamNumber(order.getTeam_number());

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