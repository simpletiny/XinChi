package com.xinchi.backend.accounting.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.payable.service.PaidService;
import com.xinchi.backend.supplier.service.SupplierService;
import com.xinchi.bean.PaidDetailSummary;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.SupplierBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccPaidAction extends BaseAction {
	private static final long serialVersionUID = 7171145601340932783L;

	private WaitingForPaidBean wfp;

	private List<WaitingForPaidBean> wfps;

	@Autowired
	private AccPaidService service;

	public String searchWaitingForPaidByPage() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", wfp);
		page.setParams(params);

		wfps = service.selectByPage(page);
		return SUCCESS;
	}

	private String wfp_pk;

	private SupplierBean supplier;

	@Autowired
	private SupplierService supplierService;

	public String searchOneWFP() {
		wfp = service.selectByPk(wfp_pk);
		SupplierPaidDetailBean paidDetail = paidService.selectPaidDetailByRelatedPk(wfp.getRelated_pk());
		if (null != paidDetail) {
			supplier = supplierService.selectByPrimaryKey(paidDetail.getSupplier_employee_pk());
		}
		return SUCCESS;
	}

	private String json;
	private String voucher_number;

	@Autowired
	private PaymentDetailService pds;
	@Autowired
	private PaidService paidService;

	public String pay() {
		resultStr = service.pay(json, voucher_number);
		return SUCCESS;
	}

	/**
	 * 将已支付打回到待支付状态
	 * 
	 * @return
	 */
	public String rollBackPay() {

		resultStr = service.rollBackPay(voucher_number);
		return SUCCESS;
	}

	/**
	 * 出纳打回有问题的待支付单子
	 * 
	 * @return
	 */
	public String rollBackWfp() {
		resultStr = service.rollBackWfp(wfp_pk);
		return SUCCESS;
	}

	private List<PaymentDetailBean> details;
	private PaidDetailSummary ps;

	// 查询支付详情
	public String searchPaidDetailByPayNumber() {
		details = pds.selectByVoucherNumber(voucher_number);
		ps = service.selectPaidDetailSummaryByPayNumber(voucher_number);

		return SUCCESS;
	}

	public WaitingForPaidBean getWfp() {
		return wfp;
	}

	public void setWfp(WaitingForPaidBean wfp) {
		this.wfp = wfp;
	}

	public List<WaitingForPaidBean> getWfps() {
		return wfps;
	}

	public void setWfps(List<WaitingForPaidBean> wfps) {
		this.wfps = wfps;
	}

	public String getWfp_pk() {
		return wfp_pk;
	}

	public void setWfp_pk(String wfp_pk) {
		this.wfp_pk = wfp_pk;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getVoucher_number() {
		return voucher_number;
	}

	public void setVoucher_number(String voucher_number) {
		this.voucher_number = voucher_number;
	}

	public List<PaymentDetailBean> getDetails() {
		return details;
	}

	public void setDetails(List<PaymentDetailBean> details) {
		this.details = details;
	}

	public PaidDetailSummary getPs() {
		return ps;
	}

	public void setPs(PaidDetailSummary ps) {
		this.ps = ps;
	}

	public SupplierBean getSupplier() {
		return supplier;
	}

	public void setSupplier(SupplierBean supplier) {
		this.supplier = supplier;
	}
}
