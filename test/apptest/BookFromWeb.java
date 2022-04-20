package apptest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BookFromWeb {

	public static void main(String[] args) throws MalformedURLException, IOException {
		OutputStream out = new FileOutputStream(new File("D://金瓯缺.txt"));
		Random r = new Random();
		int first = r.nextInt(7) + 55;
		int second = r.nextInt(3200);
		int fouth = r.nextInt(140);
		String agent = MessageFormat.format(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/{0}.0.{1}.{2} Safari/537.36 SE 2.X MetaSr 1.0",
				first, second, fouth);
		Map<String, String> map = new HashMap<String, String>();

		for (int i = 1; i < 106; i++) {
			String url = "http://t.icesmall.cn/book/1/124/" + i + ".html";
			Document doc = Jsoup.connect(url).timeout(5000).data(map).ignoreContentType(true).userAgent(agent).get();

			Elements e = doc.getElementsByClass("post-title").get(0).getElementsByTag("td");
			out.write(e.get(0).html().getBytes());
			out.write(doc.getElementById("Content").html().replaceAll("<p>", "").replaceAll("</p>", "").getBytes());
			out.flush();
		}

		out.close();

	}

}
