package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class ProductProfitBean extends SupperBO implements Serializable, Comparable<ProductProfitBean> {
	private static final long serialVersionUID = -6861985805862234005L;

	private String departure_month;

	private String status;

	private Integer people_count;

	private BigDecimal gross_profit;

	private BigDecimal product_cost = BigDecimal.ZERO;

	private BigDecimal keep_cost = BigDecimal.ZERO;

	private BigDecimal air_lose;

	private BigDecimal other_cost = BigDecimal.ZERO;;

	private BigDecimal profit;

	private BigDecimal score;

	private String user_number;

	private String pk;

	private String create_user;

	private String update_user;

	private String option_year;

	// DTO
	// 手续费
	private BigDecimal service_fees = BigDecimal.ZERO;
	// 押金扣款
	private BigDecimal deposit_deduct = BigDecimal.ZERO;

	public String getDeparture_month() {
		return departure_month;
	}

	public String getStatus() {
		return status;
	}

	public Integer getPeople_count() {
		return people_count;
	}

	public BigDecimal getGross_profit() {
		return gross_profit;
	}

	public BigDecimal getProduct_cost() {
		return product_cost;
	}

	public BigDecimal getKeep_cost() {
		return keep_cost;
	}

	public BigDecimal getAir_lose() {
		return air_lose;
	}

	public BigDecimal getOther_cost() {
		return other_cost;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public String getUser_number() {
		return user_number;
	}

	public String getPk() {
		return pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setDeparture_month(String departure_month) {
		this.departure_month = departure_month;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPeople_count(Integer people_count) {
		this.people_count = people_count;
	}

	public void setGross_profit(BigDecimal gross_profit) {
		this.gross_profit = gross_profit;
	}

	public void setProduct_cost(BigDecimal product_cost) {
		this.product_cost = product_cost;
	}

	public void setKeep_cost(BigDecimal keep_cost) {
		this.keep_cost = keep_cost;
	}

	public void setAir_lose(BigDecimal air_lose) {
		this.air_lose = air_lose;
	}

	public void setOther_cost(BigDecimal other_cost) {
		this.other_cost = other_cost;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getOption_year() {
		return option_year;
	}

	public void setOption_year(String option_year) {
		this.option_year = option_year;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public BigDecimal getService_fees() {
		return service_fees;
	}

	public void setService_fees(BigDecimal service_fees) {
		this.service_fees = service_fees;
	}

	public BigDecimal getDeposit_deduct() {
		return deposit_deduct;
	}

	public void setDeposit_deduct(BigDecimal deposit_deduct) {
		this.deposit_deduct = deposit_deduct;
	}

	@Override
	public int compareTo(ProductProfitBean o) {
		return this.getDeparture_month().compareTo(o.getDeparture_month());
	}

}
