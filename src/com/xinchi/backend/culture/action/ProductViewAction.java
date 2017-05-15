package com.xinchi.backend.culture.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.culture.service.ProductViewService;
import com.xinchi.bean.ProductViewBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductViewAction extends BaseAction {
	private static final long serialVersionUID = -1721957811781950515L;

	@Autowired
	private ProductViewService service;

	private ProductViewBean view;

	public String createProductView() {
		service.insert(view);
		resultStr = OK;
		return SUCCESS;
	}

	private List<ProductViewBean> views;

	public String searchProductViewByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", view);

		page.setParams(params);

		views = service.getAllViewsByPage(page);
		return SUCCESS;
	}

	private String view_pk;

	public String searchOneProductView() {

		view = service.selectViewByPk(view_pk);
		return SUCCESS;
	}

	public String updateProductView() {
		service.update(view);
		resultStr = OK;
		return SUCCESS;
	}

	public String deleteProductView() {
		service.delete(view_pk);
		resultStr = OK;
		return SUCCESS;
	}

	public ProductViewBean getView() {
		return view;
	}

	public void setView(ProductViewBean view) {
		this.view = view;
	}

	public List<ProductViewBean> getViews() {
		return views;
	}

	public void setViews(List<ProductViewBean> views) {
		this.views = views;
	}

	public String getView_pk() {
		return view_pk;
	}

	public void setView_pk(String view_pk) {
		this.view_pk = view_pk;
	}
}