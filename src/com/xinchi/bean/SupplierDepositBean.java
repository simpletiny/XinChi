package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class SupplierDepositBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 5507354925422129142L;

	private String voucher_number;
	private String received_voucher_number;
	private String account;

	private String supplier_pk;

	private String supplier_name;

	private BigDecimal money;

	private BigDecimal received;

	private BigDecimal balance;

	private String return_date;

	private String status;

	private String return_way;

	private String comment;

	private String deposit_type;

	private String deposit_number;

	// DTO
	private List<String> statuses;
	private String voucher_file_name;
	private String time;
	private int pay_index;

	private BigDecimal money_from;
	private BigDecimal money_to;

	public String getVoucher_number() {
		return voucher_number;
	}

	public String getAccount() {
		return account;
	}

	public String getSupplier_pk() {
		return supplier_pk;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public BigDecimal getReceived() {
		return received;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public String getReturn_date() {
		return return_date;
	}

	public String getStatus() {
		return status;
	}

	public String getReturn_way() {
		return return_way;
	}

	public String getDeposit_type() {
		return deposit_type;
	}

	public void setVoucher_number(String voucher_number) {
		this.voucher_number = voucher_number;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setSupplier_pk(String supplier_pk) {
		this.supplier_pk = supplier_pk;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public void setReceived(BigDecimal received) {
		this.received = received;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setReturn_date(String return_date) {
		this.return_date = return_date;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setReturn_way(String return_way) {
		this.return_way = return_way;
	}

	public void setDeposit_type(String deposit_type) {
		this.deposit_type = deposit_type;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getVoucher_file_name() {
		return voucher_file_name;
	}

	public void setVoucher_file_name(String voucher_file_name) {
		this.voucher_file_name = voucher_file_name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getReceived_voucher_number() {
		return received_voucher_number;
	}

	public void setReceived_voucher_number(String received_voucher_number) {
		this.received_voucher_number = received_voucher_number;
	}

	public int getPay_index() {
		return pay_index;
	}

	public void setPay_index(int pay_index) {
		this.pay_index = pay_index;
	}

	public String getDeposit_number() {
		return deposit_number;
	}

	public void setDeposit_number(String deposit_number) {
		this.deposit_number = deposit_number;
	}

	public BigDecimal getMoney_from() {
		return money_from;
	}

	public BigDecimal getMoney_to() {
		return money_to;
	}

	public void setMoney_from(BigDecimal money_from) {
		this.money_from = money_from;
	}

	public void setMoney_to(BigDecimal money_to) {
		this.money_to = money_to;
	}

}
