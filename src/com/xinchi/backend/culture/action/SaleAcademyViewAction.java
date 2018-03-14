package com.xinchi.backend.culture.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.culture.service.SaleAcademyViewService;
import com.xinchi.bean.SaleAcademyViewBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SaleAcademyViewAction extends BaseAction {
	private static final long serialVersionUID = -1721957811781950515L;

	@Autowired
	private SaleAcademyViewService service;

	private SaleAcademyViewBean view;

	public String createSaleAcademyView() {
		service.insert(view);
		resultStr = OK;
		return SUCCESS;
	}

	private List<SaleAcademyViewBean> views;

	public String searchSaleAcademyViewByPage() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", view);

		page.setParams(params);

		views = service.getAllViewsByPage(page);
		return SUCCESS;
	}

	private String view_pk;

	public String searchOneSaleAcademyView() {

		view = service.selectViewByPk(view_pk);
		return SUCCESS;
	}

	public String updateSaleAcademyView() {
		service.update(view);
		resultStr = OK;
		return SUCCESS;
	}

	public String deleteSaleAcademyView() {
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

	public SaleAcademyViewBean getView() {
		return view;
	}

	public void setView(SaleAcademyViewBean view) {
		this.view = view;
	}

	public List<SaleAcademyViewBean> getViews() {
		return views;
	}

	public void setViews(List<SaleAcademyViewBean> views) {
		this.views = views;
	}
}