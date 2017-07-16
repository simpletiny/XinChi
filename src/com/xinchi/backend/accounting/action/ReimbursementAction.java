package com.xinchi.backend.accounting.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.ReimbursementService;
import com.xinchi.bean.ReimbursementBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReimbursementAction extends BaseAction {
	private static final long serialVersionUID = -4036881630110559774L;
	@Autowired
	private ReimbursementService service;
	private ReimbursementBean reimbursement;

	public String saveReimbursement() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		reimbursement.setApply_user(sessionBean.getUser_number());
		resultStr = service.save(reimbursement);
		return SUCCESS;
	}

	public ReimbursementBean getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(ReimbursementBean reimbursement) {
		this.reimbursement = reimbursement;
	}
}
