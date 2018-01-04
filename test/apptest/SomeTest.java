package apptest;

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
		String[] bsdw = {"支付宝9544","工行哈市7479","交行哈市9999","建行哈市9028","中行哈市8471","农行哈市6919","邮储哈市5906","微信9544","公司主账户0880","交行倒账363","建行金卡7265","中行原卡"};
		
		for(int i = bsdw.length-1;i>=0;i--) {
			System.out.print("'"+bsdw[i]+"',");
		}
		
	}
}
