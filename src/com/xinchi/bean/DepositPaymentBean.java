package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class DepositPaymentBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 2277098359348186298L;

	private String deposit_pk;

	private String payment_voucher_number;

	public String getDeposit_pk() {
		return deposit_pk;
	}

	public String getPayment_voucher_number() {
		return payment_voucher_number;
	}

	public void setDeposit_pk(String deposit_pk) {
		this.deposit_pk = deposit_pk;
	}

	public void setPayment_voucher_number(String payment_voucher_number) {
		this.payment_voucher_number = payment_voucher_number;
	}

}
