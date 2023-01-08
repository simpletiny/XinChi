package com.xinchi.backend.payable.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.finance.service.CardService;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.payable.service.PaidService;
import com.xinchi.bean.CardBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaidAction extends BaseAction {
	private static final long serialVersionUID = 242023683208406623L;
	private SupplierPaidDetailBean detail;

	private String allot_json;

	@Autowired
	private PaidService paidService;

	// 返款收入
	public String applyBackRecive() {
		resultStr = paidService.applyBackRecive(detail, allot_json);

		return SUCCESS;
	}

	public String applyPay() {
		resultStr = paidService.applyPay(detail, allot_json);
		return SUCCESS;
	}

	// 扣款申请
	public String applyDeduct() {
		resultStr = paidService.applyDeduct(detail, allot_json);
		return SUCCESS;
	}

	private String json;

	// 冲账申请
	public String applyStrike() {
		resultStr = paidService.applyStrike(json);
		return SUCCESS;
	}

	private List<SupplierPaidDetailBean> paids;

	public String searchPaidByPage() {

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN) && SimpletinyString.isEmpty(detail.getCreate_user())) {
			detail.setCreate_user(sessionBean.getUser_number());
		}

		params.put("bo", detail);

		page.setParams(params);

		paids = paidService.getAllPaidsByPage(page);

		return SUCCESS;
	}

	public String searchPaidDetailsByRelatedPk() {
		paids = paidService.selectByRelatedPk(related_pk);
		return SUCCESS;
	}

	private String related_pk;

	public String rollBackPayApply() {
		resultStr = paidService.rollBackPayApply(related_pk);
		return SUCCESS;
	}

	@Autowired
	private AccPaidService accPaidService;

	@Autowired
	private PaymentDetailService paymentDetailService;

	@Autowired
	private CardService cardService;

	/**
	 * 查看出纳支付详情
	 * 
	 * @return
	 */
	public String searchPaidInfo() {
		detail = paidService.selectPaidDetailByRelatedPk(related_pk);
		List<WaitingForPaidBean> wfps = accPaidService.selectWfpByRelatedPk(related_pk);
		if (null == wfps || wfps.size() != 1) {
			return SUCCESS;
		}
		WaitingForPaidBean wfp = wfps.get(0);
		String voucher_number = wfp.getPay_number();
		List<PaymentDetailBean> pds = paymentDetailService.selectByVoucherNumber(voucher_number);
		String voucher_file_name = "";
		if (null != pds && pds.size() > 0) {

			for (PaymentDetailBean pd : pds) {
				CardBean card = cardService.selectByAccount(pd.getAccount());
				voucher_file_name += card.getPk() + "," + pd.getVoucher_file_name() + ";";
			}
		}

		voucher_file_name = voucher_file_name.substring(0, voucher_file_name.length() - 1);
		detail.setVoucher_file_name(voucher_file_name);
		return SUCCESS;
	}

	public SupplierPaidDetailBean getDetail() {
		return detail;
	}

	public void setDetail(SupplierPaidDetailBean detail) {
		this.detail = detail;
	}

	public String getAllot_json() {
		return allot_json;
	}

	public void setAllot_json(String allot_json) {
		this.allot_json = allot_json;
	}

	public List<SupplierPaidDetailBean> getPaids() {
		return paids;
	}

	public void setPaids(List<SupplierPaidDetailBean> paids) {
		this.paids = paids;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
