package com.xinchi.backend.accounting.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.AccPaidService;
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
	private AccPaidService accPaidService;
	@Autowired
	private CardService cardService;
	private BigDecimal sum_balance;
	private BigDecimal sum_card_balance;
	private BigDecimal sum_waiting_for_paid;
	private BigDecimal sum_suspense;

	public String searchPaidApplyByPage() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);

		payApprovals = service.selectByPage(page);
		sum_balance = service.selectSumBalance();
		sum_suspense = service.selectSumSuspense();

		sum_waiting_for_paid = accPaidService.selectSumWFP();
		String basePath = this.getClass().getClassLoader().getResource("").getPath();
		InputStreamReader config = new InputStreamReader(
				new FileInputStream(basePath + File.separator + "hot" + File.separator + "accountSumConfig.txt"),
				"UTF-8");
		BufferedReader br = new BufferedReader(config);
		String line;
		String r = "";
		while ((line = br.readLine()) != null) {
			r += line;
		}
		br.close();

		List<String> accounts = Arrays.asList(r.split(","));
		sum_card_balance = cardService.selectSumBalance(accounts);
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

	public BigDecimal getSum_waiting_for_paid() {
		return sum_waiting_for_paid;
	}

	public void setSum_waiting_for_paid(BigDecimal sum_waiting_for_paid) {
		this.sum_waiting_for_paid = sum_waiting_for_paid;
	}

	public BigDecimal getSum_suspense() {
		return sum_suspense;
	}

	public void setSum_suspense(BigDecimal sum_suspense) {
		this.sum_suspense = sum_suspense;
	}
}