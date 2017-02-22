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
import com.xinchi.backend.payable.service.PaidService;
import com.xinchi.backend.payable.service.PayableService;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
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

	public String applyPay() {
		detail.setType(ResourcesConstants.PAID_TYPE_PAID);
		detail.setStatus(ResourcesConstants.PAID_STATUS_ING);
		
		JSONArray array = JSONArray.fromObject(allot_json);

		String[] pks = DBCommonUtil.genPks(array.size());
		detail.setRelated_pk(Joiner.on(",").join(pks));

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String t = obj.getString("team_number");
			String r = obj.getString("paid");
			String m = obj.getString("supplier_employee_name");
			String p = obj.getString("supplier_employee_pk");
			detail.setTeam_number(t);
			detail.setSupplier_employee_name(m);
			detail.setSupplier_employee_pk(p);
			detail.setPk(pks[i]);

			if (!SimpletinyString.isEmpty(r)) {
				detail.setMoney(new BigDecimal(r));
			}

			paidService.insertWithPk(detail);
			payableService.updatePayablePaid(detail);
		}

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
		detail.setType(ResourcesConstants.PAID_TYPE_STRIKE);
		detail.setStatus(ResourcesConstants.PAID_STATUS_ING);
//		detail.setTime(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
		paidService.insert(detail);
		payableService.updatePayablePaid(detail);
		resultStr = OK;
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
		
		detail.setStatus(SimpletinyString.addSingleQuote(detail.getStatus()));
		params.put("bo", detail);

		page.setParams(params);

		paids = paidService.getAllPaidsByPage(page);
		
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
}
