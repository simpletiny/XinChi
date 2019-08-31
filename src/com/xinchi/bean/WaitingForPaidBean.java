package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class WaitingForPaidBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 5513220783949891570L;

	private String pay_number;

	private String item;

	private String receiver;

	private java.math.BigDecimal money;

	private String limit_time;

	private String apply_user;

	private String approval_user;

	private String status;

	private String create_user;

	private String pk;

	private String update_user;

	private String related_pk;

	private String pay_user;
	
	private String comment;

	// search options
	private BigDecimal money_from;
	private BigDecimal money_to;
	private String apply_date_from;
	private String apply_date_to;
	private List<String> statuses;

	public String getPay_number() {
		return pay_number;
	}

	public void setPay_number(String pay_number) {
		this.pay_number = pay_number;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

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

	public String getLimit_time() {
		return limit_time;
	}

	public void setLimit_time(String limit_time) {
		this.limit_time = limit_time;
	}

	public String getApply_user() {
		return apply_user;
	}

	public void setApply_user(String apply_user) {
		this.apply_user = apply_user;
	}

	public String getApproval_user() {
		return approval_user;
	}

	public void setApproval_user(String approval_user) {
		this.approval_user = approval_user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public String getPay_user() {
		return pay_user;
	}

	public void setPay_user(String pay_user) {
		this.pay_user = pay_user;
	}

	public BigDecimal getMoney_from() {
		return money_from;
	}

	public void setMoney_from(BigDecimal money_from) {
		this.money_from = money_from;
	}

	public BigDecimal getMoney_to() {
		return money_to;
	}

	public void setMoney_to(BigDecimal money_to) {
		this.money_to = money_to;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}

	public String getApply_date_from() {
		return apply_date_from;
	}

	public void setApply_date_from(String apply_date_from) {
		this.apply_date_from = apply_date_from;
	}

	public String getApply_date_to() {
		return apply_date_to;
	}

	public void setApply_date_to(String apply_date_to) {
		this.apply_date_to = apply_date_to;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
