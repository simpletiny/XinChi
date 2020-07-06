package com.xinchi.backend.payable.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.payable.service.AirTicketPaidDetailService;
import com.xinchi.backend.payable.service.AirTicketPayableService;
import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.bean.PassengerTicketInfoBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AirTicketPayableAction extends BaseAction {
	private static final long serialVersionUID = -7838859864025573905L;

	@Autowired
	private AirTicketPayableService service;

	private List<AirTicketPayableBean> payables;

	private AirTicketPayableBean option;

	/**
	 * 搜索机票应付款
	 * 
	 * @return
	 */
	public String searchAirTicketPayableByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);

		payables = service.selectByPage(page);
		return SUCCESS;
	}

	private String payable_pks;

	public String searchAirTicketPayablesByPks() {
		List<String> pks = Arrays.asList(payable_pks.split(","));
		payables = service.selectByPks(pks);
		return SUCCESS;
	}

	private String paidJson;
	private String payableJson;
	private BigDecimal allot_money;

	// 支付机票款
	public String payAirTicket() {
		resultStr = service.payAirTicket(paidJson, payableJson, allot_money);
		return SUCCESS;
	}

	private AirTicketPaidDetailBean detail;
	private String json;

	@Autowired
	private AirTicketPaidDetailService airTicketPaidDetailService;

	public String applyAirTicketPay() {

		JSONArray array = JSONArray.fromObject(json);

		String related_pk = DBCommonUtil.genPk();
		BigDecimal sumPaid = BigDecimal.ZERO;
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String r = obj.getString("paid");
			String p = obj.getString("supplier_employee_pk");
			String base_pk = obj.getString("base_pk");
			String PNR = obj.getString("PNR");
			AirTicketPaidDetailBean currentDetail = new AirTicketPaidDetailBean();

			currentDetail.setAllot_money(detail.getAllot_money());
			currentDetail.setLimit_time(detail.getLimit_time());
			currentDetail.setSupplier_employee_pk(p);
			currentDetail.setBase_pk(base_pk);
			currentDetail.setPNR(PNR);
			currentDetail.setType(ResourcesConstants.PAID_TYPE_PAID);
			currentDetail.setStatus(ResourcesConstants.PAID_STATUS_ING);
			currentDetail.setRelated_pk(related_pk);

			if (!SimpletinyString.isEmpty(r)) {
				sumPaid = new BigDecimal(r);
			}

			currentDetail.setMoney(sumPaid);

			airTicketPaidDetailService.insert(currentDetail);

			AirTicketPayableBean airTicketPayable = service.selectByPrimaryKey(base_pk);
			airTicketPayable.setPaid(airTicketPayable.getPaid().add(sumPaid));
			airTicketPayable.setBudget_balance(airTicketPayable.getBudget_balance().subtract(sumPaid));
			service.update(airTicketPayable);
		}

		resultStr = SUCCESS;
		return SUCCESS;
	}

	private List<AirTicketNameListBean> passengers;

	private String payable_pk;

	/**
	 * 搜索机票应付款涉及到的名单
	 * 
	 * @return
	 */
	public String searchPayablePassengersByPayablePk() {
		passengers = service.searchPayablePassengersByPayablePk(payable_pk);
		return SUCCESS;
	}

	private List<PassengerTicketInfoBean> ptInfos;

	/**
	 * 搜索机票应付款涉及到的名单
	 * 
	 * @return
	 */
	public String searchTicketInfoByPayablePk() {
		ptInfos = service.searchTicketInfoByPayablePk(payable_pk);
		return SUCCESS;
	}

	public List<AirTicketPayableBean> getPayables() {
		return payables;
	}

	public void setPayables(List<AirTicketPayableBean> payables) {
		this.payables = payables;
	}

	public AirTicketPayableBean getOption() {
		return option;
	}

	public void setOption(AirTicketPayableBean option) {
		this.option = option;
	}

	public AirTicketPaidDetailBean getDetail() {
		return detail;
	}

	public void setDetail(AirTicketPaidDetailBean detail) {
		this.detail = detail;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getPayable_pks() {
		return payable_pks;
	}

	public void setPayable_pks(String payable_pks) {
		this.payable_pks = payable_pks;
	}

	public String getPaidJson() {
		return paidJson;
	}

	public void setPaidJson(String paidJson) {
		this.paidJson = paidJson;
	}

	public String getPayableJson() {
		return payableJson;
	}

	public void setPayableJson(String payableJson) {
		this.payableJson = payableJson;
	}

	public BigDecimal getAllot_money() {
		return allot_money;
	}

	public void setAllot_money(BigDecimal allot_money) {
		this.allot_money = allot_money;
	}

	public List<AirTicketNameListBean> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<AirTicketNameListBean> passengers) {
		this.passengers = passengers;
	}

	public String getPayable_pk() {
		return payable_pk;
	}

	public void setPayable_pk(String payable_pk) {
		this.payable_pk = payable_pk;
	}

	public List<PassengerTicketInfoBean> getPtInfos() {
		return ptInfos;
	}

	public void setPtInfos(List<PassengerTicketInfoBean> ptInfos) {
		this.ptInfos = ptInfos;
	}
}