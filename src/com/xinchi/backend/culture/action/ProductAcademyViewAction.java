package com.xinchi.backend.culture.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.culture.service.ProductAcademyViewService;
import com.xinchi.bean.ProductAcademyViewBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductAcademyViewAction extends BaseAction {
	private static final long serialVersionUID = -1721957811781950515L;

	@Autowired
	private ProductAcademyViewService service;

	private ProductAcademyViewBean view;

	public String createProductAcademyView() {
		service.insert(view);
		resultStr = OK;
		return SUCCESS;
	}

	private List<ProductAcademyViewBean> views;

	public String searchProductAcademyViewByPage() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", view);

		page.setParams(params);

		views = service.getAllViewsByPage(page);
		return SUCCESS;
	}

	private String view_pk;

	public String searchOneProductAcademyView() {

		view = service.selectViewByPk(view_pk);
		return SUCCESS;
	}

	public String updateProductAcademyView() {
		service.update(view);
		resultStr = OK;
		return SUCCESS;
	}

	public String deleteProductAcademyView() {
		service.delete(view_pk);
		resultStr = OK;
		return SUCCESS;
	}

	public ProductAcademyViewBean getView() {
		return view;
	}

	public void setView(ProductAcademyViewBean view) {
		this.view = view;
	}

	public List<ProductAcademyViewBean> getViews() {
		return views;
	}

	public void setViews(List<ProductAcademyViewBean> views) {
		this.views = views;
	}

	public String getView_pk() {
		return view_pk;
	}

	public void setView_pk(String view_pk) {
		this.view_pk = view_pk;
	}
}