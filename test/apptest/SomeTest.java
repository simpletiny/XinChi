package apptest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SomeTest {
	public static void main(String[] args) {
		String test = "<p>"
				+ "<img src=\"/XinChi/file/getFileStream?fileType=SYSTEM_GUIDE_FILE&subFolder=image&fileFileName=dHV-cnp6fHF3f3J0c392cg.jpg\" width=\"400\" height=\"300\" alt=\"\" /><img src=\"/XinChi/file/getFileStream?fileType=SYSTEM_GUIDE_FILE&subFolder=image&fileFileName=cXZ1fnuAfHh2doB8f3Z6gA.jpg\" width=\"500\" height=\"375\" title=\"还不知道\" alt=\"还不知道\" /> "
				+ " </p>" + " <p>" + "你<a href=\"http://www.baidu.com\" target=\"_blank\">是收拾</a> " + " </p>";
		String imgEx = "<img (src=\"/XinChi/file/getFileStream\\?)(.*?)\".*? \\/\\>";
		// 编译正则表达式
		Pattern imgPattern = Pattern.compile(imgEx);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = imgPattern.matcher(test);
		List<String> imgs = new ArrayList<String>();

		while (matcher.find()) {
			imgs.add(matcher.group(2));
		}

		List<Map<String, String>> deletes = new ArrayList<Map<String, String>>();
		for (String img : imgs) {
			String[] properties = img.split("&");
			Map<String, String> map = new HashMap<String, String>();
			for (String property : properties) {

				String[] names = property.split("=");
				if (names.length > 1) {
					map.put(names[0], names[1]);
				}
			}
			deletes.add(map);
		}

		for (Map<String, String> map : deletes) {
			for (String key : map.keySet()) {
				System.out.println(key+":"+map.get(key));
			}
		}
	}
}
