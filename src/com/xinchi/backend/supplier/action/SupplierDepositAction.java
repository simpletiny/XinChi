package com.xinchi.backend.supplier.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.supplier.service.SupplierDepositService;
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

	private String deposit_pk;

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

}