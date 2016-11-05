package com.xinchi.backend.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.finance.dao.PaymentDetailDAO;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.bean.CardBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;

@Service
public class PaymentDetailServiceImpl implements PaymentDetailService {
	@Autowired
	private PaymentDetailDAO dao;

	@Autowired
	private CardDAO cardDao;

	@Override
	@Transactional
	public void insert(PaymentDetailBean bo) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		bo.setRecord_user(sessionBean.getUser_number());
		bo.setRecord_time(DateUtil.getTimeMillis());
		CardBean card = cardDao.getCardByAccount(bo.getAccount());
		if (null != card) {
			card.setBalance(bo.getBalance());
			cardDao.update(card);
		}
		dao.insert(bo);
	}

	@Override
	public List<PaymentDetailBean> getAllDetailsByParam(PaymentDetailBean bean) {

		return dao.selectAllDetailsByParam(bean);
	}

	@Override
	public List<PaymentDetailBean> getAllDetailsByPage(
			Page<PaymentDetailBean> page) {
		return dao.selectAllDetailsByPage(page);
	}
}
