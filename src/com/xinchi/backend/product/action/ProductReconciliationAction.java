package com.xinchi.backend.product.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.product.service.ProductReconciliationService;
import com.xinchi.bean.ProductReconciliationBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyUser;
import com.xinchi.common.UserSessionBean;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductReconciliationAction extends BaseAction {
	private static final long serialVersionUID = 5888600338574775484L;

	@Autowired
	private ProductReconciliationService service;
	private ProductReconciliationBean reconciliation;

	public String addReconciliation() {
		resultStr = service.addReconciliation(reconciliation);
		return SUCCESS;
	}

	private String product_manager_number;
	private String belong_month;

	private List<ProductReconciliationBean> reconciliations;

	/**
	 * 产品扎账
	 * 
	 * @return
	 */
	public String confirmProductAccounting() {
		resultStr = service.confirmProductAccounting(product_manager_number, belong_month);
		return SUCCESS;
	}

	public String searchReconciliationByPage() {
		UserSessionBean user = SimpletinyUser.user();
		String roles = user.getUser_roles();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			reconciliation.setProduct_manager_number(user.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", reconciliation);
		page.setParams(params);

		reconciliations = service.selectByPage(page);

		return SUCCESS;
	}

	private String reconciliation_pk;

	/**
	 * 
	 * @return
	 */
	public String deleteReconciliation() {
		resultStr = service.delete(reconciliation_pk);
		return SUCCESS;
	}

	public ProductReconciliationBean getReconciliation() {
		return reconciliation;
	}

	public void setReconciliation(ProductReconciliationBean reconciliation) {
		this.reconciliation = reconciliation;
	}

	public String getProduct_manager_number() {
		return product_manager_number;
	}

	public String getBelong_month() {
		return belong_month;
	}

	public void setProduct_manager_number(String product_manager_number) {
		this.product_manager_number = product_manager_number;
	}

	public void setBelong_month(String belong_month) {
		this.belong_month = belong_month;
	}

	public List<ProductReconciliationBean> getReconciliations() {
		return reconciliations;
	}

	public void setReconciliations(List<ProductReconciliationBean> reconciliations) {
		this.reconciliations = reconciliations;
	}

	public String getReconciliation_pk() {
		return reconciliation_pk;
	}

	public void setReconciliation_pk(String reconciliation_pk) {
		this.reconciliation_pk = reconciliation_pk;
	}

}