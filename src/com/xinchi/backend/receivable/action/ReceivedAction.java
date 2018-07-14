package com.xinchi.backend.receivable.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.base.Joiner;
import com.xinchi.backend.accounting.service.PayApprovalService;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.FileFolder;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReceivedAction extends BaseAction {
	private static final long serialVersionUID = -7691732442564602977L;

	private ClientReceivedDetailBean detail;

	@Autowired
	private ReceivedService receivedService;

	@Autowired
	private ReceivableService receivableService;

	/**
	 * 抹零申请
	 * 
	 * @return
	 */
	public String applyRidTail() {
		ReceivableBean receivable = receivableService.selectByTeamNumber(detail.getTeam_number());
		detail.setType(ResourcesConstants.RECEIVED_TYPE_TAIL);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
		detail.setReceived_time(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
		detail.setClient_employee_pk(receivable.getClient_employee_pk());

		receivedService.insert(detail);
		receivableService.updateReceivableReceived(detail);

		resultStr = OK;
		return SUCCESS;
	}

	/**
	 * 代收
	 * 
	 * @return
	 */
	public String applyCollect() {
		ReceivableBean receivable = receivableService.selectByTeamNumber(detail.getTeam_number());
		detail.setType(ResourcesConstants.RECEIVED_TYPE_COLLECT);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);

		detail.setClient_employee_pk(receivable.getClient_employee_pk());
		receivedService.insert(detail);
		receivableService.updateReceivableReceived(detail);

		// 保存收入凭证
		String subFolder = detail.getReceived_time().substring(0, 4) + File.separator
				+ detail.getReceived_time().substring(5, 7);
		FileUtil.saveFile(detail.getVoucher_file(), FileFolder.CLIENT_RECEIVED_VOUCHER.value(), subFolder);

		resultStr = SUCCESS;
		return SUCCESS;
	}

	// 收入申请
	public String applyReceive() {
		detail.setType(ResourcesConstants.RECEIVED_TYPE_RECEIVED);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
		String pk = DBCommonUtil.genPk();
		detail.setPk(pk);
		detail.setRelated_pk(pk);

		receivedService.insertWithPk(detail);
		// 保存收入凭证
		String subFolder = detail.getReceived_time().substring(0, 4) + File.separator
				+ detail.getReceived_time().substring(5, 7);
		FileUtil.saveFile(detail.getVoucher_file(), FileFolder.CLIENT_RECEIVED_VOUCHER.value(), subFolder);
		receivableService.updateReceivableReceived(detail);

		resultStr = OK;
		return SUCCESS;
	}

	private String allot_json;

	/**
	 * 合账申请
	 * 
	 * @return
	 */
	public String applySum() {
		detail.setType(ResourcesConstants.RECEIVED_TYPE_SUM);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);

		JSONArray array = JSONArray.fromObject(allot_json);

		String[] pks = DBCommonUtil.genPks(array.size());
		detail.setRelated_pk(Joiner.on(",").join(pks));

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("received");
			detail.setTeam_number(t);
			detail.setPk(pks[i]);

			if (!SimpletinyString.isEmpty(r)) {
				detail.setReceived(new BigDecimal(r));
			}

			receivedService.insertWithPk(detail);
			receivableService.updateReceivableReceived(detail);
		}
		// 保存收入凭证
		String subFolder = detail.getReceived_time().substring(0, 4) + File.separator
				+ detail.getReceived_time().substring(5, 7);
		FileUtil.saveFile(detail.getVoucher_file(), FileFolder.CLIENT_RECEIVED_VOUCHER.value(), subFolder);

		resultStr = OK;
		return SUCCESS;
	}

	@Autowired
	private PayApprovalService payApprovalService;

	/**
	 * 单笔多付返还支付申请
	 * 
	 * @return
	 */
	public String applyIfMorePay() {

		detail.setType(ResourcesConstants.RECEIVED_TYPE_PAY);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);

		JSONArray array = JSONArray.fromObject(allot_json);

		String[] pks = DBCommonUtil.genPks(array.size());
		String related_pk = DBCommonUtil.genPk();
		detail.setRelated_pk(related_pk);

		detail.setReceived_time(DateUtil.getMinStr());
		detail.setAllot_received(detail.getAllot_received().negate());

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("received");
			detail.setTeam_number(t);
			detail.setPk(pks[i]);

			if (!SimpletinyString.isEmpty(r)) {
				detail.setReceived(new BigDecimal(r).negate());
			}

			receivedService.insertWithPk(detail);
			receivableService.updateReceivableReceived(detail);
		}
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		// 生成支付审批数据
		PayApprovalBean pa = new PayApprovalBean();
		pa.setReceiver(detail.getReceiver());
		pa.setMoney(detail.getAllot_received().negate());
		pa.setItem(ResourcesConstants.PAY_TYPE_MORE_BACK);
		pa.setStatus(ResourcesConstants.PAID_STATUS_ING);
		pa.setReceiver_card_number(detail.getReceiver_card_number());
		pa.setReceiver_bank(detail.getReceiver_bank());

		pa.setRelated_pk(related_pk);

		pa.setComment(detail.getComment());
		pa.setApply_user(sessionBean.getUser_number());
		pa.setBack_pk(related_pk);
		pa.setApply_time(DateUtil.getTimeMillis());
		pa.setLimit_time(detail.getLimit_time());

		payApprovalService.insert(pa);

		resultStr = SUCCESS;
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
		JSONArray out_array = JSONArray.fromObject(strike_out_json);

		String related_pk = DBCommonUtil.genPk();

		for (int i = 0; i < out_array.size(); i++) {
			ClientReceivedDetailBean current = new ClientReceivedDetailBean();
			current.setType(ResourcesConstants.RECEIVED_TYPE_STRIKE_OUT);
			current.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
			current.setReceived_time(DateUtil.getMinStr());
			current.setRelated_pk(related_pk);

			JSONObject obj = JSONObject.fromObject(out_array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("received");
			current.setTeam_number(t);

			if (!SimpletinyString.isEmpty(r)) {
				current.setReceived(new BigDecimal(r).negate());
			}
			current.setComment(detail.getComment());
			receivedService.insert(current);
			receivableService.updateReceivableReceived(current);
		}
		JSONArray in_array = JSONArray.fromObject(strike_in_json);

		for (int i = 0; i < in_array.size(); i++) {
			ClientReceivedDetailBean current = new ClientReceivedDetailBean();
			current.setType(ResourcesConstants.RECEIVED_TYPE_STRIKE_IN);
			current.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
			current.setReceived_time(DateUtil.getMinStr());
			current.setRelated_pk(related_pk);

			JSONObject obj = JSONObject.fromObject(in_array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("received");
			current.setTeam_number(t);

			if (!SimpletinyString.isEmpty(r)) {
				current.setReceived(new BigDecimal(r));
			}

			current.setComment(detail.getComment());

			receivedService.insert(current);
			receivableService.updateReceivableReceived(current);
		}

		resultStr = SUCCESS;
		return SUCCESS;
	}

	// 返佣申请
	public String applyFly() {
		detail.setType(ResourcesConstants.RECEIVED_TYPE_FLY);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);

		String related_pk = DBCommonUtil.genPk();
		detail.setRelated_pk(related_pk);

		detail.setReceived_time(DateUtil.getMinStr());
		detail.setReceived(detail.getReceived().negate());

		receivedService.insert(detail);
		// receivableService.updateReceivableReceived(detail);

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		// 生成支付审批数据
		PayApprovalBean pa = new PayApprovalBean();
		pa.setReceiver(detail.getReceiver());
		pa.setMoney(detail.getReceived().negate());
		pa.setItem(ResourcesConstants.PAY_TYPE_FLY);
		pa.setStatus(ResourcesConstants.PAID_STATUS_ING);
		pa.setReceiver_card_number(detail.getReceiver_card_number());
		pa.setReceiver_bank(detail.getReceiver_bank());

		pa.setRelated_pk(related_pk);

		pa.setComment(detail.getTeam_number() + "订单fly");
		pa.setApply_user(sessionBean.getUser_number());
		pa.setBack_pk(related_pk);
		pa.setApply_time(DateUtil.getTimeMillis());
		pa.setLimit_time(detail.getLimit_time());

		payApprovalService.insert(pa);

		resultStr = SUCCESS;
		return SUCCESS;
	}

	private List<ClientReceivedDetailBean> receiveds;

	public String searchReceivedByPage() {

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			detail.setCreate_user(sessionBean.getUser_number());
		}
		params.put("bo", detail);

		page.setParams(params);

		receiveds = receivedService.getAllReceivedsByPage(page);
		return SUCCESS;
	}

	private String received_pks;

	public String rollBackReceived() {
		resultStr = receivedService.rollBackReceived(received_pks);
		return SUCCESS;
	}

	private String related_pks;

	public String searchByRelatedPks() {
		receiveds = receivedService.selectByRelatedPks(related_pks);

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

}
