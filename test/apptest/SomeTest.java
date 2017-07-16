package apptest;

public class SomeTest {
	public static String source = "GT9RXPJIUHF8EQ34YLNV6MB1WS052OCDAZK7";
	public static String firstNumber = "GT9R";

	public static void main(String[] args) {
		System.out.println(getByIndex(1155322));
		System.out.println(getByIndex(35));
	}

	public static String getByIndex(int index) {
		return doIndex(index, 3);
	}

	private static String doIndex(int index, int now) {
		String result = "";
		int mod = (now + index) % 36;
		int z = ((now + index)) / 36;
		String newC = String.valueOf(source.charAt(mod));
		if (z > 0) {
			now--;
			result = doIndex(z, now) + newC;
		} else {
			result = source.substring(0, now) + newC;
		}
		return result;
	}

	public static String addOne(String value) {
		if (null == value || value.equals(""))
			return "";
		int strLength = value.length();
		String last = value.substring(strLength - 1);
		String first = value.substring(0, strLength - 1);
		int nextCharIndex = source.indexOf(last) + 1;
		if (nextCharIndex >= 36) {
			String result = addOne(first) + "G";
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
