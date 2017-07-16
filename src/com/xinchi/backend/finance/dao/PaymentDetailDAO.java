package com.xinchi.backend.finance.dao;

import java.util.List;

import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.tools.Page;

public interface PaymentDetailDAO {
	public void insert(PaymentDetailBean bo);

	public List<PaymentDetailBean> selectAllDetailsByParam(PaymentDetailBean bean);

	public List<PaymentDetailBean> selectAllDetailsByPage(Page<PaymentDetailBean> page);

	public PaymentDetailBean selectById(String detailId);

	public List<PaymentDetailBean> selectAfterByParam(PaymentDetailBean detail);

	public void updateDetails(List<PaymentDetailBean> afterDetails);

	public void delete(String pk);

	public void updateDetail(PaymentDetailBean newDetail);

	public PaymentDetailBean selectPreDetail(PaymentDetailBean newDetail);

	public void insertDetails(List<PaymentDetailBean> details);

	public List<PaymentDetailBean> selectByVoucherNumber(String voucher_number);
}
