package com.xinchi.backend.receivable.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.base.Joiner;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

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
		detail.setType(ResourcesConstants.RECEIVED_TYPE_TAIL);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
		detail.setReceived_time(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
		receivedService.insert(detail);
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

		resultStr = OK;
		return SUCCESS;
	}

	/**
	 * 冲账申请
	 * 
	 * @return
	 */
	public String applyStrike() {
		detail.setType(ResourcesConstants.RECEIVED_TYPE_STRIKE);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);
		detail.setReceived_time(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));

		JSONArray array = JSONArray.fromObject(allot_json);

		String[] pks = DBCommonUtil.genPks(3);
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

		resultStr = OK;
		return SUCCESS;
	}

	// 收入申请
	public String applyReceive() {
		detail.setType(ResourcesConstants.RECEIVED_TYPE_RECEIVED);
		detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ING);

		receivedService.insert(detail);
		receivableService.updateReceivableReceived(detail);

		resultStr = OK;
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
		detail.setStatus(SimpletinyString.addSingleQuote(detail.getStatus()));
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

}
