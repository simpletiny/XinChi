package apptest;

import java.util.ArrayList;
import java.util.List;

public class SomeTest {

	// public static void main(String[] args) throws Exception {
	// String file = "F://test//newss.docx";
	// Map<String, String> data = new HashMap<String, String>();
	// data.put("Test", "你好豪华车带赛");
	// OutputStream os = new FileOutputStream(new File("F://test//niu.docx"));
	// SimpletinyWord.writeWord(file, data, os);
	// }

	public static void main(String[] args) throws Exception {
		List<String> a = new ArrayList<String>();
		
		a.add("1");
		a.add("2");
		a.add("1");
		a.remove("3");
		a.remove("1");
		
		for(String c:a) {
			System.out.println(c);
		}
	}
}
