package com.xinchi.backend.culture.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.culture.service.ValueViewService;
import com.xinchi.bean.ValueViewBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ValueViewAction extends BaseAction {
	private static final long serialVersionUID = -1721957811781950515L;

	@Autowired
	private ValueViewService service;

	private ValueViewBean view;

	public String createValueView() {
		service.insert(view);
		resultStr = OK;
		return SUCCESS;
	}

	private List<ValueViewBean> views;

	public String searchValueViewByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", view);

		page.setParams(params);

		views = service.getAllViewsByPage(page);
		return SUCCESS;
	}

	private String view_pk;

	public String searchOneValueView() {

		view = service.selectViewByPk(view_pk);
		return SUCCESS;
	}

	public String updateValueView() {
		service.update(view);
		resultStr = OK;
		return SUCCESS;
	}

	public String deleteValueView() {
		service.delete(view_pk);
		resultStr = OK;
		return SUCCESS;
	}

	public String getView_pk() {
		return view_pk;
	}

	public void setView_pk(String view_pk) {
		this.view_pk = view_pk;
	}

	public ValueViewBean getView() {
		return view;
	}

	public void setView(ValueViewBean view) {
		this.view = view;
	}

	public List<ValueViewBean> getViews() {
		return views;
	}

	public void setViews(List<ValueViewBean> views) {
		this.views = views;
	}
}