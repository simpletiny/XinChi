package com.xinchi.backend.receivable.dao;

import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.ReceivableSummaryBean;

public interface ReceivableDAO {

	public void insert(ReceivableBean receivable);

	public ReceivableSummaryBean selectReceivableSummary(String sales);

	public ReceivableBean selectReceivableByTeamNumber(String teamNumber);

	public void update(ReceivableBean receivable);

}
