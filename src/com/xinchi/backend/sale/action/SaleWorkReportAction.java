package com.xinchi.backend.sale.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.sale.service.SaleWorkReportService;
import com.xinchi.bean.SaleWorkReportDto;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SaleWorkReportAction extends BaseAction {
	private static final long serialVersionUID = 4347949176585567872L;

	@Autowired
	private SaleWorkReportService service;

	private List<SaleWorkReportDto> reports;

	private SaleWorkReportDto option;

	public String searchSwrByPage() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);
		reports = service.selectSwrByPage(page);
		return SUCCESS;
	}

	public List<SaleWorkReportDto> getReports() {
		return reports;
	}

	public void setReports(List<SaleWorkReportDto> reports) {
		this.reports = reports;
	}

	public SaleWorkReportDto getOption() {
		return option;
	}

	public void setOption(SaleWorkReportDto option) {
		this.option = option;
	}

}
