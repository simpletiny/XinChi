package com.xinchi.backend.receivable.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;

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

	public ClientReceivedDetailBean getDetail() {
		return detail;
	}

	public void setDetail(ClientReceivedDetailBean detail) {
		this.detail = detail;
	}

}
