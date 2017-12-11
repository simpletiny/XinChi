package com.xinchi.backend.accounting.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.AccountingService;
import com.xinchi.backend.accounting.service.PayApprovalService;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyUser;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountingAction extends BaseAction {
	private static final long serialVersionUID = 6895271659138883363L;

	@Autowired
	private AccountingService service;

	private String item;
	private String related_pk;
	private String pk;
	@Autowired
	private PayApprovalService payApprovalService;

	public String agreePayApply() {
		SimpletinyUser su = new SimpletinyUser();
		PayApprovalBean pa = payApprovalService.selectByPrimaryKey(pk);
		pa.setApproval_user(su.getUser().getUser_number());
		pa.setApproval_time(DateUtil.getMinStr());
		pa.setStatus(ResourcesConstants.PAID_STATUS_YES);
		payApprovalService.update(pa);

		if (item.equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			resultStr = service.updateRelatedPaid(related_pk, ResourcesConstants.PAID_STATUS_YES);
		} else if (item.equals(ResourcesConstants.PAY_TYPE_MORE_BACK)) {
			resultStr = service.agreeMoreBack(pa.getBack_pk());
		} else if (item.equals(ResourcesConstants.PAY_TYPE_PIAOWU)) {
			resultStr = service.agreeAirTicketPayApply(pa.getBack_pk());
		} else {
			resultStr = service.agreePayApply(pa.getBack_pk());
		}
		return SUCCESS;
	}

	public String rejectPayApply() {
		SimpletinyUser su = new SimpletinyUser();
		PayApprovalBean pa = payApprovalService.selectByPrimaryKey(pk);
		pa.setApproval_user(su.getUser().getUser_number());
		pa.setApproval_time(DateUtil.getMinStr());
		pa.setStatus(ResourcesConstants.PAID_STATUS_NO);
		payApprovalService.update(pa);

		if (item.equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			resultStr = service.updateRelatedPaid(related_pk, ResourcesConstants.PAID_STATUS_NO);
		} else if (item.equals(ResourcesConstants.PAY_TYPE_MORE_BACK)) {
			resultStr = service.rejectMoreBack(pa.getBack_pk());
		} else if (item.equals(ResourcesConstants.PAY_TYPE_PIAOWU)) {
			resultStr = service.rejectAirTicketPayApply(related_pk);
		} else {
			resultStr = service.rejectPayApply(pa.getBack_pk());
		}
		return SUCCESS;
	}

	public String rollBackPayApply() {
		if (item.equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			resultStr = service.rollBackRelatedPayApply(related_pk);
		}
		if (item.equals(ResourcesConstants.PAY_TYPE_PIAOWU)) {
			resultStr = service.rollBackAirTicketPayApply(related_pk);
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
