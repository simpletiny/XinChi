package com.xinchi.backend.accounting.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.PayApprovalService;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayApprovalAction extends BaseAction {
	private static final long serialVersionUID = -5126406040947508219L;

	@Autowired
	private PayApprovalService service;

	private PayApprovalBean option;
	private List<PayApprovalBean> payApprovals;

	public String searchPaidApplyByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);

		payApprovals = service.selectByPage(page);

		return SUCCESS;
	}

	public PayApprovalBean getOption() {
		return option;
	}

	public void setOption(PayApprovalBean option) {
		this.option = option;
	}

	public List<PayApprovalBean> getPayApprovals() {
		return payApprovals;
	}

	public void setPayApprovals(List<PayApprovalBean> payApprovals) {
		this.payApprovals = payApprovals;
	}
}