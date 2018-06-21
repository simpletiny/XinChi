package com.xinchi.backend.accounting.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.ReimbursementDAO;
import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.accounting.service.AccountingService;
import com.xinchi.backend.client.dao.ClientDAO;
import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.backend.payable.dao.AirTicketPaidDetailDAO;
import com.xinchi.backend.payable.dao.AirTicketPayableDAO;
import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.payable.dao.PayableDAO;
import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.bean.ClientBean;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientReceivedDetailBean;
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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String approve_user = sessionBean.getUser_number();

		SupplierPaidDetailBean options = new SupplierPaidDetailBean();
		options.setRelated_pk(related_pk);
		List<SupplierPaidDetailBean> paids = paidDao.selectByParam(options);

		for (SupplierPaidDetailBean paid : paids) {
			paid.setStatus(status);
			paid.setApprove_user(approve_user);
			paid.setConfirm_time(DateUtil.getTimeMillis());
			paidDao.update(paid);
		}

		if (status.equals(ResourcesConstants.PAID_STATUS_YES)) {
			// 生成待支付数据
			SupplierPaidDetailBean detail = paidDao.selectByRelatedPk(related_pk);
			WaitingForPaidBean waiting = new WaitingForPaidBean();
			String pay_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
					ResourcesConstants.PAY_TYPE_DIJIE, DateUtil.getDateStr(DateUtil.YYYYMMDD));
			waiting.setPay_number(pay_number);

			waiting.setItem(ResourcesConstants.PAY_TYPE_DIJIE);
			waiting.setReceiver(detail.getSupplier_employee_name());
			waiting.setMoney(detail.getAllot_money());
			waiting.setLimit_time(detail.getLimit_time());
			waiting.setApply_user(detail.getCreate_user());
			waiting.setApproval_user(approve_user);
			waiting.setRelated_pk(detail.getRelated_pk());
			waiting.setStatus(ResourcesConstants.PAY_STATUS_ING);

			accPaidService.insert(waiting);
		}
		return SUCCESS;
	}

	@Override
	public String agreeAirTicketPayApply(String related_pk) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String approve_user = sessionBean.getUser_number();

		List<AirTicketPaidDetailBean> paids = airTicketPaidDetailDao.selectByRelatedPk(related_pk);

		for (AirTicketPaidDetailBean paid : paids) {
			paid.setConfirm_time(DateUtil.getTimeMillis());
			paid.setStatus(ResourcesConstants.PAID_STATUS_YES);
			paid.setApprove_user(approve_user);
			airTicketPaidDetailDao.update(paid);
		}

		// 生成待支付数据
		AirTicketPaidDetailBean detail = airTicketPaidDetailDao.selectGroupDetailByRelatedPk(related_pk);

		WaitingForPaidBean waiting = new WaitingForPaidBean();
		String pay_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
				ResourcesConstants.PAY_TYPE_PIAOWU, DateUtil.getDateStr(DateUtil.YYYYMMDD));
		waiting.setPay_number(pay_number);

		waiting.setItem(ResourcesConstants.PAY_TYPE_PIAOWU);
		waiting.setReceiver(detail.getFinancial_body_name());
		waiting.setMoney(detail.getAllot_money());
		waiting.setLimit_time(detail.getLimit_time());
		waiting.setApply_user(detail.getCreate_user());
		waiting.setApproval_user(approve_user);
		waiting.setRelated_pk(detail.getRelated_pk());
		waiting.setStatus(ResourcesConstants.PAY_STATUS_ING);

		accPaidService.insert(waiting);

		return SUCCESS;
	}

	@Override
	public String rejectAirTicketPayApply(String related_pk) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String approve_user = sessionBean.getUser_number();

		List<AirTicketPaidDetailBean> paids = airTicketPaidDetailDao.selectByRelatedPk(related_pk);

		for (AirTicketPaidDetailBean paid : paids) {
			paid.setStatus(ResourcesConstants.PAID_STATUS_NO);
			paid.setApprove_user(approve_user);
			paid.setConfirm_time(DateUtil.getTimeMillis());
			airTicketPaidDetailDao.update(paid);
		}

		return SUCCESS;
	}

	@Override
	public String updatePaid(String pk, String status) {
		SupplierPaidDetailBean options = new SupplierPaidDetailBean();
		options.setPk(pk);
		List<SupplierPaidDetailBean> paids = paidDao.selectByParam(options);

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String approve_user = sessionBean.getUser_number();

		for (SupplierPaidDetailBean paid : paids) {
			paid.setStatus(status);
			paid.setApprove_user(approve_user);
			paidDao.update(paid);
		}
		return SUCCESS;
	}

	@Autowired
	private ReimbursementDAO reimDao;
	@Autowired
	private UserDAO userDao;

	@Override
	public String agreePayApply(String reimbursement_pk) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		ReimbursementBean bean = reimDao.selectByPk(reimbursement_pk);
		bean.setApproval_user(sessionBean.getUser_number());
		bean.setApproval_time(DateUtil.getTimeMillis());
		bean.setStatus(ResourcesConstants.PAID_STATUS_YES);
		reimDao.update(bean);

		WaitingForPaidBean waiting = new WaitingForPaidBean();

		String pay_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
				bean.getItem(), DateUtil.getDateStr(DateUtil.YYYYMMDD));
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

		return SUCCESS;
	}

	@Autowired
	private ReceivedDAO receivedDao;

	@Autowired
	private EmployeeDAO clientEmployeeDao;
	@Autowired
	private ClientDAO clientDao;

	@Override
	public String agreeMoreBack(String back_pk) {
		List<ClientReceivedDetailBean> details = receivedDao.selectByRelatedPks(back_pk);
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String user_number = sessionBean.getUser_number();

		for (ClientReceivedDetailBean bean : details) {
			bean.setConfirm_time(DateUtil.getTimeMillis());
			bean.setStatus(ResourcesConstants.PAID_STATUS_YES);

			receivedDao.update(bean);
			WaitingForPaidBean waiting = new WaitingForPaidBean();

			String pay_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER, "M",
					DateUtil.getDateStr(DateUtil.YYYYMMDD));
			waiting.setPay_number(pay_number);

			waiting.setItem(ResourcesConstants.PAY_TYPE_MORE_BACK);

			ClientEmployeeBean employee = clientEmployeeDao.selectByPrimaryKey(bean.getClient_employee_pk());
			ClientBean client = clientDao.selectByPrimaryKey(employee.getFinancial_body_pk());

			waiting.setReceiver(client.getClient_short_name());
			waiting.setRelated_pk(bean.getPk());
			waiting.setMoney(bean.getReceived().negate());
			waiting.setApply_user(bean.getCreate_user());
			waiting.setApproval_user(user_number);
			waiting.setStatus(ResourcesConstants.PAY_STATUS_ING);
			accPaidService.insert(waiting);
		}
		return SUCCESS;
	}

	@Override
	public String rejectMoreBack(String back_pk) {
		List<ClientReceivedDetailBean> details = receivedDao.selectByRelatedPks(back_pk);
		for (ClientReceivedDetailBean bean : details) {
			bean.setConfirm_time(DateUtil.getTimeMillis());
			bean.setStatus(ResourcesConstants.PAID_STATUS_NO);
			receivedDao.update(bean);
		}
		return SUCCESS;
	}

	@Override
	public String rejectPayApply(String reimbursement_pk) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		ReimbursementBean bean = reimDao.selectByPk(reimbursement_pk);
		bean.setApproval_user(sessionBean.getUser_number());
		bean.setApproval_time(DateUtil.getTimeMillis());
		bean.setStatus(ResourcesConstants.PAID_STATUS_NO);
		reimDao.update(bean);

		return SUCCESS;
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

		return SUCCESS;
	}

	@Autowired
	private AirTicketPaidDetailDAO airTicketPaidDetailDao;

	@Autowired
	private AirTicketPayableDAO airTicketPayableDao;

	@Override
	public String rollBackAirTicketPayApply(String related_pk) {

		List<AirTicketPaidDetailBean> paids = airTicketPaidDetailDao.selectByRelatedPk(related_pk);

		for (AirTicketPaidDetailBean paid : paids) {

			String base_pk = paid.getBase_pk();

			AirTicketPayableBean payable = airTicketPayableDao.selectByPrimaryKey(base_pk);

			payable.setPaid(payable.getPaid().subtract(paid.getMoney()));
			payable.setBudget_balance(payable.getBudget_balance().add(paid.getMoney()));
			if (payable.getFinal_flg().equals("Y")) {
				payable.setFinal_balance(payable.getFinal_balance().add(paid.getMoney()));
			}

			airTicketPayableDao.update(payable);

			airTicketPaidDetailDao.delete(paid.getPk());
		}

		return SUCCESS;
	}

	@Override
	public String rollBackPayApply(String pk) {
		reimDao.deleteByPk(pk);
		return SUCCESS;
	}

	@Override
	public String agreeFlyApply(String back_pk) {
		List<ClientReceivedDetailBean> details = receivedDao.selectByRelatedPks(back_pk);
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String user_number = sessionBean.getUser_number();
		for (ClientReceivedDetailBean bean : details) {
			bean.setConfirm_time(DateUtil.getTimeMillis());
			bean.setStatus(ResourcesConstants.PAID_STATUS_YES);

			receivedDao.update(bean);
			WaitingForPaidBean waiting = new WaitingForPaidBean();

			String pay_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
					ResourcesConstants.PAY_TYPE_FLY, DateUtil.getDateStr(DateUtil.YYYYMMDD));
			waiting.setPay_number(pay_number);

			waiting.setItem(ResourcesConstants.PAY_TYPE_FLY);

			ClientEmployeeBean employee = clientEmployeeDao.selectByPrimaryKey(bean.getClient_employee_pk());
			ClientBean client = clientDao.selectByPrimaryKey(employee.getFinancial_body_pk());

			waiting.setReceiver(client.getClient_short_name());
			waiting.setRelated_pk(bean.getPk());
			waiting.setMoney(bean.getReceived().negate());
			waiting.setApply_user(bean.getCreate_user());
			waiting.setApproval_user(user_number);
			waiting.setStatus(ResourcesConstants.PAY_STATUS_ING);
			accPaidService.insert(waiting);
		}
		return SUCCESS;
	}

	@Override
	public String rejectFlyApply(String back_pk) {
		List<ClientReceivedDetailBean> details = receivedDao.selectByRelatedPks(back_pk);
		for (ClientReceivedDetailBean bean : details) {
			bean.setConfirm_time(DateUtil.getTimeMillis());
			bean.setStatus(ResourcesConstants.PAID_STATUS_NO);
			receivedDao.update(bean);
		}
		return SUCCESS;
	}

}
