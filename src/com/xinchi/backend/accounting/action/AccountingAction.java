package com.xinchi.backend.accounting.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.AccountingService;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountingAction extends BaseAction {
	private static final long serialVersionUID = 6895271659138883363L;

	@Autowired
	private AccountingService service;

	private String item;
	private String related_pk;
	private String pk;

	public String agreePayApply() {
		if (item.equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			resultStr = service.updateRelatedPaid(related_pk, ResourcesConstants.PAID_STATUS_YES);
		} else {
			resultStr = service.agreePayApply(pk);
		}
		return SUCCESS;
	}

	public String rejectPayApply() {
		if (item.equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			resultStr = service.updateRelatedPaid(related_pk, ResourcesConstants.PAID_STATUS_NO);
		} else {
			resultStr = service.rejectPayApply(pk);
		}
		return SUCCESS;
	}
	
	public String rollBackPayApply(){
		if (item.equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			resultStr = service.rollBackRelatedPayApply(related_pk);
		} else {
			resultStr = service.rollBackPayApply(pk);
		}
		
		return SUCCESS;
	}
	public String getRelated_pk() {
		return related_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
}
