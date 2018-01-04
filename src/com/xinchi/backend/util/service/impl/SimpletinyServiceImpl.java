package com.xinchi.backend.util.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.finance.dao.PaymentDetailDAO;
import com.xinchi.backend.util.service.SimpletinyService;
import com.xinchi.bean.CardBean;
import com.xinchi.bean.PaymentDetailBean;

@Service
@Transactional
public class SimpletinyServiceImpl implements SimpletinyService {
	@Autowired
	private CardDAO cardDao;

	@Autowired
	private PaymentDetailDAO paymentDetailDAO;

	@Override
	public void autoFixBalance(String account_name) {
		// 取得所有账户
		CardBean cardOption = new CardBean();
		cardOption.setAccount(account_name);
		List<CardBean> cards = cardDao.getAllByParam(cardOption);

		PaymentDetailBean detail = new PaymentDetailBean();
		for (CardBean card : cards) {
			detail.setAccount(card.getAccount());
			List<PaymentDetailBean> details = paymentDetailDAO.selectAllDetailsByParam(detail);
			if (null != details && details.size() > 0) {
				BigDecimal balance = card.getInit_money();

				for (int i = 0; i < details.size(); i++) {
					PaymentDetailBean de = details.get(i);
					BigDecimal wrong = de.getMoney();
					if (de.getType().equals("支出")) {
						wrong = wrong.multiply(new BigDecimal(-1));
					}
					balance = balance.add(wrong);
					de.setBalance(balance);
					paymentDetailDAO.updateDetail(de);
				}
				card.setBalance(balance);
				cardDao.update(card);
			}
		}

	}

}
