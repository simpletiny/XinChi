package com.xinchi.backend.ticket.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.ticket.service.TicketNameTempletService;
import com.xinchi.bean.TicketNameTempletBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TikcetNameTempletAction extends BaseAction {
	private static final long serialVersionUID = -2894992909535803258L;

	@Autowired
	private TicketNameTempletService service;

	private List<TicketNameTempletBean> templets;
	private TicketNameTempletBean templet;

	public String searchTicketNameTempletByPage() {
		if (null == templet)
			templet = new TicketNameTempletBean();

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", templet);
		page.setParams(params);

		templets = service.selectByPage(page);
		return SUCCESS;
	}

	public String searchTicketNameTemplet() {
		templets = service.selectByParam(templet);
		return SUCCESS;
	}

	public String createTicketNameTemplet() {
		resultStr = service.createTicketNameTemplet(templet);
		return SUCCESS;
	}

	private String templet_pk;

	public String deleteTicketNameTemplet() {
		resultStr = service.deleteTicketNameTemplet(templet_pk);
		return SUCCESS;
	}

	public String updateTicketNameTemplet() {
		resultStr = service.updateTicketNameTemplet(templet);
		return SUCCESS;
	}

	public TicketNameTempletBean getTemplet() {
		return templet;
	}

	public void setTemplet(TicketNameTempletBean templet) {
		this.templet = templet;
	}

	public List<TicketNameTempletBean> getTemplets() {
		return templets;
	}

	public void setTemplets(List<TicketNameTempletBean> templets) {
		this.templets = templets;
	}

	public String getTemplet_pk() {
		return templet_pk;
	}

	public void setTemplet_pk(String templet_pk) {
		this.templet_pk = templet_pk;
	}

}