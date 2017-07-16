package com.xinchi.backend.product.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.product.service.ProductGroupService;
import com.xinchi.backend.product.service.ProductGroupSupplierService;
import com.xinchi.bean.ProductGroupBean;
import com.xinchi.bean.SupplierBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductGroupAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ProductGroupService service;

	List<ProductGroupBean> groups;

	private ProductGroupBean group;

	public String searchGroupsByPage() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", group);
		page.setParams(params);

		groups = service.selectByPage(page);
		return SUCCESS;
	}

	public String createGroup() {

		resultStr = service.insert(group);
		return SUCCESS;
	}

	private String group_pk;

	public String searchByPk() {
		group = service.selectByPrimaryKey(group_pk);
		return SUCCESS;
	}

	@Autowired
	private ProductGroupSupplierService pgsService;
	private List<SupplierBean> group_suppliers;

	public String searchSuppliersByGroupPk() {
		group_suppliers = service.selectByGroupPk(group_pk);
		return SUCCESS;
	}

	public String updateGroup() {
		resultStr = service.update(group);
		return SUCCESS;
	}

	public String deleteGroup() {
		resultStr = service.delete(group_pk);
		return SUCCESS;
	}

	public List<ProductGroupBean> getGroups() {
		return groups;
	}

	public void setGroups(List<ProductGroupBean> groups) {
		this.groups = groups;
	}

	public ProductGroupBean getGroup() {
		return group;
	}

	public void setGroup(ProductGroupBean group) {
		this.group = group;
	}

	public String getGroup_pk() {
		return group_pk;
	}

	public void setGroup_pk(String group_pk) {
		this.group_pk = group_pk;
	}

	public List<SupplierBean> getGroup_suppliers() {
		return group_suppliers;
	}

	public void setGroup_suppliers(List<SupplierBean> group_suppliers) {
		this.group_suppliers = group_suppliers;
	}
}