package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class OrderSupplierSaleOrderNameInfoBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -2890192810382671944L;

	private String name_pk;

	private BigDecimal price;

	private String base_pk;

	private BigDecimal final_price;

	// DTO
	private String passenger_id;
	private String passenger_name;

	public String getName_pk() {
		return name_pk;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getBase_pk() {
		return base_pk;
	}

	public void setName_pk(String name_pk) {
		this.name_pk = name_pk;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setBase_pk(String base_pk) {
		this.base_pk = base_pk;
	}

	public String getPassenger_id() {
		return passenger_id;
	}

	public String getPassenger_name() {
		return passenger_name;
	}

	public void setPassenger_id(String passenger_id) {
		this.passenger_id = passenger_id;
	}

	public void setPassenger_name(String passenger_name) {
		this.passenger_name = passenger_name;
	}

	public BigDecimal getFinal_price() {
		return final_price;
	}

	public void setFinal_price(BigDecimal final_price) {
		this.final_price = final_price;
	}

}
