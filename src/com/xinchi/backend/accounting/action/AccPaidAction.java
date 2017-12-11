package com.xinchi.backend.accounting.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.accounting.service.ReimbursementService;
import com.xinchi.backend.finance.service.CardService;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.payable.service.AirTicketPaidDetailService;
import com.xinchi.backend.payable.service.PaidService;
import com.xinchi.backend.supplier.service.SupplierService;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.CardBean;
import com.xinchi.bean.PaidDetailSummary;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.ReimbursementBean;
import com.xinchi.bean.SupplierBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
	private CardService cs;
	@Autowired
	private PaidService paidService;

	@Autowired
	private ReimbursementService reimService;

	@Autowired
	private AirTicketPaidDetailService airTicketPaidDetailService;

	public String pay() {
		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String account = obj.getString("account");
			String time = obj.getString("time");
			String receiver = obj.getString("receiver");
			BigDecimal balance = new BigDecimal(cs.getAccountBalance(account));
			BigDecimal money = new BigDecimal(obj.getString("money"));
			String voucher_file_name = obj.getString("voucherFile");

			PaymentDetailBean detail = new PaymentDetailBean();
			detail.setVoucher_number(voucher_number);
			detail.setAccount(account);
			detail.setTime(time);
			detail.setReceiver(receiver);
			detail.setMoney(money);
			detail.setBalance(balance.subtract(money));
			detail.setType("支出");
			detail.setComment(receiver + ",凭证号：" + voucher_number);
			detail.setVoucher_file_name(voucher_file_name);
			resultStr = pds.insert(detail);
			if (!resultStr.equals(SUCCESS)) {
				return SUCCESS;
			}
		}
		// 更新待支付状态
		wfp = service.selectByPayNumber(voucher_number);
		wfp.setStatus(ResourcesConstants.PAY_STATUS_YES);
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		wfp.setPay_user(sessionBean.getUser_number());
		service.update(wfp);

		String related_pk = wfp.getRelated_pk();
		// 更新申请状态
		if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			List<SupplierPaidDetailBean> details = paidService.selectByRelatedPk(related_pk);
			for (SupplierPaidDetailBean detail : details) {
				detail.setTime(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
				detail.setStatus(ResourcesConstants.PAID_STATUS_PAID);
				paidService.update(detail);
			}
		} else if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_PIAOWU)) {
			List<AirTicketPaidDetailBean> paids = airTicketPaidDetailService.selectByRelatedPk(related_pk);
			for (AirTicketPaidDetailBean paid : paids) {
				paid.setStatus(ResourcesConstants.PAID_STATUS_PAID);
				paid.setTime(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
				airTicketPaidDetailService.update(paid);
			}
		} else {
			ReimbursementBean reim = reimService.selectByPk(related_pk);
			reim.setPay_user(sessionBean.getUser_number());
			reim.setPay_time(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
			reim.setStatus(ResourcesConstants.PAID_STATUS_PAID);
			reimService.update(reim);
		}

		resultStr = SUCCESS;
		return SUCCESS;
	}

	@Autowired
	private CardService cardService;

	/**
	 * 将已支付打回到待支付状态
	 * 
	 * @return
	 */
	public String rollBackPay() {

		// 删除银行流水支出单,删除上传的凭证
		List<PaymentDetailBean> details = pds.selectByVoucherNumber(voucher_number);
		for (PaymentDetailBean detail : details) {
			String voucher_file = detail.getVoucher_file_name();

			String fileFolder = PropertiesUtil.getProperty("voucherFileFolder");
			CardBean card = cardService.selectByAccount(detail.getAccount());
			File destfile = new File(fileFolder + File.separator + card.getPk() + File.separator + voucher_file);
			destfile.delete();

			pds.deleteDetail(detail.getPk());

		}

		// 更新待支付状态
		wfp = service.selectByPayNumber(voucher_number);
		wfp.setStatus(ResourcesConstants.PAY_STATUS_ING);
		wfp.setPay_user("");
		service.update(wfp);

		String related_pk = wfp.getRelated_pk();

		// 更新申请状态
		if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			List<SupplierPaidDetailBean> supplierDetails = paidService.selectByRelatedPk(related_pk);
			for (SupplierPaidDetailBean detail : supplierDetails) {
				detail.setTime("");
				detail.setStatus(ResourcesConstants.PAID_STATUS_YES);
				paidService.update(detail);
			}
		} else if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_PIAOWU)) {
			List<AirTicketPaidDetailBean> paids = airTicketPaidDetailService.selectByRelatedPk(related_pk);
			for (AirTicketPaidDetailBean paid : paids) {
				paid.setStatus(ResourcesConstants.PAID_STATUS_YES);
				paid.setTime(DateUtil.getDateStr(""));
				airTicketPaidDetailService.update(paid);
			}
		} else {
			ReimbursementBean reim = reimService.selectByPk(related_pk);
			reim.setPay_user("");
			reim.setPay_time("");
			reim.setStatus(ResourcesConstants.PAID_STATUS_YES);
			reimService.update(reim);
		}
		resultStr = SUCCESS;
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
