package com.xinchi.backend.accounting.service;

import java.util.List;

import com.xinchi.bean.ReceivedDetailDto;
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "收支审批")
public interface AccountingService extends BaseService {
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

	public String rollBackAirTicketPayApply(String related_pk);

	public String agreeAirTicketPayApply(String related_pk);

	public String rejectAirTicketPayApply(String related_pk);

	public String agreeMoreBack(String back_pk);

	public String rejectMoreBack(String back_pk);

	// 同意返佣支出
	public String agreeFlyApply(String back_pk);

	// 拒绝返佣支出
	public String rejectFlyApply(String back_pk);

	public List<ReceivedDetailDto> searchAllReceivedsByPage(Page<ReceivedDetailDto> page);

	public String suspensePayApply(String approval_pks);

}
