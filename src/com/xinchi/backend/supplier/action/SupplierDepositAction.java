package com.xinchi.backend.supplier.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.payable.service.AirTicketPaidDetailService;
import com.xinchi.backend.receivable.service.AirReceivedService;
import com.xinchi.backend.supplier.service.SupplierDepositService;
import com.xinchi.bean.AirReceivedDetailBean;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.DepositTicketPaidBean;
import com.xinchi.bean.SupplierDepositBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope("prototype")
public class SupplierDepositAction extends BaseAction {

	private static final long serialVersionUID = 8384774237157460348L;

	@Autowired
	private SupplierDepositService service;

	private SupplierDepositBean deposit;

	private List<SupplierDepositBean> deposits;

	/**
	 * 查询押金列表
	 * 
	 * @return
	 */
	public String searchDepositByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", deposit);
		page.setParams(params);

		deposits = service.selectByPage(page);
		return SUCCESS;
	}

	/**
	 * 新建押金账
	 * 
	 * @return
	 */
	public String createSupplierDeposit() {
		resultStr = service.createDeposit(deposit);
		return SUCCESS;
	}

	private String file_name;
	private String deposit_type;

	/**
	 * 批量上传押金账
	 * 
	 * @return
	 * @throws IOException
	 */
	public String batUploadDeposit() throws IOException {
		deposits = service.batUploadDeposit(file_name, deposit_type);
		return SUCCESS;
	}

	public String batSaveDeposit() {
		resultStr = service.batSaveDeposit(json);
		return SUCCESS;
	}

	private String deposit_pk;

	private String voucher_number;

	public String searchDepositByVoucherNumber() {
		deposits = service.selectByVoucherNumber(voucher_number);

		return SUCCESS;
	}

	/**
	 * 删除押金账
	 * 
	 * @return
	 */
	public String deleteSupplierDeposit() {
		resultStr = service.deleteSupplierDeposit(deposit_pk);
		return SUCCESS;
	}

	private String json;

	/**
	 * 押金退回
	 * 
	 * @return
	 */
	public String receiveSupplierDeposit() {
		resultStr = service.receiveSupplierDeposit(deposit, json);
		return SUCCESS;
	}

	// 押金退还记录
	private List<AirReceivedDetailBean> deposit_receiveds;

	// 扣款记录
	private List<AirTicketPaidDetailBean> deposit_deducts;
	// 冲账记录
	private List<AirTicketPaidDetailBean> deposit_strikes;

	private List<DepositTicketPaidBean> deposit_deduct_relations;

	@Autowired
	private AirReceivedService airReceivedService;

	@Autowired
	private AirTicketPaidDetailService airTicketPaidDetailService;

	public String searchDepositReturnDetails() {
		deposit = service.selectByPrimaryKey(deposit_pk);

		if (deposit.getReturn_way().contains("C")) {
			deposit_deduct_relations = service.selectDepositTicketPaidsByDepositPk(deposit_pk);
			deposit_strikes = new ArrayList<AirTicketPaidDetailBean>();
			for (DepositTicketPaidBean dtp : deposit_deduct_relations) {
				List<AirTicketPaidDetailBean> li = airTicketPaidDetailService.selectByRelatedPk(dtp.getRelated_pk());
				deposit_strikes.addAll(li);
			}
		}
		if (deposit.getReturn_way().contains("T")) {
			deposit_receiveds = airReceivedService.selectByBusinessNumber(deposit.getDeposit_number());
		}
		if (deposit.getReturn_way().contains("K")) {
			deposit_deducts = airTicketPaidDetailService.selectByBasePk(deposit_pk);
		}
		return SUCCESS;
	}

	public SupplierDepositBean getDeposit() {
		return deposit;
	}

	public List<SupplierDepositBean> getDeposits() {
		return deposits;
	}

	public void setDeposit(SupplierDepositBean deposit) {
		this.deposit = deposit;
	}

	public void setDeposits(List<SupplierDepositBean> deposits) {
		this.deposits = deposits;
	}

	public String getDeposit_pk() {
		return deposit_pk;
	}

	public void setDeposit_pk(String deposit_pk) {
		this.deposit_pk = deposit_pk;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getFile_name() {
		return file_name;
	}

	public String getDeposit_type() {
		return deposit_type;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public void setDeposit_type(String deposit_type) {
		this.deposit_type = deposit_type;
	}

	public String getVoucher_number() {
		return voucher_number;
	}

	public void setVoucher_number(String voucher_number) {
		this.voucher_number = voucher_number;
	}

	public List<AirReceivedDetailBean> getDeposit_receiveds() {
		return deposit_receiveds;
	}

	public List<AirTicketPaidDetailBean> getDeposit_deducts() {
		return deposit_deducts;
	}

	public List<AirTicketPaidDetailBean> getDeposit_strikes() {
		return deposit_strikes;
	}

	public void setDeposit_receiveds(List<AirReceivedDetailBean> deposit_receiveds) {
		this.deposit_receiveds = deposit_receiveds;
	}

	public void setDeposit_deducts(List<AirTicketPaidDetailBean> deposit_deducts) {
		this.deposit_deducts = deposit_deducts;
	}

	public void setDeposit_strikes(List<AirTicketPaidDetailBean> deposit_strikes) {
		this.deposit_strikes = deposit_strikes;
	}

	public List<DepositTicketPaidBean> getDeposit_deduct_relations() {
		return deposit_deduct_relations;
	}

	public void setDeposit_deduct_relations(List<DepositTicketPaidBean> deposit_deduct_relations) {
		this.deposit_deduct_relations = deposit_deduct_relations;
	}

}