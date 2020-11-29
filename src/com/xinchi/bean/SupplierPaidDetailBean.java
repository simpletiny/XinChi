package com.xinchi.bean;

import java.io.Serializable;
import java.util.List;

import com.xinchi.common.SupperBO;

public class SupplierPaidDetailBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 439844938854617498L;

	private String team_number;

	private String supplier_employee_name;

	private String supplier_employee_pk;

	private java.math.BigDecimal money;

	private String time;

	private String type;

	private String confirm_time;

	private String status;

	private String comment;

	private java.math.BigDecimal sum_money;

	private java.math.BigDecimal allot_money;

	private String related_pk;

	private String card_pk;

	private String card_account;

	private String create_user;

	private String update_user;

	private String pk;
	private String limit_time;

	private String approve_user;

	private String paid_user;

	private String paid_user_name;
	private String approve_user_name;

	private String voucher_file_name;
	// options
	private List<String> statuses;
	private String supplier_name;

	public String getTeam_number() {
		return team_number;
	}

	public void setTeam_number(String team_number) {
		this.team_number = team_number;
	}

	public String getSupplier_employee_name() {
		return supplier_employee_name;
	}

	public void setSupplier_employee_name(String supplier_employee_name) {
		this.supplier_employee_name = supplier_employee_name;
	}

	public String getSupplier_employee_pk() {
		return supplier_employee_pk;
	}

	public void setSupplier_employee_pk(String supplier_employee_pk) {
		this.supplier_employee_pk = supplier_employee_pk;
	}

	public java.math.BigDecimal getMoney() {
		return money;
	}

	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConfirm_time() {
		return confirm_time;
	}

	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public java.math.BigDecimal getSum_money() {
		return sum_money;
	}

	public void setSum_money(java.math.BigDecimal sum_money) {
		this.sum_money = sum_money;
	}

	public java.math.BigDecimal getAllot_money() {
		return allot_money;
	}

	public void setAllot_money(java.math.BigDecimal allot_money) {
		this.allot_money = allot_money;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
	}

	public String getCard_pk() {
		return card_pk;
	}

	public void setCard_pk(String card_pk) {
		this.card_pk = card_pk;
	}

	public String getCard_account() {
		return card_account;
	}

	public void setCard_account(String card_account) {
		this.card_account = card_account;
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

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getLimit_time() {
		return limit_time;
	}

	public void setLimit_time(String limit_time) {
		this.limit_time = limit_time;
	}

	public String getApprove_user() {
		return approve_user;
	}

	public void setApprove_user(String approve_user) {
		this.approve_user = approve_user;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getPaid_user() {
		return paid_user;
	}

	public void setPaid_user(String paid_user) {
		this.paid_user = paid_user;
	}

	public String getPaid_user_name() {
		return paid_user_name;
	}

	public String getApprove_user_name() {
		return approve_user_name;
	}

	public void setPaid_user_name(String paid_user_name) {
		this.paid_user_name = paid_user_name;
	}

	public void setApprove_user_name(String approve_user_name) {
		this.approve_user_name = approve_user_name;
	}

	public String getVoucher_file_name() {
		return voucher_file_name;
	}

	public void setVoucher_file_name(String voucher_file_name) {
		this.voucher_file_name = voucher_file_name;
	}

}
