package com.xinchi.bean;

import java.io.Serializable;

import com.xinchi.common.SupperBO;

public class AirTicketPaidDetailBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -5070461142675051303L;

	private String supplier_employee_pk;
	private String supplier_employee_name;

	private String financial_body_pk;
	private String financial_body_name;

	private java.math.BigDecimal money;

	private java.math.BigDecimal allot_money;

	private java.math.BigDecimal sum_money;

	private String account;
	private String status;

	private String type;

	private String time;

	private String limit_time;

	private String confirm_time;

	private String approve_user;

	private String related_pk;

	private String comment;

	private String pk;

	private String create_user;

	private String update_user;

	private String base_pk;
	private String receiver;

	private String PNR;

	private String voucher_number;

	private String card_account;

	private String voucher_file;

	// 责任经理，针对押金扣款而设定
	private String product_manager;
	private String product_manager_name;

	private String belong_month;

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

	public java.math.BigDecimal getAllot_money() {
		return allot_money;
	}

	public void setAllot_money(java.math.BigDecimal allot_money) {
		this.allot_money = allot_money;
	}

	public java.math.BigDecimal getSum_money() {
		return sum_money;
	}

	public void setSum_money(java.math.BigDecimal sum_money) {
		this.sum_money = sum_money;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLimit_time() {
		return limit_time;
	}

	public void setLimit_time(String limit_time) {
		this.limit_time = limit_time;
	}

	public String getConfirm_time() {
		return confirm_time;
	}

	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}

	public String getApprove_user() {
		return approve_user;
	}

	public void setApprove_user(String approve_user) {
		this.approve_user = approve_user;
	}

	public String getRelated_pk() {
		return related_pk;
	}

	public void setRelated_pk(String related_pk) {
		this.related_pk = related_pk;
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

	public String getBase_pk() {
		return base_pk;
	}

	public void setBase_pk(String base_pk) {
		this.base_pk = base_pk;
	}

	public String getPNR() {
		return PNR;
	}

	public void setPNR(String pNR) {
		PNR = pNR;
	}

	public String getSupplier_employee_name() {
		return supplier_employee_name;
	}

	public void setSupplier_employee_name(String supplier_employee_name) {
		this.supplier_employee_name = supplier_employee_name;
	}

	public String getFinancial_body_pk() {
		return financial_body_pk;
	}

	public void setFinancial_body_pk(String financial_body_pk) {
		this.financial_body_pk = financial_body_pk;
	}

	public String getFinancial_body_name() {
		return financial_body_name;
	}

	public void setFinancial_body_name(String financial_body_name) {
		this.financial_body_name = financial_body_name;
	}

	public String getVoucher_number() {
		return voucher_number;
	}

	public void setVoucher_number(String voucher_number) {
		this.voucher_number = voucher_number;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getCard_account() {
		return card_account;
	}

	public void setCard_account(String card_account) {
		this.card_account = card_account;
	}

	public String getVoucher_file() {
		return voucher_file;
	}

	public void setVoucher_file(String voucher_file) {
		this.voucher_file = voucher_file;
	}

	public String getProduct_manager() {
		return product_manager;
	}

	public void setProduct_manager(String product_manager) {
		this.product_manager = product_manager;
	}

	public String getProduct_manager_name() {
		return product_manager_name;
	}

	public void setProduct_manager_name(String product_manager_name) {
		this.product_manager_name = product_manager_name;
	}

	public String getBelong_month() {
		return belong_month;
	}

	public void setBelong_month(String belong_month) {
		this.belong_month = belong_month;
	}

}
