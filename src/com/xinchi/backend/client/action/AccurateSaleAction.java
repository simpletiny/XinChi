package com.xinchi.backend.client.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.AccurateSaleService;
import com.xinchi.bean.AccurateSaleBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccurateSaleAction extends BaseAction {
	private static final long serialVersionUID = -2722322585571376887L;

	@Autowired
	private AccurateSaleService service;

	private AccurateSaleBean accurate;
	private List<AccurateSaleBean> accurates;

	public String createAccurateSale() {
		service.insert(accurate);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	public String searchAccurateSaleByPage() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", accurate);
		page.setParams(params);
		accurates = service.selectByPage(page);
		return SUCCESS;
	}

	public AccurateSaleBean getAccurate() {
		return accurate;
	}

	public void setAccurate(AccurateSaleBean accurate) {
		this.accurate = accurate;
	}

	public List<AccurateSaleBean> getAccurates() {
		return accurates;
	}

	public void setAccurates(List<AccurateSaleBean> accurates) {
		this.accurates = accurates;
	}
}