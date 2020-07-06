package com.xinchi.backend.payable.dao;

import java.math.BigDecimal;
import java.util.List;

import com.xinchi.bean.KeyValueDto;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.PayableSummaryBean;
import com.xinchi.tools.Page;

public interface PayableDAO {

	public void insert(PayableBean payable);

	public List<PayableBean> selectByParam(PayableBean payable);

	public void update(PayableBean payable);

	public PayableSummaryBean selectPayableSummary(String user_number);

	public List<PayableBean> selectAllPayableWithSupplier();

	public void deleteByTeamNumber(String team_number);

	public List<PayableBean> selectByPage(Page<PayableBean> page);

	public void deleteByPk(String pk);

	public PayableBean selectByTeamNumber(String team_number);

	public BigDecimal selectSumPayable();

	public List<KeyValueDto> selectPayableWithArea();

}
