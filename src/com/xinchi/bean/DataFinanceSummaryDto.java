package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class DataFinanceSummaryDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = -7033035304242909908L;

	private BigDecimal receivable;
	private BigDecimal payable;
	private BigDecimal cash;

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

}
