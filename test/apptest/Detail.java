package apptest;

import java.math.BigDecimal;

public class Detail {

	private int first;
	private int last;
	private int days;
	private String time;
	private BigDecimal money;

	public String getTime() {
		return time;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public int getFirst() {
		return first;
	}

	public int getLast() {
		return last;
	}

	public int getDays() {
		return days;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public void setLast(int last) {
		this.last = last;
	}

	public void setDays(int days) {
		this.days = days;
	}
}
