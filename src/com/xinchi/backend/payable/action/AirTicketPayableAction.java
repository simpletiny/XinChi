package com.xinchi.backend.payable.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.finance.service.CardService;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.payable.service.AirTicketPaidDetailService;
import com.xinchi.backend.payable.service.AirTicketPayableService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.SimpletinyUser;

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

	@Autowired
	private PaymentDetailService pds;
	@Autowired
	private CardService cardService;

	@Autowired
	private NumberService numberService;

	@Autowired
	private AccPaidService accPaidService;
	private BigDecimal allot_money;

	// 支付机票款
	public String payAirTicket() {

		// 生成申请信息，并更新应付款“已付款”内容
		JSONArray payableArray = JSONArray.fromObject(payableJson);

		String related_pk = DBCommonUtil.genPk();
		BigDecimal sumPaid = BigDecimal.ZERO;

		for (int i = 0; i < payableArray.size(); i++) {
			JSONObject obj = JSONObject.fromObject(payableArray.get(i));

			String payable_pk = obj.getString("payable_pk");
			String this_paid = obj.getString("this_paid");
			AirTicketPayableBean airTicketPayable = service.selectByPrimaryKey(payable_pk);

			String supplier_employee_pk = airTicketPayable.getSupplier_employee_pk();
			String base_pk = airTicketPayable.getPk();
			String PNR = airTicketPayable.getPNR();

			AirTicketPaidDetailBean currentDetail = new AirTicketPaidDetailBean();

			currentDetail.setAllot_money(allot_money);
			currentDetail.setSupplier_employee_pk(supplier_employee_pk);
			currentDetail.setBase_pk(base_pk);
			currentDetail.setPNR(PNR);
			currentDetail.setType(ResourcesConstants.PAID_TYPE_PAID);
			currentDetail.setStatus(ResourcesConstants.PAID_STATUS_PAID);
			currentDetail.setRelated_pk(related_pk);

			if (!SimpletinyString.isEmpty(this_paid)) {
				sumPaid = new BigDecimal(this_paid);
			}
			currentDetail.setTime(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
			currentDetail.setMoney(sumPaid);
			currentDetail.setConfirm_time(DateUtil.getTimeMillis());
			currentDetail.setApprove_user(SimpletinyUser.getUser_number());
			airTicketPaidDetailService.insert(currentDetail);

			airTicketPayable.setPaid(airTicketPayable.getPaid().add(sumPaid));
			airTicketPayable.setBudget_balance(airTicketPayable.getBudget_balance().subtract(sumPaid));
			if (airTicketPayable.getFinal_flg().equals("Y")) {
				airTicketPayable.setFinal_balance(airTicketPayable.getFinal_balance().subtract(sumPaid));
			}
			service.update(airTicketPayable);
		}

		JSONArray array = JSONArray.fromObject(paidJson);

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String account = obj.getString("account");
			String time = obj.getString("time");
			String receiver = obj.getString("receiver");
			BigDecimal balance = new BigDecimal(cardService.getAccountBalance(account));
			BigDecimal money = new BigDecimal(obj.getString("money"));
			String voucher_file_name = obj.getString("voucherFile");

			String voucher_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER, ResourcesConstants.PAY_TYPE_PIAOWU,
					DateUtil.getDateStr(DateUtil.YYYYMMDD));

			PaymentDetailBean detail = new PaymentDetailBean();
			detail.setVoucher_number(voucher_number);
			detail.setAccount(account);
			detail.setTime(time);
			detail.setReceiver(receiver);
			detail.setMoney(money);
			detail.setBalance(balance.subtract(money));
			detail.setType("支出");
			detail.setComment(receiver + ",凭证号：" + voucher_number);
			detail.setVoucher_file_name(voucher_file_name);

			resultStr = pds.insert(detail);
			if (!resultStr.equals(SUCCESS)) {
				return SUCCESS;
			}

			// 生成待支付数据并直接写入为已支付状态
			WaitingForPaidBean waiting = new WaitingForPaidBean();
			waiting.setPay_number(voucher_number);

			waiting.setItem(ResourcesConstants.PAY_TYPE_PIAOWU);
			waiting.setReceiver(receiver);
			waiting.setMoney(money);
			waiting.setApply_user(SimpletinyUser.getUser_number());
			waiting.setApproval_user(SimpletinyUser.getUser_number());
			waiting.setRelated_pk(related_pk);
			waiting.setStatus(ResourcesConstants.PAY_STATUS_YES);
			waiting.setPay_user(SimpletinyUser.getUser_number());

			accPaidService.insert(waiting);
		}
		resultStr = SUCCESS;
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
}