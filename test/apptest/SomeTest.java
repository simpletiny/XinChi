package apptest;

import java.util.List;
import java.util.Map;

import com.xinchi.common.DateUtil;

public class SomeTest {

	public static void replacePlaceholdersWithContent(String filePath,
			Map<String, List<String>> placeholdersWithContents) throws Exception {

	}

	public static void main(String[] args) throws Exception {
		String b = "1606724208441";
		String a = "1606724208443";

		System.out.println(Long.parseLong(DateUtil.getTimeMillis("2024-06-01")) > Long.parseLong(b));

	}
}
