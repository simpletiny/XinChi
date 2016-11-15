package com.xinchi.backend.receivable.service;

import java.util.List;

import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.ReceivableSummaryBean;
import com.xinchi.tools.Page;

public interface ReceivableService {

	public void insert(ReceivableBean receivable);

	public ReceivableSummaryBean searchReceivableSummary(String sales);

	public List<ReceivableBean> searchReceivableByPage(Page<ReceivableBean> page);

}
