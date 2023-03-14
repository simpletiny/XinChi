package com.xinchi.backend.payable.service;

import java.util.List;

import com.xinchi.bean.PayableBean;
import com.xinchi.bean.PayableSummaryBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "应付款")
public interface PayableService {

	@LogDescription(ignore = true)
	public void insert(PayableBean payable);

	public List<PayableBean> selectByParam(PayableBean payable);

	@LogDescription(ignore = true)
	public void updateByTeamNumber(String team_number);

	@LogDescription(ignore = true)
	public void update(PayableBean payable);

	@LogDescription(des = "搜索应付款")
	public List<PayableBean> searchPayableByPage(Page<PayableBean> page);

	@LogDescription(ignore = true)
	public PayableSummaryBean searchPayableSummary(String user_number);

	@LogDescription(ignore = true)
	public void updatePayablePaid(SupplierPaidDetailBean detail);

	@LogDescription(ignore = true)
	public void deletePayableByTeamNumber(String team_number);

	public void deleteByPk(String pk);

	public List<PayableSummaryBean> searchPayableSummaryByPage(Page<PayableSummaryBean> page);
}
