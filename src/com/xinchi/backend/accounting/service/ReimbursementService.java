package com.xinchi.backend.accounting.service;

import com.xinchi.bean.ReimbursementBean;
import com.xinchi.common.LogDescription;

@LogDescription(des = "支出报销")
public interface ReimbursementService {

	@LogDescription(des = "报销申请")
	public String save(ReimbursementBean reimbursement);

	@LogDescription(ignore = true)
	public ReimbursementBean selectByPk(String related_pk);

	@LogDescription(des = "报销更新")
	public void update(ReimbursementBean reim);

}
