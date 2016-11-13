package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

/**
 * receivable summary for page receivable.jsp
 * 
 * @author simpletiny recsum
 */
public class ReceivableSummary extends SupperBO implements Serializable {
	private static final long serialVersionUID = -6594429738956829264L;

	private int all_count;
	private BigDecimal all_balance;

	private int one_month_count;
	private BigDecimal one_month_balance;

	private int two_month_count;
	private BigDecimal two_month_balance;

	private int six_month_count;
	private BigDecimal six_month_balance;

	// longer than 6 month
	private int bad_month_count;
	private BigDecimal bad_month_balance;

	public int getAll_count() {
		return all_count;
	}

	public void setAll_count(int all_count) {
		this.all_count = all_count;
	}

	public BigDecimal getAll_balance() {
		return all_balance;
	}

	public void setAll_balance(BigDecimal all_balance) {
		this.all_balance = all_balance;
	}

	public int getOne_month_count() {
		return one_month_count;
	}

	public void setOne_month_count(int one_month_count) {
		this.one_month_count = one_month_count;
	}

	public BigDecimal getOne_month_balance() {
		return one_month_balance;
	}

	public void setOne_month_balance(BigDecimal one_month_balance) {
		this.one_month_balance = one_month_balance;
	}

	public int getTwo_month_count() {
		return two_month_count;
	}

	public void setTwo_month_count(int two_month_count) {
		this.two_month_count = two_month_count;
	}

	public BigDecimal getTwo_month_balance() {
		return two_month_balance;
	}

	public void setTwo_month_balance(BigDecimal two_month_balance) {
		this.two_month_balance = two_month_balance;
	}

	public int getSix_month_count() {
		return six_month_count;
	}

	public void setSix_month_count(int six_month_count) {
		this.six_month_count = six_month_count;
	}

	public BigDecimal getSix_month_balance() {
		return six_month_balance;
	}

	public void setSix_month_balance(BigDecimal six_month_balance) {
		this.six_month_balance = six_month_balance;
	}

	public int getBad_month_count() {
		return bad_month_count;
	}

	public void setBad_month_count(int bad_month_count) {
		this.bad_month_count = bad_month_count;
	}

	public BigDecimal getBad_month_balance() {
		return bad_month_balance;
	}

	public void setBad_month_balance(BigDecimal bad_month_balance) {
		this.bad_month_balance = bad_month_balance;
	}

}
