package com.xinchi.backend.accounting.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.AccountingService;
import com.xinchi.backend.accounting.service.PayApprovalService;
import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.bean.ReceivedDetailDto;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.SimpletinyUser;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		PayApprovalBean pa = payApprovalService.selectByPrimaryKey(pk);
		pa.setApproval_user(sessionBean.getUser_number());
		pa.setApproval_time(DateUtil.getMinStr());
		pa.setStatus(ResourcesConstants.PAID_STATUS_YES);

		// 'D' : '地接款',

		// 'X' : '销售费用',
		// 'B' : '办公费用',
		// 'C' : '产品费用',
		// 'J' : '交通垫付',
		// 'G' : '工资费用',
		// 'Q' : '其他支出',

		// 'P' : '票务费用',

		// 'M' : '多付返款',
		// 'F' : 'FLY'
		// 地接款
		if (item.equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			resultStr = service.updateRelatedPaid(pa.getRelated_pk(), ResourcesConstants.PAID_STATUS_YES);
		}
		// 多退返款
		else if (item.equals(ResourcesConstants.PAY_TYPE_MORE_BACK)) {
			resultStr = service.agreeMoreBack(pa.getBack_pk());
		}
		// 票务费用，还有被一种是票务端产生的费用，目前没有，
		else if (item.equals(ResourcesConstants.PAY_TYPE_PIAOWU)) {
			resultStr = service.agreeAirTicketPayApply(pa.getBack_pk());
		}
		// 返佣支出
		else if (item.equals(ResourcesConstants.PAY_TYPE_FLY)) {
			resultStr = service.agreeFlyApply(pa.getBack_pk());
		} else {
			resultStr = service.agreePayApply(pa.getBack_pk());
		}

		if (resultStr.equals(SUCCESS))
			payApprovalService.update(pa);

		return SUCCESS;
	}

	private String reject_reason;

	public String rejectPayApply() {
		PayApprovalBean pa = payApprovalService.selectByPrimaryKey(pk);
		pa.setApproval_user(SimpletinyUser.user().getUser_number());
		pa.setApproval_time(DateUtil.getMinStr());
		pa.setStatus(ResourcesConstants.PAID_STATUS_NO);
		if (SimpletinyString.isEmpty(reject_reason))
			reject_reason = ResourcesConstants.PAY_REJECT_DEFAULT_REASON;
		pa.setReject_reason(reject_reason);

		if (item.equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			resultStr = service.updateRelatedPaid(pa.getRelated_pk(), ResourcesConstants.PAID_STATUS_NO);
		} else if (item.equals(ResourcesConstants.PAY_TYPE_MORE_BACK)) {
			resultStr = service.rejectMoreBack(pa.getBack_pk());
		} else if (item.equals(ResourcesConstants.PAY_TYPE_PIAOWU)) {
			resultStr = service.rejectAirTicketPayApply(pa.getRelated_pk());
		} else if (item.equals(ResourcesConstants.PAY_TYPE_FLY)) {
			resultStr = service.rejectFlyApply(pa.getBack_pk());
		} else {
			resultStr = service.rejectPayApply(pa.getBack_pk());
		}

		if (resultStr.equals(SUCCESS))
			payApprovalService.update(pa);
		return SUCCESS;
	}

	private String back_pk;

	public String searchRejectReason() {
		PayApprovalBean pa = payApprovalService.selectByBackPk(back_pk);
		resultStr = SimpletinyString.isEmpty(pa.getReject_reason()) ? "" : pa.getReject_reason();
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

	private ReceivedDetailDto detail;

	private List<ReceivedDetailDto> receiveds;

	/**
	 * 搜索待匹配收入
	 * 
	 * @return
	 */
	public String searchReceivedByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		if (null == detail)
			detail = new ReceivedDetailDto();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)
				&& !roles.contains(ResourcesConstants.USER_ROLE_CASHIER)) {
			detail.setCreate_user(sessionBean.getUser_number());
		}

		params.put("bo", detail);
		page.setParams(params);

		receiveds = service.searchAllReceivedsByPage(page);

		return SUCCESS;
	}

	@Autowired
	private ReceivedService receivedService;

	private ClientEmployeeBean client_employee;

	@Autowired
	private EmployeeService clientEmployeeService;

	/**
	 * 搜索多付返款客户信息
	 * 
	 * @return
	 */
	public String searchMoreBackClientEmployee() {
		List<ClientReceivedDetailBean> details = receivedService.selectByRelatedPks(related_pk);
		if (null != details && details.size() > 0) {
			client_employee = clientEmployeeService.selectByPrimaryKey(details.get(0).getClient_employee_pk());
		} else {
			client_employee = new ClientEmployeeBean();
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

	public List<ReceivedDetailDto> getReceiveds() {
		return receiveds;
	}

	public void setReceiveds(List<ReceivedDetailDto> receiveds) {
		this.receiveds = receiveds;
	}

	public ReceivedDetailDto getDetail() {
		return detail;
	}

	public void setDetail(ReceivedDetailDto detail) {
		this.detail = detail;
	}

	public String getReject_reason() {
		return reject_reason;
	}

	public void setReject_reason(String reject_reason) {
		this.reject_reason = reject_reason;
	}

	public String getBack_pk() {
		return back_pk;
	}

	public void setBack_pk(String back_pk) {
		this.back_pk = back_pk;
	}

	public ClientEmployeeBean getClient_employee() {
		return client_employee;
	}

	public void setClient_employee(ClientEmployeeBean client_employee) {
		this.client_employee = client_employee;
	}
}
