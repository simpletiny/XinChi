package apptest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.SystemOutLogger;

import com.xinchi.bean.DishonestPersonBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.HttpUtils;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;

public class SomeTest {

	public static void main(String[] args) throws IOException {
		String a = "客户：丁宇柔;财务主体：公开;\n 客户：丁宁;财务主体：公开; 222222";
		String regex = "客户：\\s*[^;]+;\\s*财务主体：\\s*[^;]+;\\s*";
		
		System.out.println(SimpletinyString.replaceByRegex(a, regex));
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
