package com.xinchi.backend.client.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.client.service.EmployeeService;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope("prototype")
public class EmployeeAction extends BaseAction {
	private static final long serialVersionUID = 599705102230406514L;
	private ClientEmployeeBean employee;
	@Autowired
	private EmployeeService employeeService;
	public String createEmployee() {
		resultStr = employeeService.createEmployee(employee);
		return SUCCESS;
	}
	
	public String updateEmployee() {
		resultStr = employeeService.updateEmployee(employee);
		return SUCCESS;
	}
	
	private List<ClientEmployeeBean> employees;
	public String searchEmployee(){
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		
		if(!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)){
			employee = new ClientEmployeeBean();
			employee.setSales(sessionBean.getPk());
		}
		
		employees = employeeService.getAllClientEmployeeByParam(employee);
		return SUCCESS;
	}
	
	private String employee_pk;
	public String searchOneEmployee(){
		employee = employeeService.selectByPrimaryKey(employee_pk);
		return SUCCESS;
	}
	public ClientEmployeeBean getEmployee() {
		return employee;
	}

	public void setEmployee(ClientEmployeeBean employee) {
		this.employee = employee;
	}
	public List<ClientEmployeeBean> getEmployees() {
		return employees;
	}
	public void setEmployees(List<ClientEmployeeBean> employees) {
		this.employees = employees;
	}
	public String getEmployee_pk() {
		return employee_pk;
	}
	public void setEmployee_pk(String employee_pk) {
		this.employee_pk = employee_pk;
	}
	


}