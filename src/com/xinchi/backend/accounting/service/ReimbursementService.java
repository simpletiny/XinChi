package com.xinchi.backend.accounting.service;

import java.util.List;

import com.xinchi.bean.ReimbursementBean;
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "支出报销")
public interface ReimbursementService extends BaseService {

	@LogDescription(des = "报销申请")
	public String save(ReimbursementBean reimbursement);

	@LogDescription(ignore = true)
	public ReimbursementBean selectByPk(String related_pk);

	@LogDescription(des = "报销更新")
	public void update(ReimbursementBean reim);

	public List<ReimbursementBean> selectByPage(Page page);

}
