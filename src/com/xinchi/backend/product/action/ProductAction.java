package com.xinchi.backend.product.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.product.service.ProductService;
import com.xinchi.bean.ProductBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductAction extends BaseAction {
	private static final long serialVersionUID = -7626860085483363703L;

	@Autowired
	private ProductService service;

	private ProductBean product;
	private List<ProductBean> products;

	public String searchProductsByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (null == product)
			product = new ProductBean();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			product.setProduct_manager(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", product);
		page.setParams(params);

		products = service.selectByPage(page);
		return SUCCESS;
	}

	public String createProduct() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		product.setProduct_manager(sessionBean.getUser_number());
		resultStr = service.insert(product);
		return SUCCESS;
	}

	public String updateProduct() {
		resultStr = service.update(product);

		return SUCCESS;
	}

	private String product_pk;

	public String searchProductByPk() {
		product = service.selectByPrimaryKey(product_pk);
		return SUCCESS;
	}

	private String product_pks;
	private String sale_flg;

	// 上下架产品
	public String onSaleProduct() {
		resultStr = service.onSale(product_pks, sale_flg);
		return SUCCESS;
	}

	public ProductBean getProduct() {
		return product;
	}

	public void setProduct(ProductBean product) {
		this.product = product;
	}

	public List<ProductBean> getProducts() {
		return products;
	}

	public void setProducts(List<ProductBean> products) {
		this.products = products;
	}

	public String getProduct_pk() {
		return product_pk;
	}

	public void setProduct_pk(String product_pk) {
		this.product_pk = product_pk;
	}

	public String getProduct_pks() {
		return product_pks;
	}

	public void setProduct_pks(String product_pks) {
		this.product_pks = product_pks;
	}

	public String getSale_flg() {
		return sale_flg;
	}

	public void setSale_flg(String sale_flg) {
		this.sale_flg = sale_flg;
	}
}