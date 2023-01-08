package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class DataFinanceSummaryDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = -7033035304242909908L;

	// 现金
	private BigDecimal cash;
	// 待审批
	private BigDecimal waiting_for_approve;
	// 待支付
	private BigDecimal waiting_for_paid;
	// 现金余额
	private BigDecimal cash_balance;

	// 应收款
	private BigDecimal receivable;
	// 应付款
	private BigDecimal payable;
	// 应收应付余额
	private BigDecimal asset_balance;

	// 航司押金
	private BigDecimal air_deposit;
	// 其他押金
	private BigDecimal other_deposit;
	// 押金余额
	private BigDecimal deposit_balance;

	// 总余额
	private BigDecimal sum_balance;

	private BigDecimal positive_cash;
	private BigDecimal negative_cash;

	private List<KeyValueDto> areaReceivable;
	private List<KeyValueDto> areaPayable;
	private List<KeyValueDto> areaCash;
	private List<KeyValueDto> salesReceivable;

	public BigDecimal getReceivable() {
		return receivable;
	}

	public BigDecimal getPayable() {
		return payable;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public List<KeyValueDto> getAreaReceivable() {
		return areaReceivable;
	}

	public List<KeyValueDto> getAreaPayable() {
		return areaPayable;
	}

	public List<KeyValueDto> getAreaCash() {
		return areaCash;
	}

	public void setReceivable(BigDecimal receivable) {
		this.receivable = receivable;
	}

	public void setPayable(BigDecimal payable) {
		this.payable = payable;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public void setAreaReceivable(List<KeyValueDto> areaReceivable) {
		this.areaReceivable = areaReceivable;
	}

	public void setAreaPayable(List<KeyValueDto> areaPayable) {
		this.areaPayable = areaPayable;
	}

	public void setAreaCash(List<KeyValueDto> areaCash) {
		this.areaCash = areaCash;
	}

	public BigDecimal getPositive_cash() {
		return positive_cash;
	}

	public BigDecimal getNegative_cash() {
		return negative_cash;
	}

	public void setPositive_cash(BigDecimal positive_cash) {
		this.positive_cash = positive_cash;
	}

	public void setNegative_cash(BigDecimal negative_cash) {
		this.negative_cash = negative_cash;
	}

	public List<KeyValueDto> getSalesReceivable() {
		return salesReceivable;
	}

	public void setSalesReceivable(List<KeyValueDto> salesReceivable) {
		this.salesReceivable = salesReceivable;
	}

	public BigDecimal getWaiting_for_approve() {
		return waiting_for_approve;
	}

	public BigDecimal getWaiting_for_paid() {
		return waiting_for_paid;
	}

	public BigDecimal getCash_balance() {
		return cash_balance;
	}

	public BigDecimal getAsset_balance() {
		return asset_balance;
	}

	public BigDecimal getAir_deposit() {
		return air_deposit;
	}

	public BigDecimal getOther_deposit() {
		return other_deposit;
	}

	public BigDecimal getDeposit_balance() {
		return deposit_balance;
	}

	public BigDecimal getSum_balance() {
		return sum_balance;
	}

	public void setWaiting_for_approve(BigDecimal waiting_for_approve) {
		this.waiting_for_approve = waiting_for_approve;
	}

	public void setWaiting_for_paid(BigDecimal waiting_for_paid) {
		this.waiting_for_paid = waiting_for_paid;
	}

	public void setCash_balance(BigDecimal cash_balance) {
		this.cash_balance = cash_balance;
	}

	public void setAsset_balance(BigDecimal asset_balance) {
		this.asset_balance = asset_balance;
	}

	public void setAir_deposit(BigDecimal air_deposit) {
		this.air_deposit = air_deposit;
	}

	public void setOther_deposit(BigDecimal other_deposit) {
		this.other_deposit = other_deposit;
	}

	public void setDeposit_balance(BigDecimal deposit_balance) {
		this.deposit_balance = deposit_balance;
	}

	public void setSum_balance(BigDecimal sum_balance) {
		this.sum_balance = sum_balance;
	}

}
