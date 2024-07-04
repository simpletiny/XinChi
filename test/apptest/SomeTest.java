package apptest;

import java.io.IOException;

import net.sf.json.JSONObject;

public class SomeTest {

	public static void main(String[] args) throws IOException {
		String jsonString = "{\"code\":\"0\",\"message\":\"成功\",\"result\":{\"name\":\"牛世行\",\"idcard\":\"130123198703222714\",\"res\":\"1\",\"description\":\"一致\",\"sex\":\"男\",\"birthday\":\"19870322\",\"address\":\"河北省石家庄市正定县\"}}";
		// "res": "1",核验结果状态码，1 一致；2 不一致；3 无记录
		JSONObject obj = JSONObject.fromObject(jsonString);
		String response_code = obj.getString("code");
		if (response_code.equals("0")) {
			JSONObject info = obj.getJSONObject("result");
			System.out.println(info.getString("res"));
		} else {

		}
	}
}
