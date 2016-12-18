package com.xinchi.backend.payable.dao;

import com.xinchi.bean.PayableBean;
import com.xinchi.bean.PayableSummaryBean;

public interface PayableDAO {

	public void insert(PayableBean payable);

	public PayableBean selectByParam(PayableBean payable);

	public void update(PayableBean payable);

	public PayableSummaryBean selectPayableSummary(String user_number);

}
