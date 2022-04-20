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

	private String apply_date;

	private String pay_user;

	private String pay_user_pk;

	private String from_where;

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

}
