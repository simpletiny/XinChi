package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class ReceivableSummaryBean extends SupperBO implements Serializable {
	private static final long serialVersionUID = -9077407262040429178L;

	private int all_count;

	private BigDecimal all_budget_balance = BigDecimal.ZERO;

	private BigDecimal all_final_balance = BigDecimal.ZERO;

	private BigDecimal all_balance = BigDecimal.ZERO;

	private int one_month_count;

	private BigDecimal one_month_budget_balance = BigDecimal.ZERO;

	private BigDecimal one_month_final_balance = BigDecimal.ZERO;

	private BigDecimal one_month_balance = BigDecimal.ZERO;

	private int two_month_count;

	private BigDecimal two_month_budget_balance = BigDecimal.ZERO;

	private BigDecimal two_month_final_balance = BigDecimal.ZERO;

	private BigDecimal two_month_balance = BigDecimal.ZERO;

	private int six_month_count;

	private BigDecimal six_month_budget_balance = BigDecimal.ZERO;

	private BigDecimal six_month_final_balance = BigDecimal.ZERO;

	private BigDecimal six_month_balance = BigDecimal.ZERO;

	private int bad_month_count;

	private BigDecimal bad_month_budget_balance = BigDecimal.ZERO;

	private BigDecimal bad_month_final_balance = BigDecimal.ZERO;

	private BigDecimal bad_month_balance = BigDecimal.ZERO;

	private int before_count;

	private BigDecimal before_budget_balance = BigDecimal.ZERO;

	private BigDecimal before_final_balance = BigDecimal.ZERO;

	private BigDecimal before_balance = BigDecimal.ZERO;

	public int getAll_count() {
		return all_count;
	}

	public void setAll_count(int all_count) {
		this.all_count = all_count;
	}

	public BigDecimal getAll_budget_balance() {
		return all_budget_balance;
	}

	public void setAll_budget_balance(BigDecimal all_budget_balance) {
		this.all_budget_balance = all_budget_balance;
	}

	public BigDecimal getAll_final_balance() {
		return all_final_balance;
	}

	public void setAll_final_balance(BigDecimal all_final_balance) {
		this.all_final_balance = all_final_balance;
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

	public BigDecimal getOne_month_budget_balance() {
		return one_month_budget_balance;
	}

	public void setOne_month_budget_balance(BigDecimal one_month_budget_balance) {
		this.one_month_budget_balance = one_month_budget_balance;
	}

	public BigDecimal getOne_month_final_balance() {
		return one_month_final_balance;
	}

	public void setOne_month_final_balance(BigDecimal one_month_final_balance) {
		this.one_month_final_balance = one_month_final_balance;
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

	public BigDecimal getTwo_month_budget_balance() {
		return two_month_budget_balance;
	}

	public void setTwo_month_budget_balance(BigDecimal two_month_budget_balance) {
		this.two_month_budget_balance = two_month_budget_balance;
	}

	public BigDecimal getTwo_month_final_balance() {
		return two_month_final_balance;
	}

	public void setTwo_month_final_balance(BigDecimal two_month_final_balance) {
		this.two_month_final_balance = two_month_final_balance;
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

	public BigDecimal getSix_month_budget_balance() {
		return six_month_budget_balance;
	}

	public void setSix_month_budget_balance(BigDecimal six_month_budget_balance) {
		this.six_month_budget_balance = six_month_budget_balance;
	}

	public BigDecimal getSix_month_final_balance() {
		return six_month_final_balance;
	}

	public void setSix_month_final_balance(BigDecimal six_month_final_balance) {
		this.six_month_final_balance = six_month_final_balance;
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

	public BigDecimal getBad_month_budget_balance() {
		return bad_month_budget_balance;
	}

	public void setBad_month_budget_balance(BigDecimal bad_month_budget_balance) {
		this.bad_month_budget_balance = bad_month_budget_balance;
	}

	public BigDecimal getBad_month_final_balance() {
		return bad_month_final_balance;
	}

	public void setBad_month_final_balance(BigDecimal bad_month_final_balance) {
		this.bad_month_final_balance = bad_month_final_balance;
	}

	public BigDecimal getBad_month_balance() {
		return bad_month_balance;
	}

	public void setBad_month_balance(BigDecimal bad_month_balance) {
		this.bad_month_balance = bad_month_balance;
	}

	public int getBefore_count() {
		return before_count;
	}

	public void setBefore_count(int before_count) {
		this.before_count = before_count;
	}

	public BigDecimal getBefore_budget_balance() {
		return before_budget_balance;
	}

	public void setBefore_budget_balance(BigDecimal before_budget_balance) {
		this.before_budget_balance = before_budget_balance;
	}

	public BigDecimal getBefore_final_balance() {
		return before_final_balance;
	}

	public void setBefore_final_balance(BigDecimal before_final_balance) {
		this.before_final_balance = before_final_balance;
	}

	public BigDecimal getBefore_balance() {
		return before_balance;
	}

	public void setBefore_balance(BigDecimal before_balance) {
		this.before_balance = before_balance;
	}

}
