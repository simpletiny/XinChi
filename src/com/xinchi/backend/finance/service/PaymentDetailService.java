package com.xinchi.backend.finance.service;

import java.util.List;

import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.tools.Page;

public interface PaymentDetailService {
	public void insert(PaymentDetailBean bo);

	public List<PaymentDetailBean> getAllDetailsByParam(PaymentDetailBean bean);

	public List<PaymentDetailBean> getAllDetailsByPage(
			Page<PaymentDetailBean> page);
}
