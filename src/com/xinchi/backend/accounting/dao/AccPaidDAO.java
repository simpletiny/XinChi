package com.xinchi.backend.accounting.dao;

import java.util.List;

import com.xinchi.bean.PaidDetailSummary;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.tools.Page;

public interface AccPaidDAO {
	public String insert(WaitingForPaidBean paid);

	public List<WaitingForPaidBean> selectByPage(Page page);

	public WaitingForPaidBean selectByPk(String wfp_pk);

	public WaitingForPaidBean selectByPayNumber(String voucher_number);

	public void update(WaitingForPaidBean wfp);

	public PaidDetailSummary selectPaidSummaryByPayNumber(String voucher_number);
	
	
	public void deleteByPk(String wfp_pk);
}
