package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class AirReceivedDetailBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = 2832737058540041725L;

	private String business_number;

	private java.math.BigDecimal received;

	private java.math.BigDecimal sum_received;

	private String received_type;

	private String received_time;

	private String card_account;

	private String related_pk;

	private String supplier_pk;

	private String voucher_file;

	private String status;

	private String comment;

	private String confirm_time;

	private String confirm_user;

	private String collecter;

	private String apply_user;

	private String confirm_user_name;

	// options
	private List<String> statuses;
	private String supplier_name;
	private String received_month;
	private BigDecimal money_from;
	private BigDecimal money_to;

	public String getBusiness_number() {
		return business_number;
	}

	public java.math.BigDecimal getReceived() {
		return received;
	}

	public java.math.BigDecimal getSum_received() {
		return sum_received;
	}

	public String getReceived_type() {
		return received_type;
	}

	public String getReceived_time() {
		return received_time;
	}

	public String getCard_account() {
		return card_account;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public String getSupplier_pk() {
		return supplier_pk;
	}

	public String getVoucher_file() {
		return voucher_file;
	}

	public String getStatus() {
		return status;
	}

	public String getComment() {
		return comment;
	}

	public String getConfirm_time() {
		return confirm_time;
	}

	public String getConfirm_user() {
		return confirm_user;
	}

	public String getCollecter() {
		return collecter;
	}

	public void setBusiness_number(String business_number) {
		this.business_number = business_number;
	}

	public void setReceived(java.math.BigDecimal received) {
		this.received = received;
	}

	public void setSum_received(java.math.BigDecimal sum_received) {
		this.sum_received = sum_received;
	}

	public void setReceived_type(String received_type) {
		this.received_type = received_type;
	}

	public void setReceived_time(String received_time) {
		this.received_time = received_time;
	}

	public void setCard_account(String card_account) {
		this.card_account = card_account;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public void setSupplier_pk(String supplier_pk) {
		this.supplier_pk = supplier_pk;
	}

	public void setVoucher_file(String voucher_file) {
		this.voucher_file = voucher_file;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}

	public void setConfirm_user(String confirm_user) {
		this.confirm_user = confirm_user;
	}

	public void setCollecter(String collecter) {
		this.collecter = collecter;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}

	public String getReceived_month() {
		return received_month;
	}

	public void setReceived_month(String received_month) {
		this.received_month = received_month;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getApply_user() {
		return apply_user;
	}

	public String getConfirm_user_name() {
		return confirm_user_name;
	}

	public void setApply_user(String apply_user) {
		this.apply_user = apply_user;
	}

	public void setConfirm_user_name(String confirm_user_name) {
		this.confirm_user_name = confirm_user_name;
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
