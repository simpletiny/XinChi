package apptest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Administrator 通过aop拦截后执行具体操作
 */
@Aspect
@Component
public class SomeTest {
	private static final int BUFFER_SIZE = 2 * 1024;

	public static void main(String[] args) throws Exception {
		// 打开带读取的文件
		BufferedReader br = new BufferedReader(new FileReader("D://test.txt"));
		String line = null;
		String pattern1 = "<div class=\"P_Text\">([0-9]{1,4})、(.*)(</span>|</div>)";
		String pattern2 = "<input type=\"hidden\" id=\"[RMB]{1}Answer([0-9]{1,4})\" value=\"([A-Z]{1,6})\">";
		String pattern3 = "<label for=\"[rmb]{2}([0-9]{1,4}).*</span>([A-F]{1})、(.*)</div></label>";
		Pattern r = Pattern.compile(pattern1);
		Pattern x = Pattern.compile(pattern2);
		Pattern o = Pattern.compile(pattern3);

		BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("d:\\final2.txt"), "UTF-8"));
		int i = 1;
		Map<Integer, String> result = new HashMap<Integer, String>();

		while ((line = br.readLine()) != null) {
			// System.out.println(line);
			Matcher m = o.matcher(line);
			if (m.find()) {

				if (Integer.valueOf(m.group(1)) != i) {
					i++;
				}
				result.put(i, (null == result.get(i) ? "" : result.get(i)) + m.group(2) + ":" + m.group(3) + "@@@");
			}
			// System.out.println(line);
			// Matcher m = r.matcher(line);
			// if (m.find()) {
			// out.write(m.group(1) + "@@@" + m.group(2) + "\n");
			// }

			// Matcher m1 = x.matcher(line);
			// if (m1.find()) {
			// out.write(m1.group(1) + "@@@" + m1.group(2) + "\n");
			// }
		}

		for (int key : result.keySet()) {
			out.write(key + "^^^" + result.get(key) + "\n");
		}
		out.flush();
		out.close();
		br.close();//
	}

	public static int countLine(File f, int n) throws Exception {
		if (f.isFile()) {
			String fn = f.getName();
			if (fn.indexOf("java") >= 0 || fn.indexOf("jsp") >= 0 || fn.indexOf("js") >= 0 || fn.indexOf("xml") >= 0
					|| fn.indexOf("properties") >= 0 || fn.indexOf("css") >= 0) {
				LineNumberReader lnr = new LineNumberReader(new FileReader(f));
				lnr.skip(Long.MAX_VALUE);
				n += lnr.getLineNumber() + 1;
				lnr.close();
			}
		} else {
			File[] files = f.listFiles();
			for (File file : files) {
				n += countLine(file, n);
			}
		}

		return n;

	}

}
