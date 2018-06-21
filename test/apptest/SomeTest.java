package apptest;

import java.util.Arrays;

import com.xinchi.common.ToolsUtil;

public class SomeTest {

	// public static void main(String[] args) throws Exception {
	// String file = "F://test//newss.docx";
	// Map<String, String> data = new HashMap<String, String>();
	// data.put("Test", "你好豪华车带赛");
	// OutputStream os = new FileOutputStream(new File("F://test//niu.docx"));
	// SimpletinyWord.writeWord(file, data, os);
	// }

	public static void main(String[] args) throws Exception {
		 String[] exceptions = {"name","age"};
	
		Student s1 = new Student();
		s1.setName("牛世行");
		s1.setAge("");
		s1.setScores(Arrays.asList(exceptions));
		s1.setSex("");

		Student s2 = new Student();
		s2.setName("牛世行2");
		s2.setAge("32");
		s2.setScores(Arrays.asList(exceptions));
		s2.setSex("女");
		
	 ToolsUtil.combineObject(s1, s2, Arrays.asList(exceptions));
		
		System.out.println(s1.getName()+","+s1.getAge()+","+s1.getSex()+","+s1.getScores());
	}
}
