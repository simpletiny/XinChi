package com.xinchi.backend.payable.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.finance.service.ReceivedMatchService;
import com.xinchi.backend.payable.service.AirTicketPaidDetailService;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPaidDto;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.ReceivedMatchBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AirTicketPaidDetailAction extends BaseAction {
	private static final long serialVersionUID = 5173969958649923067L;

	@Autowired
	private AirTicketPaidDetailService service;

	private AirTicketPaidDto option;
	private List<AirTicketPaidDto> paids;

	public String searchAirTicketPaidDetailByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", option);
		page.setParams(params);
		paids = service.selectByPage(page);
		return SUCCESS;
	}

	private String related_pk;
	private List<AirTicketPaidDetailBean> details;
	private List<PaymentDetailBean> payment_details;

	@Autowired
	private PaymentDetailService paymentDetailService;

	@Autowired
	private ReceivedMatchService receivedMatchService;

	/**
	 * 搜索支付详情
	 * 
	 * @return
	 */
	public String searchPaidDetailByRelatedPk() {
		details = service.selectByRelatedPk(related_pk);
		payment_details = new ArrayList<PaymentDetailBean>();
		if (null != details && details.size() > 0) {
			AirTicketPaidDetailBean current_detail = details.get(0);
			if (current_detail.getType().equals(ResourcesConstants.PAID_TYPE_BACK)) {
				List<ReceivedMatchBean> rmbs = receivedMatchService.selectByReceivedPk(current_detail.getPk());
				for (ReceivedMatchBean rmb : rmbs) {
					PaymentDetailBean pd = paymentDetailService.selectByPk(rmb.getDetail_pk());
					payment_details.add(pd);
				}
			} else {
				String voucher_number = current_detail.getVoucher_number();
				if (SimpletinyString.isEmpty(voucher_number))
					return SUCCESS;

				String[] voucher_numbers = voucher_number.split(",");

				for (String v_n : voucher_numbers) {
					List<PaymentDetailBean> list = paymentDetailService.selectByVoucherNumber(v_n);
					payment_details.addAll(list);
				}
			}
		}
		return SUCCESS;
	}

	public String rollBackTicketPayApply() {
		resultStr = service.rollBackPayApply(related_pk);
		return SUCCESS;
	}

	private String detail_pk;
	private String product_manager_number;
	private String belong_month;

	/**
	 * 临时方法，在票务添加完押金扣款的责任产品经理之后删除。
	 * 
	 * @return
	 */
	public String addProductManager() {
		resultStr = service.addProductManger(detail_pk, product_manager_number, belong_month);
		return SUCCESS;
	}

	private String json;

	private AirTicketPaidDetailBean detail;

	// 机票款退反
	public String backRecive() {
		resultStr = service.backRecive(detail, json);
		return SUCCESS;
	}

	/**
	 * 机票往来业务冲账
	 * 
	 * @return
	 */
	public String businessStrike() {
		resultStr = service.businessStrike(json);
		return SUCCESS;
	}

	/**
	 * 机票往来押金冲账
	 * 
	 * @return
	 */
	public String depositStrike() {
		resultStr = service.depositStrike(json);
		return SUCCESS;
	}

	/**
	 * 机票押金无业务扣款
	 * 
	 * @return
	 */
	public String createDeduct() {
		resultStr = service.createDeduct(json);
		return SUCCESS;
	}

	private PaymentDetailBean payment_detail;

	/**
	 * 新建票务单纯收支
	 * 
	 * @return
	 */
	public String createPaymentDetail() {
		resultStr = service.createPaymentDetail(payment_detail);
		return SUCCESS;
	}

	public AirTicketPaidDto getOption() {
		return option;
	}

	public void setOption(AirTicketPaidDto option) {
		this.option = option;
	}

	public List<AirTicketPaidDto> getPaids() {
		return paids;
	}

	public void setPaids(List<AirTicketPaidDto> paids) {
		this.paids = paids;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public List<AirTicketPaidDetailBean> getDetails() {
		return details;
	}

	public List<PaymentDetailBean> getPayment_details() {
		return payment_details;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public void setDetails(List<AirTicketPaidDetailBean> details) {
		this.details = details;
	}

	public void setPayment_details(List<PaymentDetailBean> payment_details) {
		this.payment_details = payment_details;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public AirTicketPaidDetailBean getDetail() {
		return detail;
	}

	public void setDetail(AirTicketPaidDetailBean detail) {
		this.detail = detail;
	}

	public PaymentDetailBean getPayment_detail() {
		return payment_detail;
	}

	public void setPayment_detail(PaymentDetailBean payment_detail) {
		this.payment_detail = payment_detail;
	}

	public String getDetail_pk() {
		return detail_pk;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public void setDetail_pk(String detail_pk) {
		this.detail_pk = detail_pk;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public String getBelong_month() {
		return belong_month;
	}

	public void setBelong_month(String belong_month) {
		this.belong_month = belong_month;
	}

}