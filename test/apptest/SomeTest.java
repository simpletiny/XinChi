package apptest;

import java.math.BigDecimal;

public class SomeTest {

	public static void main(String[] args) throws Exception {
		BigDecimal a = new BigDecimal("1425");
		BigDecimal b = new BigDecimal("0.98");
		BigDecimal discount_receivable = a.multiply(b);
		System.out.println(discount_receivable);
		discount_receivable = discount_receivable.setScale(0, BigDecimal.ROUND_DOWN);
		System.out.println(discount_receivable);
	}

}
