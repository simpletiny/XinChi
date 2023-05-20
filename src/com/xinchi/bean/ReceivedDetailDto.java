package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class ReceivedDetailDto extends SupperBO implements Serializable {

	private static final long serialVersionUID = -7818640021219098779L;

	private BigDecimal received;

	private String type;

	private String received_time;

	private String card_account;

	private String related_pk;

	private String apply_user;
	private String apply_user_number;

	private String apply_date;

	private String pay_user;

	private String pay_user_pk;

	private String from_where;

	private String voucher_file;

	private String business_number;
	private BigDecimal sum_received;

	private String comment;

	/**
	 * 从客户收款详情复制
	 * 
	 * @param detail
	 */
	public void copyFromClientReceived(ClientReceivedDetailBean detail) {
		this.card_account = detail.getCard_account();
		this.sum_received = detail.getSum_received();
		this.received_time = detail.getReceived_time();
		this.business_number = detail.getTeam_number();
		this.pay_user = detail.getClient_employee_name();
		this.received = detail.getReceived();
	}

	/**
	 * 从供应商返款复制
	 * 
	 * @param detail
	 */
	public void copyFromSupplierReceived(SupplierPaidDetailBean detail) {
		this.card_account = detail.getCard_account();
		this.sum_received = detail.getAllot_money();
		this.received_time = detail.getTime();
		this.business_number = detail.getTeam_number();
		this.pay_user = detail.getSupplier_employee_name();
		this.received = detail.getMoney().abs();
	}

	/**
	 * 从机票返款复制
	 * 
	 * @param detail
	 */
	public void copyFromAirSupplierReceived(AirTicketPaidDetailBean detail) {
		this.card_account = detail.getCard_account();
		this.sum_received = detail.getAllot_money();
		this.received_time = detail.getTime();
		this.business_number = "无";
		this.pay_user = detail.getSupplier_employee_name();
		this.received = detail.getMoney().abs();
	}

	/**
	 * 从机票收入详情复制
	 * 
	 * @param detail
	 */
	public void copyFromAirReceived(AirReceivedDetailBean detail) {
		this.card_account = detail.getCard_account();
		this.sum_received = detail.getSum_received();
		this.received_time = detail.getReceived_time();
		this.business_number = detail.getBusiness_number();
		this.pay_user = detail.getSupplier_name();
		this.received = detail.getReceived();
	}

	// option
	private String date;

	public BigDecimal getReceived() {
		return received;
	}

	public String getType() {
		return type;
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

	public String getApply_user() {
		return apply_user;
	}

	public String getApply_date() {
		return apply_date;
	}

	public String getPay_user() {
		return pay_user;
	}

	public String getPay_user_pk() {
		return pay_user_pk;
	}

	public String getFrom_where() {
		return from_where;
	}

	public void setReceived(BigDecimal received) {
		this.received = received;
	}

	public void setType(String type) {
		this.type = type;
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

	public void setApply_user(String apply_user) {
		this.apply_user = apply_user;
	}

	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}

	public void setPay_user(String pay_user) {
		this.pay_user = pay_user;
	}

	public void setPay_user_pk(String pay_user_pk) {
		this.pay_user_pk = pay_user_pk;
	}

	public void setFrom_where(String from_where) {
		this.from_where = from_where;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVoucher_file() {
		return voucher_file;
	}

	public void setVoucher_file(String voucher_file) {
		this.voucher_file = voucher_file;
	}

	public String getBusiness_number() {
		return business_number;
	}

	public BigDecimal getSum_received() {
		return sum_received;
	}

	public void setBusiness_number(String business_number) {
		this.business_number = business_number;
	}

	public void setSum_received(BigDecimal sum_received) {
		this.sum_received = sum_received;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getApply_user_number() {
		return apply_user_number;
	}

	public void setApply_user_number(String apply_user_number) {
		this.apply_user_number = apply_user_number;
	}

}
