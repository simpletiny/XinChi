package com.xinchi.backend.finance.dao;

import java.util.List;

import com.xinchi.bean.PaymentDetailBean;

public interface PaymentDetailDAO {
	public void insert(PaymentDetailBean bo);

	public List<PaymentDetailBean> selectAllDetailsByParam(
			PaymentDetailBean bean);
}
