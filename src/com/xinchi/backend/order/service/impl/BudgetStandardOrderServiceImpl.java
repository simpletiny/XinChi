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
import com.xinchi.backend.sale.dao.SaleOrderDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.SaleOrderNameListBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.PropertiesUtil;

@Service
@Transactional
public class BudgetStandardOrderServiceImpl implements BudgetStandardOrderService {

	@Autowired
	private BudgetStandardOrderDAO dao;

	@Autowired
	private SaleOrderDAO budgetOrderDao;

	@Override
	public String insert(BudgetStandardOrderBean bean) {
		// 保存确认文件
		if (!SimpletinyString.isEmpty(bean.getConfirm_file())) {
			saveFile(bean);
		}

		dao.insert(bean);
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

	@Override
	public String update(BudgetStandardOrderBean bean) {
		BudgetStandardOrderBean old = dao.selectByPrimaryKey(bean.getPk());
		if (!SimpletinyString.isEmpty(bean.getConfirm_file())) {
			if (!old.getConfirm_file().equals(bean.getConfirm_file())) {
				deleteFile(old);
				saveFile(bean);
			}
		} else {
			deleteFile(old);
		}

		if (bean.getConfirm_flg().equals("Y")) {
			bean.setTeam_number(numberService.generateTeamNumber());

			// 保存名单
			String[] names = bean.getName_list().split(";");
			if (names.length != 0) {
				for (String str : names) {
					String people[] = str.split(":");
					if (people.length != 2)
						continue;

					SaleOrderNameListBean name = new SaleOrderNameListBean();
					name.setName(people[0]);
					name.setId(people[1]);
					name.setTeam_number(bean.getTeam_number());
					nameListDao.insert(name);
				}
			}

			// 生成预算单
			BudgetOrderBean budgetOrder = new BudgetOrderBean();
			ProductBean product = productDao.selectByPrimaryKey(bean.getProduct_pk());
			budgetOrder.setProduct(product.getName());
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
			budgetOrder
					.setOther_payment((bean.getOther_cost() == null ? BigDecimal.ZERO : bean.getOther_cost()).add((bean.getFy() == null ? BigDecimal.ZERO
							: bean.getFy())));

			String other_cost_comment = "";
			if (bean.getFy() != null) {
				other_cost_comment += bean.getOther_cost_comment() + "fy:" + bean.getFy();
			}

			budgetOrder.setPayment_comment(other_cost_comment);
			budgetOrder.setPeople_count(bean.getAdult_count() + (bean.getSpecial_count() == null ? 0 : bean.getSpecial_count()));
			budgetOrder.setClient_employee_pk(bean.getClient_employee_pk());
			budgetOrderDao.insert(budgetOrder);

			// 生成应收款
			ReceivableBean receivable = new ReceivableBean();
			receivable.setTeam_number(bean.getTeam_number());
			receivable.setFinal_flg("N");
			receivable.setClient_employee_pk(bean.getClient_employee_pk());

			receivable.setDeparture_date(bean.getDeparture_date());
			receivable.setReturn_date(returnDate);
			receivable.setProduct(product.getName());
			receivable.setPeople_count(budgetOrder.getPeople_count());
			receivable.setBudget_receivable(bean.getReceivable());

			receivable.setBudget_balance(bean.getReceivable());
			receivable.setReceived(BigDecimal.ZERO);
			receivable.setSales(old.getCreate_user());
			receivable.setCreate_user(old.getCreate_user());

			receivableDao.insert(receivable);
		}
		dao.update(bean);
		return SUCCESS;
	}

	private void saveFile(BudgetStandardOrderBean bean) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String user_number = sessionBean.getUser_number();
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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String user_number = sessionBean.getUser_number();
		String fileFolder = PropertiesUtil.getProperty("clientConfirmFileFolder");
		File oldFile = new File(fileFolder + File.separator + user_number + File.separator + old.getConfirm_file());
		oldFile.delete();
	}

	@Override
	public String delete(String id) {
		BudgetStandardOrderBean old = dao.selectByPrimaryKey(id);
		deleteFile(old);
		dao.delete(id);
		return SUCCESS;
	}

	@Override
	public BudgetStandardOrderBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
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

}