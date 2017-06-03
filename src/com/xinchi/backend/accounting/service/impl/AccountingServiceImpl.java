package com.xinchi.backend.accounting.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.accounting.service.AccountingService;
import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.SupplierPaidDetailBean;
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
		SupplierPaidDetailBean options = new SupplierPaidDetailBean();
		options.setRelated_pk(related_pk);
		List<SupplierPaidDetailBean> paids = paidDao.selectByParam(options);

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String approve_user = sessionBean.getUser_number();

		// 生成待支付数据
		SupplierPaidDetailBean detail = paidDao.selectByRelatedPk(related_pk);
		WaitingForPaidBean waiting = new WaitingForPaidBean();
		String pay_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,ResourcesConstants.PAY_TYPE_DIJIE, DateUtil.getDateStr(DateUtil.YYYYMMDD));
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

		for (SupplierPaidDetailBean paid : paids) {
			paid.setStatus(status);
			paid.setApprove_user(approve_user);
			paidDao.update(paid);
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

}
