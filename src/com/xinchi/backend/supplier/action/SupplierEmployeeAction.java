package com.xinchi.backend.supplier.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.supplier.service.SupplierEmployeeService;
import com.xinchi.bean.SupplierEmployeeBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope("prototype")
public class SupplierEmployeeAction extends BaseAction {
	private static final long serialVersionUID = 599705102230406514L;
	private SupplierEmployeeBean employee;
	@Autowired
	private SupplierEmployeeService employeeService;

	public String createEmployee() {
		resultStr = employeeService.createEmployee(employee);
		return SUCCESS;
	}

	public String updateEmployee() {
		resultStr = employeeService.updateEmployee(employee);
		return SUCCESS;
	}

	private List<SupplierEmployeeBean> employees;

	public String searchEmployee() {
		employees = employeeService.getAllSupplierEmployeeByParam(employee);
		return SUCCESS;
	}

	private String employee_pk;

	public String searchOneEmployee() {
		employee = employeeService.selectByPrimaryKey(employee_pk);
		return SUCCESS;
	}

	public SupplierEmployeeBean getEmployee() {
		return employee;
	}

	public void setEmployee(SupplierEmployeeBean employee) {
		this.employee = employee;
	}

	public List<SupplierEmployeeBean> getEmployees() {
		return employees;
	}

	public void setEmployees(List<SupplierEmployeeBean> employees) {
		this.employees = employees;
	}

	public String getEmployee_pk() {
		return employee_pk;
	}

	public void setEmployee_pk(String employee_pk) {
		this.employee_pk = employee_pk;
	}

}