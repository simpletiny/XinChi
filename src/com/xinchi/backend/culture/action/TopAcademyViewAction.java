package com.xinchi.backend.culture.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.culture.service.TopAcademyViewService;
import com.xinchi.bean.TopAcademyViewBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TopAcademyViewAction extends BaseAction {
	private static final long serialVersionUID = -1721957811781950515L;

	@Autowired
	private TopAcademyViewService service;

	private TopAcademyViewBean view;

	public String createTopAcademyView() {
		service.insert(view);
		resultStr = OK;
		return SUCCESS;
	}

	private List<TopAcademyViewBean> views;

	public String searchTopAcademyViewByPage() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", view);

		page.setParams(params);

		views = service.getAllViewsByPage(page);
		return SUCCESS;
	}

	private String view_pk;

	public String searchOneTopAcademyView() {

		view = service.selectViewByPk(view_pk);
		return SUCCESS;
	}

	public String updateTopAcademyView() {
		service.update(view);
		resultStr = OK;
		return SUCCESS;
	}

	public String deleteTopAcademyView() {
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

	public TopAcademyViewBean getView() {
		return view;
	}

	public void setView(TopAcademyViewBean view) {
		this.view = view;
	}

	public List<TopAcademyViewBean> getViews() {
		return views;
	}

	public void setViews(List<TopAcademyViewBean> views) {
		this.views = views;
	}
}