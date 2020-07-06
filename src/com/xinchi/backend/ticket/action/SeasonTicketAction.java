package com.xinchi.backend.ticket.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.ticket.service.SeasonTicketService;
import com.xinchi.bean.SeasonTicketBaseBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SeasonTicketAction extends BaseAction {
	private static final long serialVersionUID = -2862878910487093106L;

	private SeasonTicketBaseBean base;
	private String json;

	@Autowired
	private SeasonTicketService service;

	public String createSeasonTicket() {
		resultStr = service.createSeasonTicket(json, base);
		return SUCCESS;
	}

	private List<SeasonTicketBaseBean> bases;

	public String searchSeasonTicketByPage() {
		Map<String, SeasonTicketBaseBean> params = new HashMap<String, SeasonTicketBaseBean>();
		if (null == base)
			base = new SeasonTicketBaseBean();
		params.put("bo", base);

		page.setParams(params);
		bases = service.selectByPage(page);
		return SUCCESS;

	}

	public SeasonTicketBaseBean getBase() {
		return base;
	}

	public String getJson() {
		return json;
	}

	public void setBase(SeasonTicketBaseBean base) {
		this.base = base;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public List<SeasonTicketBaseBean> getBases() {
		return bases;
	}

	public void setBases(List<SeasonTicketBaseBean> bases) {
		this.bases = bases;
	}
}