package apptest;

import com.xinchi.common.DBCommonUtil;

public class SomeTest {

	public static void main(String[] args) throws Exception {
		String a = DBCommonUtil.genPk();
		System.out.println(a);
	}

}
