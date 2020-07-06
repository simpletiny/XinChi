package com.xinchi.backend.accounting.service;

import java.math.BigDecimal;
import java.util.List;

import com.xinchi.bean.PaidDetailSummary;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

/**
 * Acc for Accounting,意在指会计中的支付
 * 
 * @author simpletiny
 *
 */
@LogDescription(des = "支付逻辑")
public interface AccPaidService extends BaseService {
	@LogDescription(des = "生成待支付")
	public String insert(WaitingForPaidBean paid);

	@LogDescription(des = "搜索待支付数据")
	public List<WaitingForPaidBean> selectByPage(Page page);

	@LogDescription(des = "查看待支付详情")
	public WaitingForPaidBean selectByPk(String wfp_pk);

	@LogDescription(des = "通过凭证号查看待支付详情")
	public WaitingForPaidBean selectByPayNumber(String voucher_number);

	@LogDescription(des = "更新待支付")
	public void update(WaitingForPaidBean wfp);

	@LogDescription(des = "通过凭证号查看支付详情")
	public PaidDetailSummary selectPaidDetailSummaryByPayNumber(String voucher_number);

	public String rollBackWfp(String wfp_pk);

	public String rollBackPay(String voucher_number);

	public BigDecimal selectSumWFP();
}
