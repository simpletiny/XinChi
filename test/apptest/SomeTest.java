package apptest;

import java.util.Calendar;
import java.util.Random;

public class SomeTest {
	public static String source = "GT9RXPJIUHF8EQ34YLNV6MB1WS052OCDAZK7";

	public static void main(String[] args) {

		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(1478309959968L);
		c.set(2016, 10, 9, 0, 0, 0);
//		System.out.println(c.getTime());
		System.out.println(String.valueOf(c.getTimeInMillis()));
		String.valueOf(c.getTimeInMillis());
	}

	public static String add(String value) {
		if (null == value || value.equals(""))
			return "";
		int strLength = value.length();
		String last = value.substring(strLength - 1);
		String first = value.substring(0, strLength - 1);
		int nextCharIndex = source.indexOf(last) + 1;
		if (nextCharIndex >= 36) {
			String result = add(first) + "G";
			if (result.equals("GGGG")) {
				return "YOU ARE RICH!";
			} else {
				return result;
			}
		} else {
			return first + source.charAt(source.indexOf(last) + 1);
		}
	}
}
