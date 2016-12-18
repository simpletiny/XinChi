package com.xinchi.backend.payable.service;

import java.util.List;

import com.xinchi.bean.PayableBean;
import com.xinchi.bean.PayableSummaryBean;
import com.xinchi.bean.ReceivableSummaryBean;
import com.xinchi.tools.Page;

public interface PayableService {

	public void insert(PayableBean payable);

	public void updateByTeamNumber(String team_number);

	public void update(PayableBean payable);

	public List<PayableBean> searchPayableByPage(Page<PayableBean> page);

	public PayableSummaryBean searchPayableSummary(String user_number);
}
