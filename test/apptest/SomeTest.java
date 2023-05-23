package apptest;

import java.util.Arrays;

public class SomeTest {
	public static String source_team_number = "GT9RXPJIUHF8EQ34YLNV6MB1WS052OCDAZK7";

	public static void main(String[] args) throws Exception {
		String a = "sswComment";
		System.out.println(a.contains("Comment"));
	}

	private static int add(int a, int b, int... c) {
		int result = a + b;
		if (c != null && c.length > 0) {
			for (int i = 0; i < c.length; i++) {
				result += c[i];
			}
		}
		return result;
	}

	private static String addOne(String value, String source) {

		int strLength = value.length();
		String last = value.substring(strLength - 1);
		String first = value.substring(0, strLength - 1);
		int nextCharIndex = source.indexOf(last) + 1;
		if (nextCharIndex >= 36) {
			String result = addOne(first, source) + source.charAt(0);
			char[] max = new char[4];
			Arrays.fill(max, source.charAt(0));
			if (result.equals(String.valueOf(max))) {
				return "YOU ARE RICH!";
			} else {
				return result;
			}
		} else {
			return first + source.charAt(nextCharIndex);
		}
	}

	public static String[] doGenerateNumber(String prefix, String source, int len) {
		String result[] = new String[len];

		// String user_pk = "dHV3eXJ8fHl4gHF3d3p4fQ";
		for (int i = 0; i < len; i++) {
			if (i == 0) {
				result[i] = "D00" + source.substring(0, 4);
			} else {
				result[i] = addOne(result[i - 1], source);
			}

		}
		return result;
	}

	public static String doIndex(int index, int now) {
		String result = "";
		int mod = (now + index) % 36;
		int z = ((now + index)) / 36;

		String newC = String.valueOf(source_team_number.charAt(mod));
		if (z > 0) {
			now--;
			result = doIndex(z, now) + newC;
		} else {
			result = source_team_number.substring(0, now) + newC;
		}
		return result;
	}

	public static String doIndex2(int decimal, int len) {
		StringBuilder result = new StringBuilder();
		while (decimal > 0) {
			int remainder = decimal % 36;
			result.insert(0, source_team_number.charAt(remainder));
			decimal /= 36;
		}
		if (result.length() == 2) {
			result.insert(0, "GT");
			return result.toString();
		}

		if (result.length() < len) {
			char[] zero = new char[len - result.length()];
			Arrays.fill(zero, source_team_number.charAt(0));
			result.insert(0, zero);
		} else if (result.length() == len) {
			return result.toString();
		} else {
			return "YOU ARE RICH";
		}

		return result.toString();
	}

}
