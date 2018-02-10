package apptest;

import java.util.Calendar;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class SomeTest {

	// public static void main(String[] args) throws Exception {
	// String file = "F://test//newss.docx";
	// Map<String, String> data = new HashMap<String, String>();
	// data.put("Test", "你好豪华车带赛");
	// OutputStream os = new FileOutputStream(new File("F://test//niu.docx"));
	// SimpletinyWord.writeWord(file, data, os);
	// }

	public static void main(String[] args) throws Exception {
		String x = "2015-09-09";
		System.out.println(x.substring(0, 4) + ":" + x.substring(5, 7));
	}
}
