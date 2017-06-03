package com.xinchi.backend.accounting.action;

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

import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.finance.service.CardService;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccPaidAction extends BaseAction {
	private static final long serialVersionUID = 7171145601340932783L;

	private WaitingForPaidBean wfp;

	private List<WaitingForPaidBean> wfps;

	@Autowired
	private AccPaidService service;

	public String searchWaitingForPaidByPage() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", wfp);
		wfp.setStatus(SimpletinyString.addSingleQuote(wfp.getStatus()));
		page.setParams(params);

		wfps = service.selectByPage(page);
		return SUCCESS;
	}

	private String wfp_pk;

	public String searchOneWFP() {
		wfp = service.selectByPk(wfp_pk);
		return SUCCESS;
	}

	private String json;
	private String voucher_number;

	@Autowired
	private PaymentDetailService pds;
	@Autowired
	private CardService cs;

	public String pay() {
		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String account = obj.getString("account");
			String time = obj.getString("time");
			String receiver = obj.getString("receiver");
			BigDecimal balance = new BigDecimal(cs.getAccountBalance(account));
			BigDecimal money = new BigDecimal(obj.getString("money"));
			PaymentDetailBean detail = new PaymentDetailBean();
			detail.setVoucher_number(voucher_number);
			detail.setAccount(account);
			detail.setTime(time);
			detail.setReceiver(receiver);
			detail.setMoney(money);
			detail.setBalance(balance.subtract(money));
			detail.setType("支出");
			detail.setComment(receiver + ",凭证号：" + voucher_number);
			resultStr = pds.insert(detail);
			if (!resultStr.equals(SUCCESS)) {
				return SUCCESS;
			}
		}
		// 更新待支付状态
		wfp = service.selectByPayNumber(voucher_number);
		wfp.setStatus(ResourcesConstants.PAY_STATUS_YES);
		service.update(wfp);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	public WaitingForPaidBean getWfp() {
		return wfp;
	}

	public void setWfp(WaitingForPaidBean wfp) {
		this.wfp = wfp;
	}

	public List<WaitingForPaidBean> getWfps() {
		return wfps;
	}

	public void setWfps(List<WaitingForPaidBean> wfps) {
		this.wfps = wfps;
	}

	public String getWfp_pk() {
		return wfp_pk;
	}

	public void setWfp_pk(String wfp_pk) {
		this.wfp_pk = wfp_pk;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getVoucher_number() {
		return voucher_number;
	}

	public void setVoucher_number(String voucher_number) {
		this.voucher_number = voucher_number;
	}
}
