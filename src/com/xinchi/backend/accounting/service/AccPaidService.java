package com.xinchi.backend.accounting.service;

import java.util.List;

import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.tools.Page;

/**
 * Acc for Accounting,意在指会计中的支付
 * @author simpletiny
 *
 */
public interface AccPaidService {
	public String insert(WaitingForPaidBean paid);

	public List<WaitingForPaidBean> selectByPage(Page page);

	public WaitingForPaidBean selectByPk(String wfp_pk);

	public WaitingForPaidBean selectByPayNumber(String voucher_number);

	public void update(WaitingForPaidBean wfp);
}
