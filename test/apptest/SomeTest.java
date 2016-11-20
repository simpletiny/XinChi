package apptest;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;

import com.google.common.base.Joiner;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;

public class SomeTest {
	public static String source = "GT9RXPJIUHF8EQ34YLNV6MB1WS052OCDAZK7";
	public static String solr_url = "http://localhost:8983/solr/receivable";

	public static void main(String[] args) throws SolrServerException,
			IOException {
		String[] a = {"1","3","5","6"};
		System.out.println(Joiner.on(",").join(a));
	}

	private static String buildQuery(ReceivableBean options) {
		String separator = " AND ";
		List<String> queryParts = new ArrayList<String>();

		if (!SimpletinyString.isEmpty(options.getClient_employee_name())) {
			queryParts.add("client_employee_name:\""
					+ options.getClient_employee_name() + "\"");
		}

		if (!SimpletinyString.isEmpty(options.getSales_name())) {
			queryParts.add("sales_name:\"" + options.getSales_name() + "\"");
		}

		String team_status = options.getTeam_status();
		String from = "";
		String to = "";
		if (!SimpletinyString.isEmpty(team_status)) {
			if (team_status.equals(ResourcesConstants.TEAM_STATUS_BEFORE)) {
				from = DateUtil.getUTC();
				to = "*";
				queryParts.add("departure_date:[" + from + " TO " + to + "]");
			} else if (team_status.equals(ResourcesConstants.TEAM_STATUS_AFTER)) {
				from = "*";
				to = DateUtil.getUTC();
				queryParts.add("departure_date:[" + from + " TO " + to + "]");
			} else if (team_status
					.equals(ResourcesConstants.TEAM_STATUS_RETURN)) {
				from = "*";
				to = DateUtil.getUTC();
				queryParts.add("return_date:[" + from + " TO " + to + "]");
			}
		}

		String departure_month = options.getDeparture_month();
		if (!SimpletinyString.isEmpty(departure_month)) {
			from = DateUtil.getUTC(departure_month + "-01");
			to =DateUtil.getUTC( DateUtil.getLastDay(departure_month));
			queryParts.add("departure_date:[" + from + " TO " + to + "]");
		}
		return Joiner.on(separator).join(queryParts);
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

	private String safeGet(SolrDocument doc, String key) {
		return doc.get(key) != null ? doc.get(key).toString() : null;
	}
}
