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
import com.xinchi.backend.ticket.service.AirTicketNameListService;
import com.xinchi.backend.ticket.service.PassengerTicketInfoService;
import com.xinchi.bean.AirOtherPaymentDto;
import com.xinchi.bean.AirServiceFeeDto;
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
	private String related_pk;

	/**
	 * 搜索机票应付款涉及到的名单
	 * 
	 * @return
	 */
	public String searchPayablePassengersByPayablePk() {
		passengers = service.searchPayablePassengersByPayablePk(payable_pk);
		return SUCCESS;
	}

	public String searchPayablePassengersByRelatedPk() {
		List<AirTicketPayableBean> payables = service.selectByRelatedPk(related_pk);
		for (AirTicketPayableBean p : payables) {
			if (p.getPayable_type().equals("A")) {
				passengers = service.searchPayablePassengersByPayablePk(p.getPk());
				break;
			} else if (p.getPayable_type().equals("C")) {
				passengers = airTicketNameListService.selectByChangePk(p.getRelated_pk());
				break;
			}
		}
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

	@Autowired
	private AirTicketNameListService airTicketNameListService;

	@Autowired
	private PassengerTicketInfoService passengerTicketInfoService;

	public String searchTicketInfoByRelatedPk() {
		List<AirTicketPayableBean> payables = service.selectByRelatedPk(related_pk);
		for (AirTicketPayableBean p : payables) {
			if (p.getPayable_type().equals("A")) {
				ptInfos = service.searchTicketInfoByPayablePk(p.getPk());
				break;
			} else if (p.getPayable_type().equals("C")) {
				List<AirTicketNameListBean> names = airTicketNameListService.selectByChangePk(p.getRelated_pk());
				if (null != names && names.size() > 0) {
					ptInfos = passengerTicketInfoService.selectByPassengerPk(names.get(0).getPk());
					break;
				}
			}
		}
		return SUCCESS;
	}

	private List<AirServiceFeeDto> service_fees;
	private List<AirOtherPaymentDto> deposit_deducts;
	private List<AirOtherPaymentDto> none_business_payments;

	private AirServiceFeeDto summary_option;

	/**
	 * 搜索票务财务汇总数据
	 * 
	 * @return
	 */
	public String searchAirPayableSummary() {
		service_fees = service.searchServiceFees(summary_option);
		deposit_deducts = service.searchDepositDeducts(summary_option);
		none_business_payments = service.searchNoneBussinessPayment(summary_option);

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

	public String getRelated_pk() {
		return related_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public List<AirServiceFeeDto> getService_fees() {
		return service_fees;
	}

	public void setService_fees(List<AirServiceFeeDto> service_fees) {
		this.service_fees = service_fees;
	}

	public AirServiceFeeDto getSummary_option() {
		return summary_option;
	}

	public void setSummary_option(AirServiceFeeDto summary_option) {
		this.summary_option = summary_option;
	}

	public List<AirOtherPaymentDto> getDeposit_deducts() {
		return deposit_deducts;
	}

	public void setDeposit_deducts(List<AirOtherPaymentDto> deposit_deducts) {
		this.deposit_deducts = deposit_deducts;
	}

	public List<AirOtherPaymentDto> getNone_business_payments() {
		return none_business_payments;
	}

	public void setNone_business_payments(List<AirOtherPaymentDto> none_business_payments) {
		this.none_business_payments = none_business_payments;
	}
}