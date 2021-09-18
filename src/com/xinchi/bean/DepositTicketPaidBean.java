package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class DepositTicketPaidBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 7471163355981919782L;

	private String deposit_pk;

	private String related_pk;

	private BigDecimal money;

	private String type;

	public String getDeposit_pk() {
		return deposit_pk;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public void setDeposit_pk(String deposit_pk) {
		this.deposit_pk = deposit_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
