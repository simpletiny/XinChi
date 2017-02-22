package com.xinchi.backend.finance.service;

import java.io.File;
import java.util.List;

import com.xinchi.bean.InnerTransferBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.tools.Page;

public interface PaymentDetailService {
	public void insert(PaymentDetailBean bo);

	public List<PaymentDetailBean> getAllDetailsByParam(PaymentDetailBean bean);

	public List<PaymentDetailBean> getAllDetailsByPage(
			Page<PaymentDetailBean> page);

	public void saveInnerDetail(InnerTransferBean innerTransfer);

	public String deleteDetail(String detailId);

	public PaymentDetailBean selectByPk(String detailId);

	public String updateDetail(PaymentDetailBean detail);
	
	public String importDetailExcel(File file);
}
