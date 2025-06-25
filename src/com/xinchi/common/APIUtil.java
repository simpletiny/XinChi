package com.xinchi.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.xinchi.backend.sys.service.IdentityService;
import com.xinchi.bean.IdentityBean;

import net.sf.json.JSONObject;

public class APIUtil {

	private static String ID_ILLEGAL = "illegal";
	private static String ID_MISMATCH = "mismatch";
	private static String ID_NO_RECORD = "no_record";
	private static String SUCCESS = "success";
	private static String FAIL = "fail";

	public static String checkIDFromAPI(String name, String id) {
		if (!Utils.validateIDCard(id)) {
			return ID_ILLEGAL;
		}
		IdentityService identityService = SpringContextUtil.getBean(IdentityService.class);
		// 检查本地数据库中的实名信息
		IdentityBean identity = identityService.selectById(id);
		if (null != identity) {
			return name.equals(identity.getName()) ? SUCCESS : ID_MISMATCH;
		}

		String host = "https://eid.shumaidata.com";
		String method = "POST";
		String path = "/eid/check";
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + ResourcesConstants.API_APP_CODE);
		// 根据API的要求，定义相对应的Content-Type
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		Map<String, String> params = new HashMap<>();
		params.put("idcard", id);
		params.put("name", name);
		try {
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, params);
			// 获取response的body
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			String resultStr = paraseIDResult(jsonStr);

			// 姓名身份信息一致
			if (resultStr.equals("1")) {
				// 保存信息到本地
				identity = new IdentityBean();
				identity.setName(name);
				identity.setId(id);
				identityService.insert(identity);
				return SUCCESS;
			}
			// 不一致
			else if (resultStr.equals("2")) {
				return ID_MISMATCH;
			}
			// 无记录
			else if (resultStr.equals("3")) {
				return ID_NO_RECORD;
			} else {
				return FAIL;
			}
		} catch (Exception e) {
			return FAIL;
		}
	}

	private static String paraseIDResult(String jsonStr) {
		JSONObject obj = JSONObject.fromObject(jsonStr);
		String response_code = obj.getString("code");
		if (response_code.equals("0")) {
			JSONObject info = obj.getJSONObject("result");
			String res = info.getString("res");
			return res;
		} else {
			return "4";
		}
	}

}
