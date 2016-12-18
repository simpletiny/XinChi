package com.xinchi.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xinchi.common.SupperBO;

public class PayableSummaryBean extends SupperBO implements Serializable {

	private static final long serialVersionUID = 3897439246292847469L;

	private String user_number;

	private String user_name;

	private int all_count;

	private BigDecimal all_budget_balance = BigDecimal.ZERO;

	private BigDecimal all_final_balance = BigDecimal.ZERO;

	private BigDecimal all_balance = BigDecimal.ZERO;

	private int current_month_count;

	private BigDecimal current_month_budget_balance = BigDecimal.ZERO;

	private BigDecimal current_month_final_balance = BigDecimal.ZERO;

	private BigDecimal current_month_balance = BigDecimal.ZERO;

	private int one_month_count;

	private BigDecimal one_month_budget_balance = BigDecimal.ZERO;

	private BigDecimal one_month_final_balance = BigDecimal.ZERO;

	private BigDecimal one_month_balance = BigDecimal.ZERO;

	private int two_month_count;

	private BigDecimal two_month_budget_balance = BigDecimal.ZERO;

	private BigDecimal two_month_final_balance = BigDecimal.ZERO;

	private BigDecimal two_month_balance = BigDecimal.ZERO;

	private int three_month_count;

	private BigDecimal three_month_budget_balance = BigDecimal.ZERO;

	private BigDecimal three_month_final_balance = BigDecimal.ZERO;

	private BigDecimal three_month_balance = BigDecimal.ZERO;

	private int earlier_month_count;

	private BigDecimal earlier_month_budget_balance = BigDecimal.ZERO;

	private BigDecimal earlier_month_final_balance = BigDecimal.ZERO;

	private BigDecimal earlier_month_balance = BigDecimal.ZERO;

	public String getUser_number() {
		return user_number;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

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

	public int getCurrent_month_count() {
		return current_month_count;
	}

	public void setCurrent_month_count(int current_month_count) {
		this.current_month_count = current_month_count;
	}

	public BigDecimal getCurrent_month_budget_balance() {
		return current_month_budget_balance;
	}

	public void setCurrent_month_budget_balance(BigDecimal current_month_budget_balance) {
		this.current_month_budget_balance = current_month_budget_balance;
	}

	public BigDecimal getCurrent_month_final_balance() {
		return current_month_final_balance;
	}

	public void setCurrent_month_final_balance(BigDecimal current_month_final_balance) {
		this.current_month_final_balance = current_month_final_balance;
	}

	public BigDecimal getCurrent_month_balance() {
		return current_month_balance;
	}

	public void setCurrent_month_balance(BigDecimal current_month_balance) {
		this.current_month_balance = current_month_balance;
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

	public int getThree_month_count() {
		return three_month_count;
	}

	public void setThree_month_count(int three_month_count) {
		this.three_month_count = three_month_count;
	}

	public BigDecimal getThree_month_budget_balance() {
		return three_month_budget_balance;
	}

	public void setThree_month_budget_balance(BigDecimal three_month_budget_balance) {
		this.three_month_budget_balance = three_month_budget_balance;
	}

	public BigDecimal getThree_month_final_balance() {
		return three_month_final_balance;
	}

	public void setThree_month_final_balance(BigDecimal three_month_final_balance) {
		this.three_month_final_balance = three_month_final_balance;
	}

	public BigDecimal getThree_month_balance() {
		return three_month_balance;
	}

	public void setThree_month_balance(BigDecimal three_month_balance) {
		this.three_month_balance = three_month_balance;
	}

	public int getEarlier_month_count() {
		return earlier_month_count;
	}

	public void setEarlier_month_count(int earlier_month_count) {
		this.earlier_month_count = earlier_month_count;
	}

	public BigDecimal getEarlier_month_budget_balance() {
		return earlier_month_budget_balance;
	}

	public void setEarlier_month_budget_balance(BigDecimal earlier_month_budget_balance) {
		this.earlier_month_budget_balance = earlier_month_budget_balance;
	}

	public BigDecimal getEarlier_month_final_balance() {
		return earlier_month_final_balance;
	}

	public void setEarlier_month_final_balance(BigDecimal earlier_month_final_balance) {
		this.earlier_month_final_balance = earlier_month_final_balance;
	}

	public BigDecimal getEarlier_month_balance() {
		return earlier_month_balance;
	}

	public void setEarlier_month_balance(BigDecimal earlier_month_balance) {
		this.earlier_month_balance = earlier_month_balance;
	}

}
