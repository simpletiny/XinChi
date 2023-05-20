package com.xinchi.backend.receivable.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.receivable.service.AirReceivedService;
import com.xinchi.bean.AirReceivedDetailBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyUser;
import com.xinchi.common.UserSessionBean;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AirReceivedAction extends BaseAction {
	private static final long serialVersionUID = 604350660028611523L;
	@Autowired
	private AirReceivedService service;

	private List<AirReceivedDetailBean> details;
	private String detail_pk;
	private String related_pk;
	private AirReceivedDetailBean detail;

	public String searchAirDetailsByPage() {
		UserSessionBean user = SimpletinyUser.user();
		String roles = user.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)
				&& !roles.contains(ResourcesConstants.USER_ROLE_CASHIER)) {
			detail.setCreate_user(user.getUser_number());
		}
		params.put("bo", detail);

		page.setParams(params);
		details = service.selectByPage(page);
		return SUCCESS;
	}

	public List<AirReceivedDetailBean> getDetails() {
		return details;
	}

	public String getDetail_pk() {
		return detail_pk;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public AirReceivedDetailBean getDetail() {
		return detail;
	}

	public void setDetails(List<AirReceivedDetailBean> details) {
		this.details = details;
	}

	public void setDetail_pk(String detail_pk) {
		this.detail_pk = detail_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public void setDetail(AirReceivedDetailBean detail) {
		this.detail = detail;
	}
}
