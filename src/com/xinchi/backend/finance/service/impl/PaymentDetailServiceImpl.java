package com.xinchi.backend.finance.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.finance.dao.PaymentDetailDAO;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.bean.CardBean;
import com.xinchi.bean.InnerTransferBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.common.DBCommonUtil;
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

	@Override
	@Transactional
	public void saveInnerDetail(InnerTransferBean innerTransfer) {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		CardBean fromCard = cardDao.getCardByAccount(innerTransfer
				.getFrom_account());

		CardBean toCard = cardDao.getCardByAccount(innerTransfer
				.getTo_account());

		String inner_pk = DBCommonUtil.genPk();

		// 支出明细
		PaymentDetailBean payDetail = new PaymentDetailBean();
		payDetail.setAccount(innerTransfer.getFrom_account());
		payDetail.setTime(innerTransfer.getTime());
		payDetail.setType("支出");
		payDetail.setMoney(innerTransfer.getMoney());
		payDetail.setRecord_user(sessionBean.getUser_number());
		payDetail.setRecord_time(DateUtil.getTimeMillis());
		payDetail.setComment(innerTransfer.getComment());
		payDetail.setInner_flg("Y");
		payDetail.setInner_pk(inner_pk);

		// 收入明细
		PaymentDetailBean receiveDetail = new PaymentDetailBean();

		receiveDetail.setAccount(innerTransfer.getTo_account());
		receiveDetail.setTime(innerTransfer.getTime());
		receiveDetail.setType("收入");
		receiveDetail.setMoney(innerTransfer.getMoney());
		receiveDetail.setRecord_user(sessionBean.getUser_number());
		receiveDetail.setRecord_time(DateUtil.getTimeMillis());
		receiveDetail.setComment(innerTransfer.getComment());
		receiveDetail.setInner_flg("Y");
		receiveDetail.setInner_pk(inner_pk);

		// 计算明细余额
		BigDecimal payBalance = fromCard.getBalance().subtract(
				payDetail.getMoney());
		BigDecimal receiveBalance = toCard.getBalance().add(
				payDetail.getMoney());
		payDetail.setBalance(payBalance);
		receiveDetail.setBalance(receiveBalance);

		dao.insert(payDetail);
		dao.insert(receiveDetail);

		// 汇兑明细
		if (null != innerTransfer.getExchange_account()) {
			PaymentDetailBean exchangeDetail = new PaymentDetailBean();

			if (innerTransfer.getExchange_account().equals("out")) {
				exchangeDetail.setAccount(innerTransfer.getFrom_account());
				payBalance = payBalance.subtract(innerTransfer
						.getExchange_money());
				exchangeDetail.setBalance(payBalance);

			} else {
				exchangeDetail.setAccount(innerTransfer.getTo_account());
				receiveBalance = receiveBalance.subtract(innerTransfer
						.getExchange_money());

				exchangeDetail.setBalance(receiveBalance);
			}

			exchangeDetail.setType("支出");
			exchangeDetail.setTime(innerTransfer.getTime());
			exchangeDetail.setMoney(innerTransfer.getExchange_money());
			exchangeDetail.setRecord_user(sessionBean.getUser_number());
			exchangeDetail.setRecord_time(DateUtil.getTimeMillis());
			exchangeDetail.setComment("汇兑："+innerTransfer.getComment());
			exchangeDetail.setInner_flg("Y");
			exchangeDetail.setInner_pk(inner_pk);

			dao.insert(exchangeDetail);
		}

		// 更新账户
		fromCard.setBalance(payBalance);
		toCard.setBalance(receiveBalance);
		cardDao.update(fromCard);
		cardDao.update(toCard);
	}
}
