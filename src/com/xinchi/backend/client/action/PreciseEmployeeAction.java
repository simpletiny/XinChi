package com.xinchi.backend.client.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.PreciseEmployeeService;
import com.xinchi.bean.PreciseClientEmployeeBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PreciseEmployeeAction extends BaseAction {
	private static final long serialVersionUID = -4249121882533323462L;
	private PreciseClientEmployeeBean employee;
	@Autowired
	private PreciseEmployeeService service;

	public String createPreciseEmployee() {
		resultStr = service.createPreciseEmployee(employee);
		return SUCCESS;
	}
	private List<PreciseClientEmployeeBean> employees;

	public String searchPreciseEmployeeByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", employee);
		page.setParams(params);
		employees = service.selectByPage(page);
		return SUCCESS;
	}
	
	private String employee_pk;
	public String searchPreciseEmployeeByPk() {
		employee = service.selectByPrimaryKey(employee_pk);
		return SUCCESS;
	}
	
	public String updatePreciseEmployee() {
		resultStr = service.updatePreciseEmployee(employee);
		return SUCCESS;
	}
	
	public String updatePreciseEmployeeSimp() {
		resultStr = service.update(employee);
		return SUCCESS;
	}
	
	private String json;
	public String bindingPreciseEmployees() {
		resultStr = service.bindingPreciseEmployees(json);
		return SUCCESS;
	}
	public String deletePreciseEmployee() {
		resultStr = service.deletePreciseEmployee(employee_pk);
		return SUCCESS;
	}
	public PreciseClientEmployeeBean getEmployee() {
		return employee;
	}

	public void setEmployee(PreciseClientEmployeeBean employee) {
		this.employee = employee;
	}
	public List<PreciseClientEmployeeBean> getEmployees() {
		return employees;
	}
	public void setEmployees(List<PreciseClientEmployeeBean> employees) {
		this.employees = employees;
	}
	public String getEmployee_pk() {
		return employee_pk;
	}
	public void setEmployee_pk(String employee_pk) {
		this.employee_pk = employee_pk;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}