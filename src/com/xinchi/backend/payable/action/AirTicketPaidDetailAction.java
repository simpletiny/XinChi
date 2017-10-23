package com.xinchi.backend.payable.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.payable.service.AirTicketPaidDetailService;
import com.xinchi.bean.AirTicketPaidDto;
import com.xinchi.bean.ReimbursementDto;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AirTicketPaidDetailAction extends BaseAction {
	private static final long serialVersionUID = 5173969958649923067L;

	@Autowired
	private AirTicketPaidDetailService service;

	private AirTicketPaidDto option;
	private List<AirTicketPaidDto> paids;

	public String searchAirTicketPaidDetailByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);
		paids = service.selectByPage(page);
		return SUCCESS;
	}

	public AirTicketPaidDto getOption() {
		return option;
	}

	public void setOption(AirTicketPaidDto option) {
		this.option = option;
	}

	public List<AirTicketPaidDto> getPaids() {
		return paids;
	}

	public void setPaids(List<AirTicketPaidDto> paids) {
		this.paids = paids;
	}

}