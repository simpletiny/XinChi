package com.xinchi.backend.culture.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.culture.service.AcademyViewService;
import com.xinchi.bean.AcademyViewBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AcademyViewAction extends BaseAction {
	private static final long serialVersionUID = -1721957811781950515L;

	@Autowired
	private AcademyViewService service;

	private AcademyViewBean view;

	public String createAcademyView() {
		service.insert(view);
		resultStr = OK;
		return SUCCESS;
	}

	private List<AcademyViewBean> views;

	public String searchAcademyViewByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", view);

		page.setParams(params);

		views = service.getAllViewsByPage(page);
		return SUCCESS;
	}

	private String view_pk;

	public String searchOneAcademyView() {

		view = service.selectViewByPk(view_pk);
		return SUCCESS;
	}

	public String updateAcademyView() {
		service.update(view);
		resultStr = OK;
		return SUCCESS;
	}

	public String deleteAcademyView() {
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

	public AcademyViewBean getView() {
		return view;
	}

	public void setView(AcademyViewBean view) {
		this.view = view;
	}

	public List<AcademyViewBean> getViews() {
		return views;
	}

	public void setViews(List<AcademyViewBean> views) {
		this.views = views;
	}
}