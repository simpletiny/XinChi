package com.xinchi.backend.accounting.service;

import com.xinchi.common.LogDescription;

@LogDescription(des = "收支审批")
public interface AccountingService {
	@LogDescription(ignore = true)
	public String updateRelatedPaid(String related_pk, String status);
	
	@LogDescription(ignore = true)
	public String updatePaid(String pk, String status);
	
	@LogDescription(des = "同意支出申请")
	public String agreePayApply(String reimbursement_pk);

	@LogDescription(des = "拒绝支出申请")
	public String rejectPayApply(String reimbursement_pk);

	@LogDescription(des = "支出申请打回重报")
	public String rollBackRelatedPayApply(String related_pk);
	
	@LogDescription(des = "支出申请打回重报")
	public String rollBackPayApply(String pk);

}
