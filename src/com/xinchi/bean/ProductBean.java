package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class ProductBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -4179083864639813045L;

	private String name;

	private String product_number;

	private String location;

	private Integer days;

	private BigDecimal max_profit_substract;

	private BigDecimal business_price;

	private String product_manager;

	private String sale_flg;

	private String comment;

	private String pk;

	private String create_user;

	private String update_user;

	private String air_ticket_charge;

	private BigDecimal product_value;

	private BigDecimal air_ticket_cost;

	private BigDecimal other_cost;

	private BigDecimal gross_profit;

	private float gross_profit_rate;

	private String product_model;

	private BigDecimal adult_price;

	private BigDecimal child_price;

	private BigDecimal business_profit_substract;

	private BigDecimal air_ticket_child_cost;

	private BigDecimal local_adult_cost;

	private BigDecimal local_child_cost;

	private BigDecimal other_child_cost;

	private BigDecimal product_child_value;

	private BigDecimal cash_flow;

	private BigDecimal spot_cash;

	private String first_air_start;

	private String first_air_end;

	private String sale_attention;

	private String sale_strategy;

	private String strict_price_flg;

	private String cash_flow_air_flg;

	private String cash_flow_local_flg;

	private String cash_flow_other_flg;

	private String off_shelves_date;

	private BigDecimal gross_child_profit;
	private BigDecimal cash_child_flow;
	private BigDecimal spot_child_cash;
	private float gross_child_profit_rate;

	// search options
	private List<String> locations;
	private List<String> statuses;
	
	private String high_value_flg ="N";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProduct_number() {
		return product_number;
	}

	public void setProduct_number(String product_number) {
		this.product_number = product_number;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public BigDecimal getMax_profit_substract() {
		return max_profit_substract;
	}

	public void setMax_profit_substract(BigDecimal max_profit_substract) {
		this.max_profit_substract = max_profit_substract;
	}

	public String getProduct_manager() {
		return product_manager;
	}

	public void setProduct_manager(String product_manager) {
		this.product_manager = product_manager;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public BigDecimal getBusiness_price() {
		return business_price;
	}

	public void setBusiness_price(BigDecimal business_price) {
		this.business_price = business_price;
	}

	public String getSale_flg() {
		return sale_flg;
	}

	public void setSale_flg(String sale_flg) {
		this.sale_flg = sale_flg;
	}

	public String getAir_ticket_charge() {
		return air_ticket_charge;
	}

	public void setAir_ticket_charge(String air_ticket_charge) {
		this.air_ticket_charge = air_ticket_charge;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public BigDecimal getAir_ticket_cost() {
		return air_ticket_cost;
	}

	public void setAir_ticket_cost(BigDecimal air_ticket_cost) {
		this.air_ticket_cost = air_ticket_cost;
	}

	public BigDecimal getOther_cost() {
		return other_cost;
	}

	public void setOther_cost(BigDecimal other_cost) {
		this.other_cost = other_cost;
	}

	public BigDecimal getGross_profit() {
		return gross_profit;
	}

	public void setGross_profit(BigDecimal gross_profit) {
		this.gross_profit = gross_profit;
	}

	public float getGross_profit_rate() {
		return gross_profit_rate;
	}

	public void setGross_profit_rate(float gross_profit_rate) {
		this.gross_profit_rate = gross_profit_rate;
	}

	public BigDecimal getProduct_value() {
		return product_value;
	}

	public void setProduct_value(BigDecimal product_value) {
		this.product_value = product_value;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}

	public String getProduct_model() {
		return product_model;
	}

	public BigDecimal getAdult_price() {
		return adult_price;
	}

	public BigDecimal getChild_price() {
		return child_price;
	}

	public BigDecimal getBusiness_profit_substract() {
		return business_profit_substract;
	}

	public BigDecimal getAir_ticket_child_cost() {
		return air_ticket_child_cost;
	}

	public BigDecimal getLocal_adult_cost() {
		return local_adult_cost;
	}

	public BigDecimal getLocal_child_cost() {
		return local_child_cost;
	}

	public BigDecimal getOther_child_cost() {
		return other_child_cost;
	}

	public BigDecimal getProduct_child_value() {
		return product_child_value;
	}

	public BigDecimal getCash_flow() {
		return cash_flow;
	}

	public BigDecimal getSpot_cash() {
		return spot_cash;
	}

	public String getFirst_air_start() {
		return first_air_start;
	}

	public String getFirst_air_end() {
		return first_air_end;
	}

	public String getSale_attention() {
		return sale_attention;
	}

	public String getSale_strategy() {
		return sale_strategy;
	}

	public String getStrict_price_flg() {
		return strict_price_flg;
	}

	public String getCash_flow_air_flg() {
		return cash_flow_air_flg;
	}

	public String getCash_flow_local_flg() {
		return cash_flow_local_flg;
	}

	public String getCash_flow_other_flg() {
		return cash_flow_other_flg;
	}

	public void setProduct_model(String product_model) {
		this.product_model = product_model;
	}

	public void setAdult_price(BigDecimal adult_price) {
		this.adult_price = adult_price;
	}

	public void setChild_price(BigDecimal child_price) {
		this.child_price = child_price;
	}

	public void setBusiness_profit_substract(BigDecimal business_profit_substract) {
		this.business_profit_substract = business_profit_substract;
	}

	public void setAir_ticket_child_cost(BigDecimal air_ticket_child_cost) {
		this.air_ticket_child_cost = air_ticket_child_cost;
	}

	public void setLocal_adult_cost(BigDecimal local_adult_cost) {
		this.local_adult_cost = local_adult_cost;
	}

	public void setLocal_child_cost(BigDecimal local_child_cost) {
		this.local_child_cost = local_child_cost;
	}

	public void setOther_child_cost(BigDecimal other_child_cost) {
		this.other_child_cost = other_child_cost;
	}

	public void setProduct_child_value(BigDecimal product_child_value) {
		this.product_child_value = product_child_value;
	}

	public void setCash_flow(BigDecimal cash_flow) {
		this.cash_flow = cash_flow;
	}

	public void setSpot_cash(BigDecimal spot_cash) {
		this.spot_cash = spot_cash;
	}

	public void setFirst_air_start(String first_air_start) {
		this.first_air_start = first_air_start;
	}

	public void setFirst_air_end(String first_air_end) {
		this.first_air_end = first_air_end;
	}

	public void setSale_attention(String sale_attention) {
		this.sale_attention = sale_attention;
	}

	public void setSale_strategy(String sale_strategy) {
		this.sale_strategy = sale_strategy;
	}

	public void setStrict_price_flg(String strict_price_flg) {
		this.strict_price_flg = strict_price_flg;
	}

	public void setCash_flow_air_flg(String cash_flow_air_flg) {
		this.cash_flow_air_flg = cash_flow_air_flg;
	}

	public void setCash_flow_local_flg(String cash_flow_local_flg) {
		this.cash_flow_local_flg = cash_flow_local_flg;
	}

	public void setCash_flow_other_flg(String cash_flow_other_flg) {
		this.cash_flow_other_flg = cash_flow_other_flg;
	}

	public String getOff_shelves_date() {
		return off_shelves_date;
	}

	public void setOff_shelves_date(String off_shelves_date) {
		this.off_shelves_date = off_shelves_date;
	}

	public BigDecimal getGross_child_profit() {
		return gross_child_profit;
	}

	public BigDecimal getCash_child_flow() {
		return cash_child_flow;
	}

	public BigDecimal getSpot_child_cash() {
		return spot_child_cash;
	}

	public float getGross_child_profit_rate() {
		return gross_child_profit_rate;
	}

	public void setGross_child_profit(BigDecimal gross_child_profit) {
		this.gross_child_profit = gross_child_profit;
	}

	public void setCash_child_flow(BigDecimal cash_child_flow) {
		this.cash_child_flow = cash_child_flow;
	}

	public void setSpot_child_cash(BigDecimal spot_child_cash) {
		this.spot_child_cash = spot_child_cash;
	}

	public void setGross_child_profit_rate(float gross_child_profit_rate) {
		this.gross_child_profit_rate = gross_child_profit_rate;
	}

	public String getHigh_value_flg() {
		return high_value_flg;
	}

	public void setHigh_value_flg(String high_value_flg) {
		this.high_value_flg = high_value_flg;
	}

}
