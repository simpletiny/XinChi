package com.xinchi.backend.payable.action;

import static com.xinchi.common.SimpletinyString.isEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.payable.service.PayableService;
import com.xinchi.backend.supplier.service.SupplierEmployeeService;
import com.xinchi.backend.supplier.service.SupplierService;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.PayableSummaryBean;
import com.xinchi.bean.SupplierBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayableAction extends BaseAction {
	private static final long serialVersionUID = 2614822711242785923L;

	private PayableSummaryBean summary;

	@Autowired
	private PayableService payableService;

	private String sales_name;

	public String searchPayableSummary() {

		String user_number = "";

		if (!isEmpty(sales_name)) {
			user_number = String.valueOf(ResourcesConstants.MAP_USER_NAME.get(sales_name));
		}

		summary = payableService.searchPayableSummary(user_number);

		if (null == summary)
			summary = new PayableSummaryBean();

		return SUCCESS;
	}

	private PayableBean payable;
	private List<PayableBean> payables;

	public String searchPayableByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			payable.setSales(sessionBean.getUser_number());
		}
		String team_status = payable.getTeam_status();
		String departure_from = "";
		String departure_to = "";
		String return_to = "";
		if (!SimpletinyString.isEmpty(team_status)) {
			if (team_status.equals(ResourcesConstants.TEAM_STATUS_BEFORE)) {
				departure_from = DateUtil.getDateStr(DateUtil.YYYY_MM_DD);
			} else if (team_status.equals(ResourcesConstants.TEAM_STATUS_AFTER)) {
				departure_to = DateUtil.getDateStr(DateUtil.YYYY_MM_DD);
			} else if (team_status.equals(ResourcesConstants.TEAM_STATUS_RETURN)) {
				return_to = DateUtil.getDateStr(DateUtil.YYYY_MM_DD);
			}
		}
		payable.setDeparture_from(departure_from);
		payable.setDeparture_to(departure_to);
		payable.setReturn_to(return_to);
		if (payable.getSort_type().equals("倒序")) {
			payable.setSort_type("D");
		} else if (payable.getSort_type().equals("正序")) {
			payable.setSort_type("Z");
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", payable);
		page.setParams(params);

		payables = payableService.searchPayableByPage(page);
		return SUCCESS;
	}

	private String supplier_employee_pks;
	@Autowired
	private SupplierEmployeeService employeeService;
	@Autowired
	private SupplierService supplierService;
	private SupplierBean supplier;

	private String isSame;

	public String isSameFinancialBody2() {
		String employee_pks[] = supplier_employee_pks.split(",");
		List<String> body_pks = employeeService.getBodyPksByEmployeePks(employee_pks);
		if (body_pks.size() > 1) {
			isSame = "NOT";
		} else {
			isSame = OK;
			supplier = supplierService.selectByPrimaryKey(body_pks.get(0));
		}
		return SUCCESS;
	}

	public String getSales_name() {
		return sales_name;
	}

	public void setSales_name(String sales_name) {
		this.sales_name = sales_name;
	}

	public PayableSummaryBean getSummary() {
		return summary;
	}

	public void setSummary(PayableSummaryBean summary) {
		this.summary = summary;
	}

	public PayableBean getPayable() {
		return payable;
	}

	public void setPayable(PayableBean payable) {
		this.payable = payable;
	}

	public List<PayableBean> getPayables() {
		return payables;
	}

	public void setPayables(List<PayableBean> payables) {
		this.payables = payables;
	}

	public String getSupplier_employee_pks() {
		return supplier_employee_pks;
	}

	public void setSupplier_employee_pks(String supplier_employee_pks) {
		this.supplier_employee_pks = supplier_employee_pks;
	}

	public SupplierBean getSupplier() {
		return supplier;
	}

	public void setSupplier(SupplierBean supplier) {
		this.supplier = supplier;
	}

	public String getIsSame() {
		return isSame;
	}

	public void setIsSame(String isSame) {
		this.isSame = isSame;
	}
}
