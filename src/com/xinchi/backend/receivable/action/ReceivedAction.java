package com.xinchi.backend.receivable.action;

import java.math.BigDecimal;

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

}
