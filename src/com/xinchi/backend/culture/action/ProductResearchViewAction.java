package com.xinchi.backend.culture.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.culture.service.ProductResearchViewService;
import com.xinchi.bean.ProductResearchLabelBean;
import com.xinchi.bean.ProductResearchViewBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductResearchViewAction extends BaseAction {
	private static final long serialVersionUID = -1721957811781950515L;

	@Autowired
	private ProductResearchViewService service;

	private ProductResearchViewBean view;

	public String createProductResearchView() {
		service.insert(view);
		resultStr = OK;
		return SUCCESS;
	}

	private List<ProductResearchViewBean> views;

	public String searchProductResearchViewByPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		if (view == null)
			view = new ProductResearchViewBean();
		params.put("bo", view);

		page.setParams(params);

		views = service.getAllViewsByPage(page);
		return SUCCESS;
	}

	private String view_pk;

	public String searchOneProductResearchView() {

		view = service.selectViewByPk(view_pk);
		return SUCCESS;
	}

	public String updateProductResearchView() {
		service.update(view);
		resultStr = OK;
		return SUCCESS;
	}

	public String deleteProductResearchView() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		String user_number = sessionBean.getUser_number();
		if (roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			service.delete(view_pk);
			resultStr = OK;
		} else {
			ProductResearchViewBean deleteView = service.selectViewByPk(view_pk);
			if (deleteView.getCreate_user().equals(user_number)) {
				service.delete(view_pk);
				resultStr = OK;
			} else {
				resultStr = "NORIGHT";
			}
		}

		return SUCCESS;
	}

	private ProductResearchLabelBean label;

	public String createLabel() {
		resultStr = service.createLabel(label);
		return SUCCESS;
	}

	private List<ProductResearchLabelBean> labels;

	public String searchAllLabels() {
		labels = service.selectLabelsByParam(label);
		return SUCCESS;
	}

	private String label_name;

	public String deleteLabelByName() {
		resultStr = service.deleteLabelByName(label_name);
		return SUCCESS;
	}

	private String sort_json;

	public String sortLabels() {
		resultStr = service.sortLabels(sort_json);
		return SUCCESS;
	}

	public String getView_pk() {
		return view_pk;
	}

	public void setView_pk(String view_pk) {
		this.view_pk = view_pk;
	}

	public ProductResearchViewBean getView() {
		return view;
	}

	public void setView(ProductResearchViewBean view) {
		this.view = view;
	}

	public List<ProductResearchViewBean> getViews() {
		return views;
	}

	public void setViews(List<ProductResearchViewBean> views) {
		this.views = views;
	}

	public ProductResearchLabelBean getLabel() {
		return label;
	}

	public void setLabel(ProductResearchLabelBean label) {
		this.label = label;
	}

	public List<ProductResearchLabelBean> getLabels() {
		return labels;
	}

	public void setLabels(List<ProductResearchLabelBean> labels) {
		this.labels = labels;
	}

	public String getLabel_name() {
		return label_name;
	}

	public void setLabel_name(String label_name) {
		this.label_name = label_name;
	}

	public String getSort_json() {
		return sort_json;
	}

	public void setSort_json(String sort_json) {
		this.sort_json = sort_json;
	}
}