package com.xinchi.backend.accounting.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xinchi.bean.ReimbursementBean;
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "支出报销")
public interface ReimbursementService extends BaseService {

	@LogDescription(des = "报销申请")
	public String save(ReimbursementBean reimbursement);

	@LogDescription(ignore = true)
	public ReimbursementBean selectByPk(String reimbursement_pk);

	@LogDescription(des = "报销更新")
	public void update(ReimbursementBean reim);

	public List<ReimbursementBean> selectByPage(Page page);

	public String deleteReibursement(List<String> reimbursement_pks);

	public String reApply(ReimbursementBean reimbursement);

	public Map<String, BigDecimal> searchSummaries(ReimbursementBean reimbursement);
	
	public List<ReimbursementBean> selectByParam(ReimbursementBean option);

	public List<ReimbursementBean> selectSumByParam(ReimbursementBean option);

}
