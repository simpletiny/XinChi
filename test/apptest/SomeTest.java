package apptest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xinchi.common.SimpletinyString;

public class SomeTest {

	public static void replacePlaceholdersWithContent(String filePath,
			Map<String, List<String>> placeholdersWithContents) throws Exception {

	}

	public static void main(String[] args) throws Exception {
		String b = "";
		BigDecimal a = new BigDecimal(SimpletinyString.isEmpty(b) ? "1" : b);
		System.out.println(a);

	}
}
