package apptest;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.xinchi.bean.DishonestPersonBean;
import com.xinchi.common.HttpUtils;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.XMLUtils;

public class SomeTest {

	public static void main(String[] args) throws Exception {
		String a = "<xml><ToUserName><![CDATA[wwa7854bfa8dde7156]]></ToUserName><CreateTime>1780990771</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[kf_msg_or_event]]></Event><Token><![CDATA[ENCHLjLoLz18yvie2km9QB1P2jPwxMEes6T8b7xDePTxgcR]]></Token><OpenKfId><![CDATA[wkOwxqCwAAOcACUvFecbyyusPQB3KU8g]]></OpenKfId></xml>\r\n";

		Map<String, String> c = XMLUtils.toMap(a);

		String url = "https://qyapi.weixin.qq.com/cgi-bin/kf/sync_msg?access_token=rUMCcg-n33v89wE8CJeU81qz-021rxZseEF6SwfLJdgO8UjGJRLqI8GJ9vyi37QpaNd8LZsmD8itE0U3ZLFv1pQ1gLEC2GWQX-dr0oAj2LVOVbjuPjxLUNcqzmXQqXv_gdoy8j49owTcNGml6GqYXzqTSKZ955UJBa34FYxJAGEevKe1vkcT5W1pXo3-6iFvf1D59seWPRI__58KicKu1g";

		JSONObject body = new JSONObject();
		body.put("token", c.get("Token"));
		body.put("open_kfid", c.get("OpenKfId"));
		body.put("limit", 1000); // 最大1000条
		String cursor = "";
		if (cursor != null && !cursor.isEmpty()) {
			body.put("cursor", cursor);
		}

		// voice_format: 0 表示不返回语音转文字，1 表示返回
		body.put("voice_format", 0);

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost request1 = new HttpPost(url);
			request1.setHeader("Content-Type", "application/json");
			request1.setEntity(new StringEntity(body.toJSONString(), "UTF-8"));

			org.apache.http.HttpResponse response = httpClient.execute(request1);
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String checkHighFromApi(DishonestPersonBean person) {
		String cellphone = "13147470888";
		String host = "https://jumfrite.market.alicloudapi.com";
		String path = "/personal/limit-high-consumption";
		String method = "POST";
		String appcode = ResourcesConstants.API_APP_CODE;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		// 根据API的要求，定义相对应的Content-Type
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		Map<String, String> bodys = new HashMap<String, String>();
		bodys.put("idcard_number", person.getId());
		bodys.put("mobile_number", cellphone);
		bodys.put("name", person.getName());

		try {
			/**
			 * 重要提示如下: HttpUtils请从
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
			 * 下载
			 *
			 * 相应的依赖请参照
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
			 */
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
			// 获取response的body
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			return jsonStr;
		} catch (Exception e) {
			return "none";
		}

	}

	// 验证身份证号
	public static boolean isValidSFZ(String id) {
		if (id == null || id.length() != 18) {
			return false;
		}

		// 加权因子数组（2 的幂 mod 11）
		int[] weights = new int[17];
		for (int i = 0; i < 17; i++) {
			weights[i] = (int) (Math.pow(2, 17 - i) % 11);
		}

		// 校验位对照表
		String[] checkDigits = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

		int sum = 0;
		for (int i = 0; i < 17; i++) {
			char ch = id.charAt(i);
			if (!Character.isDigit(ch)) {
				return false;
			}
			int digit = Character.getNumericValue(ch);
			sum += digit * weights[i];
		}

		int mod = sum % 11;
		String expectedCheckDigit = checkDigits[mod];

		String actualCheckDigit = id.substring(17).toUpperCase();

		return expectedCheckDigit.equals(actualCheckDigit);
	}
}
