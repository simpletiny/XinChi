package apptest;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

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
		String a = "B^^^D^^^A^^^B^^^D^^^B^^^A^^^B^^^C^^^C^^^D^^^A^^^C^^^A^^^A^^^A^^^A^^^B^^^C^^^B^^^A^^^A^^^B^^^C^^^A^^^C^^^D^^^C^^^D^^^A^^^D^^^D^^^D^^^D^^^A^^^A^^^A^^^ABCD^^^ABC^^^ABCD^^^AB^^^ABCD^^^ABCD^^^ABCD^^^ABC^^^ABD^^^ABCD^^^AC^^^ABC^^^ABCD^^^ABCD^^^ACD^^^ABC^^^AB^^^AD^^^ACD^^^AC^^^ABC^^^ABCD^^^ABCD^^^ABCD^^^AB^^^ABCD^^^ABD^^^ABD^^^ABD^^^B^^^B^^^A^^^A^^^A^^^B^^^B^^^A^^^A^^^A^^^B^^^A^^^B^^^A^^^A^^^A^^^B^^^B^^^B^^^B^^^A^^^A^^^A^^^A^^^A^^^A^^^B^^^B^^^B^^^A^^^B^^^B^^^A^^^A^^^";

		String b[] = a.split("\\^\\^\\^");
		int i = 1;
		for (String x : b) {

			System.out.println(i + ":" + x);
			i++;
		}

		// System.out.println(DateUtil.getTimeMillis());
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
