package com.xinchi.backend.receivable.service;

import java.util.List;

import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.ReceivableSummaryBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "应收款")
public interface ReceivableService {
	@LogDescription(ignore = true)
	public void insert(ReceivableBean receivable);

	@LogDescription(ignore = true)
	public ReceivableSummaryBean searchReceivableSummary(String sales);

	@LogDescription(des = "查询应收款")
	public List<ReceivableBean> searchReceivableByPage(Page<ReceivableBean> page);

	@LogDescription(ignore = true)
	public void updateByTeamNumber(String teamNumber);

	@LogDescription(ignore = true)
	public void update(ReceivableBean receivable);

	@LogDescription(ignore = true)
	public void updateReceivableReceived(ClientReceivedDetailBean detail);
}
