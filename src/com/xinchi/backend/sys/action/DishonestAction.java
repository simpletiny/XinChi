package com.xinchi.backend.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.sys.service.DishonestLogService;
import com.xinchi.backend.sys.service.DishonestPersonService;
import com.xinchi.backend.sys.service.IdentityService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.DishonestLogBean;
import com.xinchi.bean.DishonestPersonBean;
import com.xinchi.bean.IdentityBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.HttpUtils;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.Utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DishonestAction extends BaseAction {
	private static final long serialVersionUID = -8567000119275893144L;

	private DishonestPersonBean person;
	private DishonestPersonBean person_result;
	private List<DishonestLogBean> cases;
	@Autowired
	private DishonestPersonService dishonestPersonService;

	@Autowired
	private DishonestLogService dishonestLogService;

	@Autowired
	private IdentityService identityService;

	/**
	 * CODE: 200:成功。 501 官方数据源维护，请稍候再试。999 其他，以实际返回为准 其他，以实际返回为准 0: 成功 400: 参数错误
	 * 20010: 身份证号为空或非法 20310: 姓名为空或非法 404: 请求资源不存在 500: 系统内部错误，请联系服务商 501: 第三方服务异常
	 * 604: 接口停用 1001: 其他，以实际返回为准 201 姓名与身份证号不一致
	 * 
	 * @return
	 */
	public String checkIsDishonest() {
		// 查验身份证是否合法
		if (!Utils.validateIDCard(person.getId())) {
			person_result = new DishonestPersonBean();
			person_result.setCode("20010");
			person_result.setName(person.getName());
			person_result.setMsg("身份证号不合法。");
			return SUCCESS;
		}

		// 检查本地数据库中的实名信息
		IdentityBean identity = identityService.selectById(person.getId());
		if (null != identity) {
			if (!person.getName().equals(identity.getName())) {
				person_result = new DishonestPersonBean();
				person_result.setCode("201");
				person_result.setName(person.getName());
				person_result.setMsg("姓名与身份证号不一致。");
				return SUCCESS;
			}
		}
		// 调用接口查验身份信息是否一致
		else {
			String jsonStr = checkIDFromAPI(person);
			JSONObject obj = JSONObject.fromObject(jsonStr);
			String response_code = obj.getString("code");
			if (response_code.equals("0")) {
				JSONObject info = obj.getJSONObject("result");
				String res = info.getString("res");
				// 姓名身份信息一致
				if (res.equals("1")) {
					// 保存信息到本地
					identity = new IdentityBean();
					identity.setName(person.getName());
					identity.setId(person.getId());
					identityService.insert(identity);
				}
				// 不一致
				else if (res.equals("2")) {
					person_result = new DishonestPersonBean();
					person_result.setCode("201");
					person_result.setName(person.getName());
					person_result.setMsg("姓名与身份证号不一致。");
					return SUCCESS;
				}
				// 无记录
				else {

				}
			} else {
				String msg = obj.getString("message");
				person_result = new DishonestPersonBean();
				person_result.setCode(response_code);
				person_result.setName(person.getName());
				person_result.setMsg(msg);
				return SUCCESS;
			}
		}

		// 查询本地数据库，是否有数据
		person_result = dishonestPersonService.selectByPersonId(person.getId());
		if (null != person_result) {
			person_result.setCode("200");
			// 不管是不是失信人，都查询案件，有下架案件
			cases = dishonestLogService.selectByPersonId(person.getId());
			person_result.setCases(cases);
		} else {
			person_result = new DishonestPersonBean();
			String jsonStr = checkDishonestFromApi(person);
			JSONObject json = JSONObject.fromObject(jsonStr);
			int response_code = json.getInt("code");
			String response_msg = json.getString("msg");
			if (response_code == 200) {
				JSONObject data = JSONObject.fromObject(json.get("data"));
				int case_count = data.getInt("caseCount");
				if (case_count == 0) {
					person_result.setDishonest_flg("N");
				} else {
					cases = new ArrayList<>();
					JSONArray case_list = data.getJSONArray("caseList");
					Set<String> sign_flgs = new HashSet<>();
					for (int i = 0; i < case_list.size(); i++) {
						DishonestLogBean log = new DishonestLogBean();
						JSONObject current_case = JSONObject.fromObject(case_list.get(i));
						String case_code = current_case.getString("casecode");
						// 立案时间
						String reg_date = current_case.getString("regdate");
						// 发布时间
						String publish_date = current_case.getString("publishdate");
						// 是否下架
						String sign_flg = current_case.getString("sign").equals("0") ? "Y" : "N";
						sign_flgs.add(sign_flg);
						String signal_rating = current_case.getString("signalRating");
						String court_name = current_case.getString("courtname");

						log.setName(person.getName());
						log.setId(person.getId());
						log.setCase_code(case_code);
						log.setReg_date(reg_date);
						log.setPublish_date(publish_date);
						log.setSign_flg(sign_flg);
						log.setSignal_rating(signal_rating);
						log.setCourt_name(court_name);
						cases.add(log);

						// 保存log到本地数据库
						dishonestLogService.insert(log);
					}
					if (sign_flgs.contains("N")) {
						person_result.setDishonest_flg("Y");
					} else {
						person_result.setDishonest_flg("N");
					}
				}
				// 保存为非失信人到本地数据库
				person_result.setCode(String.valueOf(response_code));
				person_result.setMsg(response_msg);
				person_result.setName(person.getName());
				person_result.setId(person.getId());
				dishonestPersonService.insert(person_result);
				person_result.setCases(cases);
			} else {
				person_result.setCode(String.valueOf(response_code));
				person_result.setMsg(response_msg);
			}
		}
		return SUCCESS;
	}

	@Autowired
	private NumberService numberService;

	private String checkDishonestFromApi(DishonestPersonBean person) {
		String cellphone = numberService.generateFakePhoneNumber();
		String host = "https://jumjokk.market.alicloudapi.com";
		String path = "/personal/disenforcement";
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

	private String checkIDFromAPI(DishonestPersonBean person) {
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
		params.put("idcard", person.getId());
		params.put("name", person.getName());
		try {
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, params);
			// 获取response的body
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			return jsonStr;
		} catch (Exception e) {
			return "none";
		}
	}

	public DishonestPersonBean getPerson() {
		return person;
	}

	public void setPerson(DishonestPersonBean person) {
		this.person = person;
	}

	public DishonestPersonBean getPerson_result() {
		return person_result;
	}

	public List<DishonestLogBean> getCases() {
		return cases;
	}

	public void setPerson_result(DishonestPersonBean person_result) {
		this.person_result = person_result;
	}

	public void setCases(List<DishonestLogBean> cases) {
		this.cases = cases;
	}
}