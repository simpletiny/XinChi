package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class PayApprovalBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = -437117872804705309L;

	private String receiver;

	private java.math.BigDecimal money;

	private String item;

	private String status;

	private String related_pk;

	private String apply_user;

	private String apply_time;

	private String limit_time;

	private String back_pk;

	private String pk;

	private String create_user;

	private String update_user;

	private String comment;

	private String approval_user;

	private String approval_time;

	private String receiver_card_number;
	private String receiver_bank;

	// options
	private List<String> statuses;
	private String date_from;
	private String date_to;

	private BigDecimal money_from;
	private BigDecimal money_to;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public java.math.BigDecimal getMoney() {
		return money;
	}

	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public String getApply_user() {
		return apply_user;
	}

	public void setApply_user(String apply_user) {
		this.apply_user = apply_user;
	}

	public String getApply_time() {
		return apply_time;
	}

	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}

	public String getLimit_time() {
		return limit_time;
	}

	public void setLimit_time(String limit_time) {
		this.limit_time = limit_time;
	}

	public String getBack_pk() {
		return back_pk;
	}

	public void setBack_pk(String back_pk) {
		this.back_pk = back_pk;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getApproval_user() {
		return approval_user;
	}

	public void setApproval_user(String approval_user) {
		this.approval_user = approval_user;
	}

	public String getApproval_time() {
		return approval_time;
	}

	public void setApproval_time(String approval_time) {
		this.approval_time = approval_time;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}

	public String getDate_from() {
		return date_from;
	}

	public void setDate_from(String date_from) {
		this.date_from = date_from;
	}

	public String getDate_to() {
		return date_to;
	}

	public void setDate_to(String date_to) {
		this.date_to = date_to;
	}

	public String getReceiver_card_number() {
		return receiver_card_number;
	}

	public void setReceiver_card_number(String receiver_card_number) {
		this.receiver_card_number = receiver_card_number;
	}

	public String getReceiver_bank() {
		return receiver_bank;
	}

	public void setReceiver_bank(String receiver_bank) {
		this.receiver_bank = receiver_bank;
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
