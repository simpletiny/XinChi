package com.xinchi.backend.culture.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.culture.service.WorldViewService;
import com.xinchi.bean.WorldViewBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WorldViewAction extends BaseAction {
	private static final long serialVersionUID = -1721957811781950515L;

	@Autowired
	private WorldViewService service;

	private WorldViewBean view;

	public String createWorldView() {
		service.insert(view);
		resultStr = OK;
		return SUCCESS;
	}

	private List<WorldViewBean> views;

	public String searchWorldViewByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", view);

		page.setParams(params);

		views = service.getAllViewsByPage(page);
		return SUCCESS;
	}

	private String view_pk;

	public String searchOneWorldView() {

		view = service.selectViewByPk(view_pk);
		return SUCCESS;
	}

	public String updateWorldView() {
		service.update(view);
		resultStr = OK;
		return SUCCESS;
	}
	
	public String deleteWorldView() {
		service.delete(view_pk);
		resultStr = OK;
		return SUCCESS;
	}
	
	
	public WorldViewBean getView() {
		return view;
	}

	public void setView(WorldViewBean view) {
		this.view = view;
	}

	public List<WorldViewBean> getViews() {
		return views;
	}

	public void setViews(List<WorldViewBean> views) {
		this.views = views;
	}

	public String getView_pk() {
		return view_pk;
	}

	public void setView_pk(String view_pk) {
		this.view_pk = view_pk;
	}
}