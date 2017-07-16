package com.xinchi.backend.accounting.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.ReimbursementDAO;
import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.accounting.service.AccountingService;
import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.payable.dao.PayableDAO;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.ReimbursementBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Service
@Transactional
public class AccountingServiceImpl implements AccountingService {

	@Autowired
	private PaidDAO paidDao;

	@Autowired
	private NumberService numberService;

	@Autowired
	private AccPaidService accPaidService;

	@Override
	public String updateRelatedPaid(String related_pk, String status) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String approve_user = sessionBean.getUser_number();

		SupplierPaidDetailBean options = new SupplierPaidDetailBean();
		options.setRelated_pk(related_pk);
		List<SupplierPaidDetailBean> paids = paidDao.selectByParam(options);

		for (SupplierPaidDetailBean paid : paids) {
			paid.setStatus(status);
			paid.setApprove_user(approve_user);
			paidDao.update(paid);
		}

		if (status.equals(ResourcesConstants.PAID_STATUS_YES)) {
			// 生成待支付数据
			SupplierPaidDetailBean detail = paidDao.selectByRelatedPk(related_pk);
			WaitingForPaidBean waiting = new WaitingForPaidBean();
			String pay_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER, ResourcesConstants.PAY_TYPE_DIJIE,
					DateUtil.getDateStr(DateUtil.YYYYMMDD));
			waiting.setPay_number(pay_number);

			waiting.setItem(ResourcesConstants.PAY_TYPE_DIJIE);
			waiting.setReceiver(detail.getSupplier_employee_name());
			waiting.setMoney(detail.getMoney());
			waiting.setLimit_time(detail.getLimit_time());
			waiting.setApply_user(detail.getCreate_user());
			waiting.setApproval_user(approve_user);
			waiting.setRelated_pk(detail.getRelated_pk());
			waiting.setStatus(ResourcesConstants.PAY_STATUS_ING);

			accPaidService.insert(waiting);
		}
		return "success";
	}

	@Override
	public String updatePaid(String pk, String status) {
		SupplierPaidDetailBean options = new SupplierPaidDetailBean();
		options.setPk(pk);
		List<SupplierPaidDetailBean> paids = paidDao.selectByParam(options);

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String approve_user = sessionBean.getUser_number();

		for (SupplierPaidDetailBean paid : paids) {
			paid.setStatus(status);
			paid.setApprove_user(approve_user);
			paidDao.update(paid);
		}
		return "success";
	}

	@Autowired
	private ReimbursementDAO reimDao;
	@Autowired
	private UserDAO userDao;

	@Override
	public String agreePayApply(String reimbursement_pk) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		ReimbursementBean bean = reimDao.selectByPk(reimbursement_pk);
		bean.setApproval_user(sessionBean.getUser_number());
		bean.setApproval_time(DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss"));
		bean.setStatus(ResourcesConstants.PAID_STATUS_YES);
		reimDao.update(bean);

		WaitingForPaidBean waiting = new WaitingForPaidBean();

		String pay_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER, bean.getItem(),
				DateUtil.getDateStr(DateUtil.YYYYMMDD));
		waiting.setPay_number(pay_number);

		waiting.setItem(bean.getItem());
		UserBaseBean user = userDao.getByUserNumber(bean.getApply_user());
		waiting.setReceiver(user.getUser_name());
		waiting.setRelated_pk(bean.getPk());
		waiting.setMoney(bean.getMoney());
		waiting.setApply_user(bean.getApply_user());
		waiting.setApproval_user(bean.getApproval_user());
		waiting.setStatus(ResourcesConstants.PAY_STATUS_ING);
		accPaidService.insert(waiting);

		return "success";
	}

	@Override
	public String rejectPayApply(String reimbursement_pk) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		ReimbursementBean bean = reimDao.selectByPk(reimbursement_pk);
		bean.setApproval_user(sessionBean.getUser_number());
		bean.setApproval_time(DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss"));
		bean.setStatus(ResourcesConstants.PAID_STATUS_NO);
		reimDao.update(bean);

		return "success";
	}

	@Autowired
	private PayableDAO payableDao;

	@Override
	public String rollBackRelatedPayApply(String related_pk) {
		SupplierPaidDetailBean options = new SupplierPaidDetailBean();
		options.setRelated_pk(related_pk);
		List<SupplierPaidDetailBean> paids = paidDao.selectByParam(options);

		for (SupplierPaidDetailBean paid : paids) {
			String supplier_employee_pk = paid.getSupplier_employee_pk();
			String team_number = paid.getTeam_number();
			PayableBean options2 = new PayableBean();
			options2.setTeam_number(team_number);
			options2.setSupplier_employee_pk(supplier_employee_pk);
			List<PayableBean> payables = payableDao.selectByParam(options2);
			if (null != payables && payables.size() > 0) {
				PayableBean payable = payables.get(0);

				payable.setPaid(payable.getPaid().subtract(paid.getMoney()));
				payable.setBudget_balance(payable.getBudget_balance().add(paid.getMoney()));
				if (payable.getFinal_flg().equals("Y")) {
					payable.setFinal_balance(payable.getFinal_balance().add(paid.getMoney()));
				}
				payableDao.update(payable);
			}
			paidDao.deleteByPk(paid.getPk());
		}

		return "success";
	}

	@Override
	public String rollBackPayApply(String pk) {
		reimDao.deleteByPk(pk);
		return "success";
	}
}
