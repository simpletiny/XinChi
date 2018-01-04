package com.xinchi.backend.accounting.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.PayApprovalService;
import com.xinchi.backend.finance.service.CardService;
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
	
	@Autowired
	private CardService cardService;
	private BigDecimal sum_balance;
	private BigDecimal sum_card_balance;
	public String searchPaidApplyByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);

		payApprovals = service.selectByPage(page);
		sum_balance = service.selectSumBalance();
		sum_card_balance = cardService.selectSumBalance();
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

	public BigDecimal getSum_balance() {
		return sum_balance;
	}

	public void setSum_balance(BigDecimal sum_balance) {
		this.sum_balance = sum_balance;
	}

	public BigDecimal getSum_card_balance() {
		return sum_card_balance;
	}

	public void setSum_card_balance(BigDecimal sum_card_balance) {
		this.sum_card_balance = sum_card_balance;
	}
}