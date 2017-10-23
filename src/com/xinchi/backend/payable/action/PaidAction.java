package com.xinchi.backend.payable.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.base.Joiner;
import com.xinchi.backend.accounting.service.PayApprovalService;
import com.xinchi.backend.payable.service.PaidService;
import com.xinchi.backend.payable.service.PayableService;
import com.xinchi.backend.supplier.service.SupplierEmployeeService;
import com.xinchi.backend.supplier.service.SupplierService;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.bean.SupplierBean;
import com.xinchi.bean.SupplierEmployeeBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.SimpletinyUser;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaidAction extends BaseAction {
	private static final long serialVersionUID = 242023683208406623L;
	private SupplierPaidDetailBean detail;

	private String allot_json;

	@Autowired
	private PayableService payableService;

	@Autowired
	private PaidService paidService;

	// 返款收入
	public String applyBackRecive() {
		detail.setType(ResourcesConstants.PAID_TYPE_BACK);
		detail.setStatus(ResourcesConstants.PAID_STATUS_ING);

		JSONArray array = JSONArray.fromObject(allot_json);

		String[] pks = DBCommonUtil.genPks(array.size());
		detail.setRelated_pk(Joiner.on(",").join(pks));

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("received");
			String m = obj.getString("supplier_employee_name");
			String p = obj.getString("supplier_employee_pk");
			detail.setTeam_number(t);
			detail.setSupplier_employee_name(m);
			detail.setSupplier_employee_pk(p);
			detail.setPk(pks[i]);

			if (!SimpletinyString.isEmpty(r)) {
				detail.setMoney((new BigDecimal(r)).multiply(new BigDecimal(-1)));
			}

			paidService.insertWithPk(detail);
			payableService.updatePayablePaid(detail);
		}

		resultStr = OK;
		return SUCCESS;
	}

	@Autowired
	private PayApprovalService payApprovalService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private SupplierEmployeeService supplierEmployeeService;

	public String applyPay() {
		detail.setType(ResourcesConstants.PAID_TYPE_PAID);
		detail.setStatus(ResourcesConstants.PAID_STATUS_ING);

		JSONArray array = JSONArray.fromObject(allot_json);

		String related_pk = DBCommonUtil.genPk();
		detail.setRelated_pk(related_pk);
		String supplier_employee_pk = "";
		BigDecimal sum_money = BigDecimal.ZERO;
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("paid");
			String m = obj.getString("supplier_employee_name");
			String p = obj.getString("supplier_employee_pk");

			if (supplier_employee_pk.equals(""))
				supplier_employee_pk = p;

			detail.setTeam_number(t);
			detail.setSupplier_employee_name(m);
			detail.setSupplier_employee_pk(p);

			if (!SimpletinyString.isEmpty(r)) {
				detail.setMoney(new BigDecimal(r));
				sum_money = sum_money.add(new BigDecimal(r));
			}

			paidService.insert(detail);
			payableService.updatePayablePaid(detail);
		}

		// 生成支出审批数据
		PayApprovalBean pa = new PayApprovalBean();
		SupplierEmployeeBean supplierEmployee = supplierEmployeeService.selectByPrimaryKey(supplier_employee_pk);
		SupplierBean supplier = supplierService.selectByPrimaryKey(supplierEmployee.getFinancial_body_pk());
		pa.setReceiver(supplier.getSupplier_short_name());
		pa.setMoney(sum_money);
		pa.setItem(ResourcesConstants.PAY_TYPE_DIJIE);
		pa.setStatus(ResourcesConstants.PAID_STATUS_ING);
		pa.setRelated_pk(related_pk);
		pa.setApply_user(SimpletinyUser.getUser_number());
		pa.setApply_time(DateUtil.getTimeMillis());
		pa.setLimit_time(detail.getLimit_time());
		pa.setBack_pk(related_pk);
		pa.setComment(detail.getComment());
		payApprovalService.insert(pa);

		resultStr = OK;
		return SUCCESS;
	}

	// 扣款申请
	public String applyDeduct() {
		detail.setType(ResourcesConstants.PAID_TYPE_DEDUCT);
		detail.setStatus(ResourcesConstants.PAID_STATUS_ING);

		JSONArray array = JSONArray.fromObject(allot_json);

		String[] pks = DBCommonUtil.genPks(array.size());
		detail.setRelated_pk(Joiner.on(",").join(pks));

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("deduct");
			String m = obj.getString("supplier_employee_name");
			String p = obj.getString("supplier_employee_pk");
			detail.setTeam_number(t);
			detail.setSupplier_employee_name(m);
			detail.setSupplier_employee_pk(p);
			detail.setPk(pks[i]);

			if (!SimpletinyString.isEmpty(r)) {
				detail.setMoney((new BigDecimal(r)).multiply(new BigDecimal(-1)));
			}

			paidService.insertWithPk(detail);
			payableService.updatePayablePaid(detail);
		}

		resultStr = OK;
		return SUCCESS;
	}

	// 冲账申请
	public String applyStrike() {
		detail.setType(ResourcesConstants.PAID_TYPE_STRIKE_OUT);
		detail.setStatus(ResourcesConstants.PAID_STATUS_ING);

		BigDecimal out_money = BigDecimal.ZERO;

		JSONArray array = JSONArray.fromObject(allot_json);

		String related_pk = DBCommonUtil.genPk();
		detail.setRelated_pk(related_pk);

		for (int i = 0; i < array.size(); i++) {
			SupplierPaidDetailBean current = new SupplierPaidDetailBean();

			current.setType(ResourcesConstants.PAID_TYPE_STRIKE_IN);
			current.setStatus(ResourcesConstants.PAID_STATUS_ING);
			current.setRelated_pk(related_pk);

			JSONObject obj = JSONObject.fromObject(array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("paid");
			String supplier_employee_pk = obj.getString("supplier_employee_pk");
			current.setTeam_number(t);
			current.setSupplier_employee_pk(supplier_employee_pk);

			if (!SimpletinyString.isEmpty(r)) {
				current.setMoney(new BigDecimal(r));
				out_money = out_money.add(new BigDecimal(r));
			}

			paidService.insert(current);
			payableService.updatePayablePaid(current);
		}

		detail.setMoney(out_money.negate());
		paidService.insert(detail);
		payableService.updatePayablePaid(detail);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	private List<SupplierPaidDetailBean> paids;

	public String searchPaidByPage() {

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN) && SimpletinyString.isEmpty(detail.getCreate_user())) {
			detail.setCreate_user(sessionBean.getUser_number());
		}

		params.put("bo", detail);

		page.setParams(params);

		paids = paidService.getAllPaidsByPage(page);

		return SUCCESS;
	}

	private String related_pk;

	public String rollBackPayApply() {
		resultStr = paidService.rollBackPayApply(related_pk);
		return SUCCESS;
	}

	public SupplierPaidDetailBean getDetail() {
		return detail;
	}

	public void setDetail(SupplierPaidDetailBean detail) {
		this.detail = detail;
	}

	public String getAllot_json() {
		return allot_json;
	}

	public void setAllot_json(String allot_json) {
		this.allot_json = allot_json;
	}

	public List<SupplierPaidDetailBean> getPaids() {
		return paids;
	}

	public void setPaids(List<SupplierPaidDetailBean> paids) {
		this.paids = paids;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}
}
