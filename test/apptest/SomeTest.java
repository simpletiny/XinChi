package apptest;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.SimpleOrderedMap;

import com.google.common.base.Joiner;

public class SomeTest {
	public static String source = "GT9RXPJIUHF8EQ34YLNV6MB1WS052OCDAZK7";
	public static String solr_url = "http://localhost:8983/solr/receivable";

	public static void main(String[] args) throws SolrServerException,
			IOException {
		// System.out.println(DateUtil.dateDiff("2016-11-06", "2016-11-07"));

		List<String> queryParts = new ArrayList<String>();

		queryParts.add("_text_:\"" + "map" + "\"");
		String str = Joiner.on("and").join(queryParts);

		// SolrClient solr = new HttpSolrClient.Builder(solr_url).build();
		SolrClient solr = new HttpSolrClient(solr_url);
		// 计算合计
		SolrQuery query = new SolrQuery("*:*");
		query.add("stats", "true");
		query.add("stats.field", "budget_receivable");

		// query.add("id:test");
		QueryResponse response = solr.query(query);
		System.out.println(response);
		SimpleOrderedMap<SimpleOrderedMap<SimpleOrderedMap>> map = (SimpleOrderedMap) response
				.getResponse().get("stats");
		System.out.println(map.get("stats_fields").get("budget_receivable")
				.get("sum"));
		SolrDocumentList list = response.getResults();

		for (SolrDocument doc : list) {

			System.out.println(doc.get("id"));

		}

		SolrInputDocument document = new SolrInputDocument();

		// document.addField("id", "1");
		// document.addField("name", "牛世行");
		// document.addField("sex", "map飞");
		// document.addField("age", "马云");
		// UpdateResponse response1 = solr.add(document);

		// solr.deleteById("1");
		// solr.deleteById("xxx");

		// solr.commit();
	}

	/**
	 * 字符串MD5加密100次
	 * 
	 * @param strSource
	 * @return 加密后的密文
	 */
	public static String MD5(String strSource) {
		try {
			for (int x = 0; x < 100; x++) {
				strSource = MD5OneTime(strSource);
			}
			return strSource;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * md5执行一次
	 * 
	 * @param strSource
	 * @return
	 */
	public static String MD5OneTime(String strSource) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {

			byte[] strTemp = strSource.getBytes();
			// 使用MD5创建MessageDigest对象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				// 将没个数(int)b进行双字节加密
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			strSource = String.valueOf(str);

			return strSource;
		} catch (Exception e) {
			return null;
		}
	}
}
