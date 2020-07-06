package apptest;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
		Set<String> aa = new HashSet<String>();

		aa.add("a");
		aa.add("a");
		aa.add("b");
		aa.add("a");
		aa.add("c");
		Iterator b = aa.iterator();
		while (b.hasNext()) {
			System.out.println(b.next());
		}

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
