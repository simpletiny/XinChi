package apptest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

	public static void main(String[] args) throws IOException {
		File folder = new File("C:\\Users\\simpl\\Desktop\\mod\\test");
		rename(folder,"grass","iron_grass");
	}

	public static void rename(File f, String src, String dest) {
		if (f.isDirectory()) {
			List<File> files = Arrays.asList(f.listFiles());
			for (File f1 : files) {
				rename(f1, src, dest);
			}
		}
		String parent = f.getParent();
		String oldName = f.getName();
		if (oldName.indexOf(src) >= 0) {
			f.renameTo(new File(parent + File.separator + oldName.replaceAll(src, dest)));
		}

	}
}