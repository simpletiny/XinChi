package com.xinchi.backend.accounting.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		reimbursement.setApply_user(sessionBean.getUser_number());
		resultStr = service.save(reimbursement);
		return SUCCESS;
	}

	private List<String> reimbursement_pks;

	public String deleteReibursement() {

		resultStr = service.deleteReibursement(reimbursement_pks);

		return SUCCESS;
	}

	private String reimbursement_pk;

	public String searchReimbursementByPk() {
		reimbursement = service.selectByPk(reimbursement_pk);
		return SUCCESS;
	}

	public String reApplyReimbursement() {
		resultStr = service.reApply(reimbursement);
		return SUCCESS;
	}

	private List<ReimbursementBean> reimbursements;

	/**
	 * 搜索费用填报
	 * 
	 * @return
	 */
	public String searchReimbursementByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		Map<String, Object> params = new HashMap<String, Object>();

		if (null == reimbursement) {
			reimbursement = new ReimbursementBean();
		}

		if (!sessionBean.getUser_roles().contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			reimbursement.setApply_user(sessionBean.getUser_number());
		}

		params.put("bo", reimbursement);
		page.setParams(params);

		reimbursements = service.selectByPage(page);
		return SUCCESS;
	}

	public ReimbursementBean getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(ReimbursementBean reimbursement) {
		this.reimbursement = reimbursement;
	}

	public List<ReimbursementBean> getReimbursements() {
		return reimbursements;
	}

	public void setReimbursements(List<ReimbursementBean> reimbursements) {
		this.reimbursements = reimbursements;
	}

	public List<String> getReimbursement_pks() {
		return reimbursement_pks;
	}

	public void setReimbursement_pks(List<String> reimbursement_pks) {
		this.reimbursement_pks = reimbursement_pks;
	}

	public String getReimbursement_pk() {
		return reimbursement_pk;
	}

	public void setReimbursement_pk(String reimbursement_pk) {
		this.reimbursement_pk = reimbursement_pk;
	}
}
