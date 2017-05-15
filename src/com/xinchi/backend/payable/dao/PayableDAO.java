package com.xinchi.backend.payable.dao;

import java.util.List;

import com.xinchi.bean.PayableBean;
import com.xinchi.bean.PayableSummaryBean;

public interface PayableDAO {

	public void insert(PayableBean payable);

	public List<PayableBean> selectByParam(PayableBean payable);

	public void update(PayableBean payable);

	public PayableSummaryBean selectPayableSummary(String user_number);

	public List<PayableBean> selectAllPayableWithSupplier();

	public void deleteByTeamNumber(String team_number);

}
