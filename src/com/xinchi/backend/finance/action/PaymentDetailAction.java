package com.xinchi.backend.finance.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;

@Controller
@Scope("prototype")
public class PaymentDetailAction extends BaseAction {
	private static final long serialVersionUID = 3408762081387739186L;
	@Autowired
	private PaymentDetailService pds;
	private PaymentDetailBean detail;

	public String createDetail() {
		pds.insert(detail);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	List<PaymentDetailBean> details;

	public String searchDetail() {
		details = pds.getAllDetailsByParam(null);
		return SUCCESS;
	}

	public String searchDetailByPage() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", detail);
		page.setParams(params);
		
		details = pds.getAllDetailsByPage(page);
		return SUCCESS;
	}

	public PaymentDetailBean getDetail() {
		return detail;
	}

	public void setDetail(PaymentDetailBean detail) {
		this.detail = detail;
	}

	public List<PaymentDetailBean> getDetails() {
		return details;
	}

	public void setDetails(List<PaymentDetailBean> details) {
		this.details = details;
	}
}
