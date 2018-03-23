package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class ProductDelayBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -8870103613695907905L;

	private String pk;

	private String product_pk;

	private BigDecimal adult_price;

	private BigDecimal business_profit_substract;

	private BigDecimal max_profit_substract;

	private BigDecimal product_value;

	private BigDecimal product_child_value;

	private BigDecimal child_price;

	private Integer update_cnt;

	private String create_user;

	private String update_user;
	private BigDecimal gross_profit;

	private float gross_profit_rate;

	private BigDecimal cash_flow;

	private BigDecimal spot_cash;

	public String getPk() {
		return pk;
	}

	public String getProduct_pk() {
		return product_pk;
	}

	public BigDecimal getAdult_price() {
		return adult_price;
	}

	public BigDecimal getBusiness_profit_substract() {
		return business_profit_substract;
	}

	public BigDecimal getProduct_value() {
		return product_value;
	}

	public BigDecimal getProduct_child_value() {
		return product_child_value;
	}

	public BigDecimal getChild_price() {
		return child_price;
	}

	public Integer getUpdate_cnt() {
		return update_cnt;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public void setAdult_price(BigDecimal adult_price) {
		this.adult_price = adult_price;
	}

	public void setBusiness_profit_substract(BigDecimal business_profit_substract) {
		this.business_profit_substract = business_profit_substract;
	}

	public void setProduct_value(BigDecimal product_value) {
		this.product_value = product_value;
	}

	public void setProduct_child_value(BigDecimal product_child_value) {
		this.product_child_value = product_child_value;
	}

	public void setChild_price(BigDecimal child_price) {
		this.child_price = child_price;
	}

	public void setUpdate_cnt(Integer update_cnt) {
		this.update_cnt = update_cnt;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public BigDecimal getMax_profit_substract() {
		return max_profit_substract;
	}

	public void setMax_profit_substract(BigDecimal max_profit_substract) {
		this.max_profit_substract = max_profit_substract;
	}

	public BigDecimal getGross_profit() {
		return gross_profit;
	}

	public float getGross_profit_rate() {
		return gross_profit_rate;
	}

	public BigDecimal getCash_flow() {
		return cash_flow;
	}

	public BigDecimal getSpot_cash() {
		return spot_cash;
	}

	public void setGross_profit(BigDecimal gross_profit) {
		this.gross_profit = gross_profit;
	}

	public void setGross_profit_rate(float gross_profit_rate) {
		this.gross_profit_rate = gross_profit_rate;
	}

	public void setCash_flow(BigDecimal cash_flow) {
		this.cash_flow = cash_flow;
	}

	public void setSpot_cash(BigDecimal spot_cash) {
		this.spot_cash = spot_cash;
	}

}
