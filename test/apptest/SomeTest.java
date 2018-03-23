package apptest;

import java.math.BigDecimal;

public class SomeTest {

	// public static void main(String[] args) throws Exception {
	// String file = "F://test//newss.docx";
	// Map<String, String> data = new HashMap<String, String>();
	// data.put("Test", "你好豪华车带赛");
	// OutputStream os = new FileOutputStream(new File("F://test//niu.docx"));
	// SimpletinyWord.writeWord(file, data, os);
	// }

	public static void main(String[] args) throws Exception {
		BigDecimal a = new BigDecimal(1);
		BigDecimal b = new BigDecimal(3).divide(new BigDecimal(100));
		BigDecimal result = a.divide(b,0,BigDecimal.ROUND_HALF_UP);
		System.out.println(result.floatValue());
	}
}
