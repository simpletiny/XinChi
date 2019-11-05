package com.xinchi.backend.sys.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.sys.service.BaseDataService;
import com.xinchi.bean.BaseDataBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BaseDataAction extends BaseAction {
	private static final long serialVersionUID = 7325379106991169911L;

	@Autowired
	private BaseDataService service;

	private String type;

	private String level;

	private String father_level_pk;

	private List<BaseDataBean> datas;

	private BaseDataBean baseData;

	public String searchByType() {

		BaseDataBean option = new BaseDataBean();

		option.setType(type);
		if (SimpletinyString.isEmpty(level))
			level = "1";
		option.setLevel(Integer.valueOf(level));

		datas = service.selectByParam(option);

		return SUCCESS;
	}

	public String searchByFatherPk() {
		datas = service.selectByFatherLevelPk(father_level_pk);
		return SUCCESS;
	}

	public String createBaseData() {

		resultStr = service.createBaseData(baseData);
		return SUCCESS;
	}

	public String updateBaseData() {
		resultStr = service.updateBaseData(baseData);
		return SUCCESS;
	}
	public String deleteBaseData() {
		resultStr = service.deleteBaseData(baseData);
		return SUCCESS;
	}

	private String json;

	public String sortProductLine() {
		resultStr = service.sortData(json);
		return SUCCESS;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<BaseDataBean> getDatas() {
		return datas;
	}

	public void setDatas(List<BaseDataBean> datas) {
		this.datas = datas;
	}

	public String getFather_level_pk() {
		return father_level_pk;
	}

	public void setFather_level_pk(String father_level_pk) {
		this.father_level_pk = father_level_pk;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public BaseDataBean getBaseData() {
		return baseData;
	}

	public void setBaseData(BaseDataBean baseData) {
		this.baseData = baseData;
	}

}