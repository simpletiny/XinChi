package com.xinchi.backend.finance.dao;

import java.util.List;

import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.tools.Page;

public interface PaymentDetailDAO {
	public void insert(PaymentDetailBean bo);

	public List<PaymentDetailBean> selectAllDetailsByParam(
			PaymentDetailBean bean);

	public List<PaymentDetailBean> selectAllDetailsByPage(
			Page<PaymentDetailBean> page);
}
