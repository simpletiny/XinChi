package com.xinchi.backend.sys.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.sys.service.PagesRoleService;
import com.xinchi.backend.sys.service.PagesService;
import com.xinchi.bean.PagesBean;
import com.xinchi.bean.PagesRoleBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PagesAction extends BaseAction {
	private static final long serialVersionUID = -511745710554071384L;

	@Autowired
	private PagesService service;

	private List<PagesBean> pages;

	private String json;

	@Autowired
	private PagesRoleService pagesRoleService;

	public String searchSortPages() {
		PagesBean option = new PagesBean();
		option.setLevel(1);
		pages = service.selectByParam(option);
		return SUCCESS;
	}

	public String updatePages() {
		resultStr = service.updatePages(json);
		return SUCCESS;
	}

	public String updateRolePages() {
		resultStr = pagesRoleService.updateRolePages(json);
		return SUCCESS;
	}

	private List<PagesRoleBean> role_pages;
	private String role;

	public String searchRolePages() {
		role_pages = pagesRoleService.searchRolePages(role);
		return SUCCESS;
	}

	public List<PagesBean> getPages() {
		return pages;
	}

	public void setPages(List<PagesBean> pages) {
		this.pages = pages;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public List<PagesRoleBean> getRole_pages() {
		return role_pages;
	}

	public void setRole_pages(List<PagesRoleBean> role_pages) {
		this.role_pages = role_pages;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}