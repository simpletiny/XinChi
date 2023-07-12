package com.xinchi.backend.receivable.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.ReceivedDetailDto;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReceivedAction extends BaseAction {
	private static final long serialVersionUID = -7691732442564602977L;

	private ClientReceivedDetailBean detail;

	@Autowired
	private ReceivedService service;

	/**
	 * 抹零申请
	 * 
	 * @return
	 */
	public String applyRidTail() {
		resultStr = service.applyRidTail(detail);
		return SUCCESS;
	}

	/**
	 * 代收
	 * 
	 * @return
	 */
	public String applyCollect() {
		resultStr = service.applyCollect(detail);
		return SUCCESS;
	}

	/**
	 * 98清尾
	 * 
	 * @return
	 */
	public String applyTail98() {
		resultStr = service.applyTail98(detail);
		return SUCCESS;
	}

	private String team_number;

	public String checkIs98() {
		resultStr = service.checkIs98(team_number);
		return SUCCESS;
	}

	// 收入申请
	public String applyReceive() {
		resultStr = service.applyReceive(detail);
		return SUCCESS;
	}

	private String allot_json;

	/**
	 * 合账申请
	 * 
	 * @return
	 */
	public String applySum() {
		resultStr = service.applySum(detail, allot_json);
		return SUCCESS;
	}

	/**
	 * 单笔多付返还支付申请
	 * 
	 * @return
	 */
	public String applyIfMorePay() {
		resultStr = service.applyIfMorePay(detail, allot_json);
		return SUCCESS;
	}

	private String strike_out_json;
	private String strike_in_json;

	/**
	 * 冲账申请
	 * 
	 * @return
	 */
	public String applyStrike() {
		resultStr = service.applyStrike(detail, strike_out_json, strike_in_json);
		return SUCCESS;
	}

	// 返佣申请
	public String applyFly() {
		resultStr = service.applyFly(detail);
		return SUCCESS;
	}

	private List<ClientReceivedDetailBean> receiveds;

	public String searchReceivedByPage() {

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)
				&& !roles.contains(ResourcesConstants.USER_ROLE_CASHIER)) {
			detail.setCreate_user(sessionBean.getUser_number());
		}
		params.put("bo", detail);

		page.setParams(params);

		receiveds = service.getAllReceivedsByPage(page);
		return SUCCESS;
	}

	private String received_pks;

	public String rollBackReceived() {
		resultStr = service.rollBackReceived(received_pks);
		return SUCCESS;
	}

	private String related_pks;

	public String searchByRelatedPks() {
		receiveds = service.selectByRelatedPks(related_pks);

		return SUCCESS;
	}

	public String rejectReceived() {
		resultStr = service.rejectRecived(related_pks);
		return SUCCESS;
	}

	public String searchReceivedByTeamNumber() {
		receiveds = service.selectByTeamNumber(team_number);
		return SUCCESS;
	}

	private List<PaymentDetailBean> paymentDetails;

	@Autowired
	private AccPaidService accPaidService;

	@Autowired
	private PaymentDetailService paymentDetailService;

	private String related_pk;

	public String searchPayVoucherInfo() {
		List<WaitingForPaidBean> wfps = accPaidService.selectWfpByRelatedPk(related_pk);
		if (null == wfps || wfps.size() != 1) {
			return SUCCESS;
		}

		WaitingForPaidBean wfp = wfps.get(0);

		paymentDetails = paymentDetailService.selectByVoucherNumber(wfp.getPay_number());

		return SUCCESS;
	}

	private String from_where;
	private List<ReceivedDetailDto> received_details;

	public String searchAllAboutReceivedByRelatedPks() {
		received_details = service.searchAllAboutReceivedByRelatedPks(related_pk, from_where);
		return SUCCESS;
	}

	public ClientReceivedDetailBean getDetail() {
		return detail;
	}

	public void setDetail(ClientReceivedDetailBean detail) {
		this.detail = detail;
	}

	public String getAllot_json() {
		return allot_json;
	}

	public void setAllot_json(String allot_json) {
		this.allot_json = allot_json;
	}

	public List<ClientReceivedDetailBean> getReceiveds() {
		return receiveds;
	}

	public void setReceiveds(List<ClientReceivedDetailBean> receiveds) {
		this.receiveds = receiveds;
	}

	public String getReceived_pks() {
		return received_pks;
	}

	public void setReceived_pks(String received_pks) {
		this.received_pks = received_pks;
	}

	public String getRelated_pks() {
		return related_pks;
	}

	public void setRelated_pks(String related_pks) {
		this.related_pks = related_pks;
	}

	public String getStrike_out_json() {
		return strike_out_json;
	}

	public void setStrike_out_json(String strike_out_json) {
		this.strike_out_json = strike_out_json;
	}

	public String getStrike_in_json() {
		return strike_in_json;
	}

	public void setStrike_in_json(String strike_in_json) {
		this.strike_in_json = strike_in_json;
	}

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public List<PaymentDetailBean> getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(List<PaymentDetailBean> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public String getFrom_where() {
		return from_where;
	}

	public List<ReceivedDetailDto> getReceived_details() {
		return received_details;
	}

	public void setFrom_where(String from_where) {
		this.from_where = from_where;
	}

	public void setReceived_details(List<ReceivedDetailDto> received_details) {
		this.received_details = received_details;
	}

}
