package com.xinchi.backend.finance.service;

import java.util.List;

import com.xinchi.bean.PaymentDetailBean;

public interface PaymentDetailService {
	public void insert(PaymentDetailBean bo);

	public List<PaymentDetailBean> getAllDetailsByParam(PaymentDetailBean bean);
}
