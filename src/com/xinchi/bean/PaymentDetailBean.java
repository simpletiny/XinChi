package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xinchi.common.SupperBO;

public class PaymentDetailBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 8505739134690461246L;

	private String account;

	private String account_pk;
	private String time;

	private String type;

	private BigDecimal money;

	private BigDecimal balance;

	private String record_time;

	private String record_user;

	private String update_user;

	private String create_user;

	private String pk;
	private String comment;
	private String inner_flg;
	private String inner_pk;

	private String finance_flg;
	private String month;
	// 凭证号
	private String voucher_number;
	// 收款方，支出的时候有用
	private String receiver;
	// 凭证文件名
	private String voucher_file_name;
	// 收入匹配状态
	private String match_flg;
	// 汇兑标签
	private String exchange_flg;

	// 内转搜索条件
	private List<String> exchange_type;
	private String from_date;
	private String to_date;
	private String from_account;
	private String to_account;
	private String from_month;
	private String to_month;
	private String purpose;

	private String purpose_ticket;

	private BigDecimal money_from;
	private BigDecimal money_to;

	// dto
	private String receiver_pk;
	private String belong_month;
	private String product_manager;

	private List<String> match_flgs;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getRecord_time() {
		return record_time;
	}

	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}

	public String getRecord_user() {
		return record_user;
	}

	public void setRecord_user(String record_user) {
		this.record_user = record_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getInner_flg() {
		return inner_flg;
	}

	public void setInner_flg(String inner_flg) {
		this.inner_flg = inner_flg;
	}

	public String getInner_pk() {
		return inner_pk;
	}

	public void setInner_pk(String inner_pk) {
		this.inner_pk = inner_pk;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getVoucher_number() {
		return voucher_number;
	}

	public void setVoucher_number(String voucher_number) {
		this.voucher_number = voucher_number;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getVoucher_file_name() {
		return voucher_file_name;
	}

	public void setVoucher_file_name(String voucher_file_name) {
		this.voucher_file_name = voucher_file_name;
	}

	public String getAccount_pk() {
		return account_pk;
	}

	public void setAccount_pk(String account_pk) {
		this.account_pk = account_pk;
	}

	public String getMatch_flg() {
		return match_flg;
	}

	public void setMatch_flg(String match_flg) {
		this.match_flg = match_flg;
	}

	public List<String> getExchange_type() {
		return exchange_type;
	}

	public void setExchange_type(List<String> exchange_type) {
		this.exchange_type = exchange_type;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public String getFrom_account() {
		return from_account;
	}

	public void setFrom_account(String from_account) {
		this.from_account = from_account;
	}

	public String getTo_account() {
		return to_account;
	}

	public void setTo_account(String to_account) {
		this.to_account = to_account;
	}

	public String getFrom_month() {
		return from_month;
	}

	public void setFrom_month(String from_month) {
		this.from_month = from_month;
	}

	public String getTo_month() {
		return to_month;
	}

	public void setTo_month(String to_month) {
		this.to_month = to_month;
	}

	public String getExchange_flg() {
		return exchange_flg;
	}

	public void setExchange_flg(String exchange_flg) {
		this.exchange_flg = exchange_flg;
	}

	public String getFinance_flg() {
		return finance_flg;
	}

	public void setFinance_flg(String finance_flg) {
		this.finance_flg = finance_flg;
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

	public String getReceiver_pk() {
		return receiver_pk;
	}

	public void setReceiver_pk(String receiver_pk) {
		this.receiver_pk = receiver_pk;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getPurpose_ticket() {
		return purpose_ticket;
	}

	public void setPurpose_ticket(String purpose_ticket) {
		this.purpose_ticket = purpose_ticket;
	}

	public String getBelong_month() {
		return belong_month;
	}

	public void setBelong_month(String belong_month) {
		this.belong_month = belong_month;
	}

	public String getProduct_manager() {
		return product_manager;
	}

	public void setProduct_manager(String product_manager) {
		this.product_manager = product_manager;
	}

	public List<String> getMatch_flgs() {
		return match_flgs;
	}

	public void setMatch_flgs(List<String> match_flgs) {
		this.match_flgs = match_flgs;
	}

}
