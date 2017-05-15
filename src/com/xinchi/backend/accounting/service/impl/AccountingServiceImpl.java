package com.xinchi.backend.accounting.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.service.AccountingService;
import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Service
@Transactional
public class AccountingServiceImpl implements AccountingService {

	@Autowired
	private PaidDAO paidDao;

	@Override
	public String updateRelatedPaid(String related_pk, String status) {
		SupplierPaidDetailBean options = new SupplierPaidDetailBean();
		options.setRelated_pk(related_pk);
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
