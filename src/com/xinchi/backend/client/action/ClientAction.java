package com.xinchi.backend.client.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.ClientService;
import com.xinchi.bean.ClientBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope("prototype")
public class ClientAction extends BaseAction {
	private static final long serialVersionUID = -5405111196840939221L;
	private ClientBean client;
	@Autowired
	private ClientService clientService;

	public String createCompany() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		client.setSales(sessionBean.getPk());
		client.setSales_name(sessionBean.getUser_name());
		if(!SimpletinyString.isEmpty(client.getAgency_pk())){
			client.setRelate_flg("Y");
		}
		resultStr = clientService.createCompany(client);
		return SUCCESS;
	}

	public String updateCompany() {
		if(SimpletinyString.isEmpty(client.getAgency_pk())){
			client.setRelate_flg("N");
		}else{
			client.setRelate_flg("Y");
		}
		resultStr = clientService.updateCompany(client);
		return SUCCESS;
	}

	private List<ClientBean> clients;

	public String searchCompany() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		ClientBean cb = null;

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			cb = new ClientBean();
			cb.setCreate_user(sessionBean.getUser_number());
		}

		clients = clientService.getAllCompaniesByParam(cb);
		return SUCCESS;
	}

	private String company_status;
	private String relate_status;

	public String searchCompanyByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			client.setSales(sessionBean.getPk());
		}
		
		if (!SimpletinyString.isEmpty(relate_status)) {
			String[] statuses = relate_status.split(",");
			if (statuses.length == 1) {
				if (statuses[0].equals(ResourcesConstants.RELATE_STATUS_YES)) {
					client.setRelate_flg("Y");
				} else {
					client.setRelate_flg("N");
				}
			}
		}
		if (!SimpletinyString.isEmpty(company_status)) {
			String[] statuses = company_status.split(",");
			if (statuses.length == 1) {
				if (statuses[0].equals(ResourcesConstants.STOP_STATUS_NORMAL)) {
					client.setDelete_flg("N");
				} else {
					client.setDelete_flg("Y");
				}
			}
		}

		params.put("bo", client);

		page.setParams(params);

		clients = clientService.getAllCompaniesByPage(page);
		return SUCCESS;
	}

	private String client_pk;

	public String searchCompanyByPk() {
		client = clientService.selectByPrimaryKey(client_pk);
		return SUCCESS;
	}

	private List<String> company_pks;

	public String deleteCompany() {
		resultStr = clientService.deleteClientEmployee(company_pks);
		return resultStr;
	}

	public String recoveryCompany() {
		resultStr = clientService.recoveryClientEmployee(company_pks);
		return resultStr;
	}

	public ClientBean getClient() {
		return client;
	}

	public void setClient(ClientBean client) {
		this.client = client;
	}

	public List<ClientBean> getClients() {
		return clients;
	}

	public void setClients(List<ClientBean> clients) {
		this.clients = clients;
	}

	public String getClient_pk() {
		return client_pk;
	}

	public void setClient_pk(String client_pk) {
		this.client_pk = client_pk;
	}

	public List<String> getCompany_pks() {
		return company_pks;
	}

	public void setCompany_pks(List<String> company_pks) {
		this.company_pks = company_pks;
	}

	public String getCompany_status() {
		return company_status;
	}

	public void setCompany_status(String company_status) {
		this.company_status = company_status;
	}

	public String getRelate_status() {
		return relate_status;
	}

	public void setRelate_status(String relate_status) {
		this.relate_status = relate_status;
	}

}