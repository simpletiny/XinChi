package com.xinchi.backend.client.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.ClientRelationService;
import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.ClientSummaryDto;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope("prototype")
public class ClientRelationAction extends BaseAction {
	private static final long serialVersionUID = -5702112495549468432L;

	private ClientVisitBean visit;

	private ClientRelationSummaryBean relation;
	private List<ClientRelationSummaryBean> relations;
	@Autowired
	private ClientRelationService service;

	public String createVisit() {
		resultStr = service.createVisit(visit);
		return SUCCESS;
	}

	public String searchRelationsByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			relation = new ClientRelationSummaryBean();
			relation.setSales(sessionBean.getPk());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", relation);
		page.setParams(params);
		relations = service.getRelationsByPage(page);
		return SUCCESS;
	}

	private ClientSummaryDto clientSummary;

	public String searchClientSummary() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			relation = new ClientRelationSummaryBean();
			relation.setSales_name(sessionBean.getUser_name());
		}

		clientSummary = service.getClientSummary(relation);

		return SUCCESS;
	}

	public ClientVisitBean getVisit() {
		return visit;
	}

	public void setVisit(ClientVisitBean visit) {
		this.visit = visit;
	}

	public ClientRelationSummaryBean getRelation() {
		return relation;
	}

	public void setRelation(ClientRelationSummaryBean relation) {
		this.relation = relation;
	}

	public List<ClientRelationSummaryBean> getRelations() {
		return relations;
	}

	public void setRelations(List<ClientRelationSummaryBean> relations) {
		this.relations = relations;
	}

	public ClientSummaryDto getClientSummary() {
		return clientSummary;
	}

	public void setClientSummary(ClientSummaryDto clientSummary) {
		this.clientSummary = clientSummary;
	}
}