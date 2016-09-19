package com.xinchi.backend.client.action;

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

	public ClientBean getClient() {
		return client;
	}

	public void setClient(ClientBean client) {
		this.client = client;
	}

}