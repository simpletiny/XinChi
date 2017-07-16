package com.xinchi.backend.finance.service;

import java.io.File;
import java.util.List;

import com.xinchi.bean.InnerTransferBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "银行流水")
public interface PaymentDetailService {
	@LogDescription(des = "新建银行流水")
	public String insert(PaymentDetailBean bo);

	@LogDescription(ignore = true)
	public List<PaymentDetailBean> getAllDetailsByParam(PaymentDetailBean bean);

	@LogDescription(des = "搜索银行流水")
	public List<PaymentDetailBean> getAllDetailsByPage(Page<PaymentDetailBean> page);

	@LogDescription(des = "新建内转")
	public void saveInnerDetail(InnerTransferBean innerTransfer);

	@LogDescription(des = "删除银行流水")
	public String deleteDetail(String detailId);

	@LogDescription(ignore = true)
	public PaymentDetailBean selectByPk(String detailId);

	@LogDescription(des = "更新银行流水")
	public String updateDetail(PaymentDetailBean detail);

	@LogDescription(ignore = true)
	public String importDetailExcel(File file);

	@LogDescription(ignore = true)
	public List<PaymentDetailBean> selectByVoucherNumber(String voucher_number);
}
