package com.xinchi.backend.accounting;

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

	private String type;
	private String related_pk;
	private String pk;

	public String agreePayApply() {
		if (type.equals(ResourcesConstants.PAID_TYPE_STRIKE)) {
			resultStr = service.updatePaid(pk, ResourcesConstants.PAID_STATUS_YES);
		} else {
			resultStr = service.updateRelatedPaid(related_pk, ResourcesConstants.PAID_STATUS_YES);
		}

		return SUCCESS;
	}

	public String rejectPayApply() {
		if (type.equals(ResourcesConstants.PAID_TYPE_STRIKE)) {
			resultStr = service.updatePaid(pk, ResourcesConstants.PAID_STATUS_NO);
		} else {
			resultStr = service.updateRelatedPaid(related_pk, ResourcesConstants.PAID_STATUS_NO);
		}
		return SUCCESS;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
}
