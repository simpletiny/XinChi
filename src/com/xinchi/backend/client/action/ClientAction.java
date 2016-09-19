package com.xinchi.backend.client.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.ClientService;
import com.xinchi.bean.ClientBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope("prototype")
public class ClientAction extends BaseAction {
	private static final long serialVersionUID = -5405111196840939221L;
	private ClientBean client;
	@Autowired
	private ClientService clientService;

	public String createCompany() {
		resultStr = clientService.createCompany(client);
		return SUCCESS;
	}
	
	public String updateCompany() {
		resultStr = clientService.updateCompany(client);
		return SUCCESS;
	}

	private List<ClientBean> clients;

	public String searchCompany() {
		clients = clientService.getAllCompaniesByParam(null);
		return SUCCESS;
	}

	private String client_pk;

	public String searchCompanyByPk() {
		client = clientService.selectByPrimaryKey(client_pk);
		return SUCCESS;
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


}