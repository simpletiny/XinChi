package com.xinchi.backend.sys.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.sys.service.SystemGuideService;
import com.xinchi.bean.SystemGuideBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SystemGuideAction extends BaseAction {
	private static final long serialVersionUID = -9117357769836495933L;

	@Autowired
	private SystemGuideService service;

	private SystemGuideBean view;

	public String createSystemGuide() {
		service.insert(view);
		resultStr = OK;
		return SUCCESS;
	}

	private List<SystemGuideBean> views;

	public String searchSystemGuideByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", view);

		page.setParams(params);

		views = service.getAllViewsByPage(page);
		return SUCCESS;
	}

	private String view_pk;

	public String searchOneSystemGuide() {

		view = service.selectViewByPk(view_pk);
		return SUCCESS;
	}

	public String updateSystemGuide() {
		service.update(view);
		resultStr = OK;
		return SUCCESS;
	}

	public String deleteSystemGuide() {
		service.delete(view_pk);
		resultStr = OK;
		return SUCCESS;
	}

	public SystemGuideBean getView() {
		return view;
	}

	public void setView(SystemGuideBean view) {
		this.view = view;
	}

	public List<SystemGuideBean> getViews() {
		return views;
	}

	public void setViews(List<SystemGuideBean> views) {
		this.views = views;
	}

	public String getView_pk() {
		return view_pk;
	}

	public void setView_pk(String view_pk) {
		this.view_pk = view_pk;
	}
}