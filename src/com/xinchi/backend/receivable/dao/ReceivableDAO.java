package com.xinchi.backend.receivable.dao;

import java.math.BigDecimal;
import java.util.List;

import com.xinchi.bean.KeyValueDto;
import com.xinchi.bean.ReceivableBalanceDto;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.ReceivableSummaryBean;
import com.xinchi.tools.Page;

public interface ReceivableDAO {

	public String insert(ReceivableBean receivable);

	public ReceivableSummaryBean selectReceivableSummary(String sales);

	public ReceivableBean selectReceivableByTeamNumber(String teamNumber);

	public void update(ReceivableBean receivable);

	public List<ReceivableBean> selectAllReceivablesWithFinancial();

	public List<ReceivableBean> selectByPage(Page<ReceivableBean> page);

	public void deleteByTeamNumber(String team_number);

	public BigDecimal fetchEmployeeBalance(String client_employee_pk);

	public BigDecimal selectSumReceivable();

	public List<KeyValueDto> selectReceivableWithClient();

	public List<KeyValueDto> selectReceivableWithSales();

	public ReceivableBalanceDto selectUserReceivableBalanceByUserNumber(String user_number);

}
