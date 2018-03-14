package com.xinchi.backend.culture.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.culture.service.ProductRuleViewService;
import com.xinchi.bean.ProductRuleViewBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductRuleViewAction extends BaseAction {
	private static final long serialVersionUID = -1721957811781950515L;

	@Autowired
	private ProductRuleViewService service;

	private ProductRuleViewBean view;

	public String createProductRuleView() {
		service.insert(view);
		resultStr = OK;
		return SUCCESS;
	}

	private List<ProductRuleViewBean> views;

	public String searchProductRuleViewByPage() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", view);

		page.setParams(params);

		views = service.getAllViewsByPage(page);
		return SUCCESS;
	}

	private String view_pk;

	public String searchOneProductRuleView() {

		view = service.selectViewByPk(view_pk);
		return SUCCESS;
	}

	public String updateProductRuleView() {
		service.update(view);
		resultStr = OK;
		return SUCCESS;
	}

	public String deleteProductRuleView() {
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

	public ProductRuleViewBean getView() {
		return view;
	}

	public void setView(ProductRuleViewBean view) {
		this.view = view;
	}

	public List<ProductRuleViewBean> getViews() {
		return views;
	}

	public void setViews(List<ProductRuleViewBean> views) {
		this.views = views;
	}
}