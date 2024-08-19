package apptest;

import java.io.IOException;

import com.xinchi.common.DateUtil;

public class SomeTest {

	public static void main(String[] args) throws IOException {
		String a = "2024-09-19";
		System.out.println(DateUtil.addDate(a, 1));
	}
}
