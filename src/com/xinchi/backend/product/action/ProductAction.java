package com.xinchi.backend.product.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.product.service.ProductAirTicketService;
import com.xinchi.backend.product.service.ProductOrderService;
import com.xinchi.backend.product.service.ProductReportService;
import com.xinchi.backend.product.service.ProductService;
import com.xinchi.backend.product.service.ProductSupplierService;
import com.xinchi.backend.ticket.service.FlightService;
import com.xinchi.bean.FlightBean;
import com.xinchi.bean.ProductAirTicketBean;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ProductDelayBean;
import com.xinchi.bean.ProductLocalBean;
import com.xinchi.bean.ProductNeedDto;
import com.xinchi.bean.ProductProfitBean;
import com.xinchi.bean.ProductReportDto;
import com.xinchi.bean.ProductSupplierBean;
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
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
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

	public String searchOnSaleProducts() {
		if (null == product)
			product = new ProductBean();

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", product);
		page.setParams(params);

		products = service.selectByPage(page);
		return SUCCESS;
	}

	private List<ProductProfitBean> productProfits;

	private ProductProfitBean productProfit;

	/**
	 * 搜索产品利润
	 * 
	 * @return
	 */
	public String searchProductProfit() {

		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (null == productProfit)
			productProfit = new ProductProfitBean();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			productProfit.setUser_number(sessionBean.getUser_number());
		}

		productProfits = service.searchProductProfit(productProfit);
		return SUCCESS;
	}

	/**
	 * 搜索产品供应商信息
	 * 
	 * @return
	 */
	public String searchSuppliersByProductPk() {
		productSuppliers = productSupplierService.selectByProductPk(product_pk);
		return SUCCESS;
	}

	@Autowired
	private ProductSupplierService productSupplierService;
	private String json;

	public String createProduct() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		product.setProduct_manager(sessionBean.getUser_number());
		resultStr = service.createProduct(product);

		return SUCCESS;
	}

	private ProductDelayBean delay;

	public String updateProductDirectly() {
		resultStr = service.updateProductDirectly(product);
		return SUCCESS;
	}

	public String updateProductValue() {
		resultStr = service.updateProductValue(product, delay);
		return SUCCESS;
	}

	public String searchUrgentCnt() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		resultStr = service.searchUrgentCnt(sessionBean.getUser_number());
		return SUCCESS;
	}

	public String updateProduct() {
		resultStr = service.update(product, delay);

		// // 删除之前的供应商联系
		// productSupplierService.deleteByProductPk(product.getPk());
		//
		// JSONArray array = JSONArray.fromObject(json);
		// for (int i = 0; i < array.size(); i++) {
		// JSONObject obj = array.getJSONObject(i);
		// int supplier_index = obj.getInt("supplier_index");
		// String supplier_employee_pk = obj.getString("supplier_employee_pk");
		// String supplier_product_name = obj.getString("supplier_product_name");
		// String c = obj.getString("supplier_cost");
		// BigDecimal supplier_cost = null == c ? BigDecimal.ZERO : new BigDecimal(c);
		//
		// int land_day = obj.getInt("land_day");
		// String pick_type = obj.getString("pick_type");
		// String picker = obj.getString("picker");
		// String picker_cellphone = obj.getString("picker_cellphone");
		// int off_day = obj.getInt("off_day");
		// String send_type = obj.getString("send_type");
		//
		// ProductSupplierBean psb = new ProductSupplierBean();
		// psb.setSupplier_index(supplier_index);
		// psb.setSupplier_employee_pk(supplier_employee_pk);
		// psb.setLand_day(land_day);
		// psb.setPick_type(pick_type);
		// psb.setPicker(picker);
		// psb.setPicker_cellphone(picker_cellphone);
		// psb.setOff_day(off_day);
		// psb.setProduct_pk(product.getPk());
		// psb.setSupplier_product_name(supplier_product_name);
		// psb.setSupplier_cost(supplier_cost);
		// psb.setSend_type(send_type);
		//
		// productSupplierService.insert(psb);
		// }

		return SUCCESS;
	}

	private String product_pk;

	private List<ProductSupplierBean> productSuppliers;

	public String searchProductByPk() {
		product = service.selectByPrimaryKey(product_pk);
		productSuppliers = productSupplierService.selectByProductPk(product_pk);
		return SUCCESS;
	}

	private String product_pks;
	private String sale_flg;
	private String force_flg;
	private String urgent_flg;

	// 上下架产品
	public String onSaleProduct() {
		resultStr = service.onSale(product_pks, sale_flg, force_flg, urgent_flg);
		return SUCCESS;
	}

	private List<ProductReportDto> reports;
	private ProductReportDto report_option;

	@Autowired
	private ProductReportService reportService;

	/**
	 * 搜索产品报表
	 * 
	 * @return
	 */
	public String searchProductReportByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (null == report_option)
			report_option = new ProductReportDto();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			report_option.setProduct_manager_number(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", report_option);
		page.setParams(params);

		reports = reportService.selectByPage(page);
		return SUCCESS;
	}

	private ProductNeedDto order_option;

	@Autowired
	private ProductOrderService productOrderService;

	private List<ProductNeedDto> productOrders;

	/**
	 * 搜索产品订单
	 * 
	 * @return
	 */
	public String searchProductNeedByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (null == order_option)
			order_option = new ProductNeedDto();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			order_option.setProduct_manager_number(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", order_option);
		page.setParams(params);

		productOrders = productOrderService.selectNeedByPage(page);
		return SUCCESS;
	}

	private List<String> team_numbers;

	/**
	 * 解锁订单
	 * 
	 * @return
	 */
	public String unlockOrders() {
		resultStr = service.unlockOrders(team_numbers);
		return SUCCESS;
	}

	/**
	 * 提示销售确认名单
	 * 
	 * @return
	 */
	public String tipSalesConfirmName() {
		resultStr = service.tipSalesConfirmName(team_numbers);
		return SUCCESS;
	}

	@Autowired
	private ProductAirTicketService productAirTicketService;

	private String ticket_json;

	/**
	 * 保存产品机票信息
	 * 
	 * @return
	 */
	public String saveAirTicket() {
		resultStr = service.saveAirTicket(product_pk, ticket_json);
		return SUCCESS;
	}

	private List<ProductAirTicketBean> air_tickets;

	public String searchProductAirTicketInfoByProductPk() {
		product = service.selectByPrimaryKey(product_pk);

		air_tickets = productAirTicketService.selectByProductPk(product_pk);
		return SUCCESS;
	}

	/**
	 * 上传组团确认模板
	 * 
	 * @return
	 */
	public String uploadClientConfirmTemplet() {
		resultStr = service.uploadClientConfirmTemplet(product);
		return SUCCESS;
	}

	/**
	 * 上传出团通知模板
	 * 
	 * @return
	 */
	public String uploadOutNoticeTemplet() {
		resultStr = service.uploadOutNoticeTemplet(product);
		return SUCCESS;
	}

	/**
	 * 保存地接维护信息
	 * 
	 * @return
	 */
	public String saveProductSupplier() {
		resultStr = service.saveProductSupplier(json);
		return SUCCESS;
	}

	/**
	 * 更新地接维护信息
	 * 
	 * @return
	 */
	public String updateProductSupplier() {
		resultStr = service.updateProductSupplier(json);
		return SUCCESS;
	}

	/**
	 * 保存本地维护信息
	 * 
	 * @return
	 */
	public String saveProductLocal() {
		resultStr = service.saveProductLocal(json);
		return SUCCESS;
	}

	/**
	 * 更新本地维护信息
	 * 
	 * @return
	 */
	public String updateProductLocal() {
		resultStr = service.updateProductLocal(json);
		return SUCCESS;
	}

	private List<ProductLocalBean> productLocals;

	/**
	 * 搜索本地维护
	 * 
	 * @return
	 */
	public String searchProductLocalByProductPk() {
		productLocals = service.searchProductLocalByProductPk(product_pk);
		return SUCCESS;
	}

	private FlightBean flight;

	public String saveProductFlight() {
		resultStr = service.saveProductFlight(flight, json);
		return SUCCESS;
	}

	public String updateProductFlight() {
		resultStr = service.updateProductFlight(flight, json);
		return SUCCESS;
	}

	@Autowired
	private FlightService flightService;

	public String searchFlightByProductPk() {
		flight = flightService.selectByProductPk(product_pk);
		return SUCCESS;
	}

	// 通过团号查询机票信息
	public String searchProductAirTicketInfoByTeamNumber() {

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

	public List<ProductReportDto> getReports() {
		return reports;
	}

	public void setReports(List<ProductReportDto> reports) {
		this.reports = reports;
	}

	public ProductReportDto getReport_option() {
		return report_option;
	}

	public void setReport_option(ProductReportDto report_option) {
		this.report_option = report_option;
	}

	public String getTicket_json() {
		return ticket_json;
	}

	public void setTicket_json(String ticket_json) {
		this.ticket_json = ticket_json;
	}

	public List<ProductAirTicketBean> getAir_tickets() {
		return air_tickets;
	}

	public void setAir_tickets(List<ProductAirTicketBean> air_tickets) {
		this.air_tickets = air_tickets;
	}

	public ProductNeedDto getOrder_option() {
		return order_option;
	}

	public void setOrder_option(ProductNeedDto order_option) {
		this.order_option = order_option;
	}

	public List<ProductNeedDto> getProductOrders() {
		return productOrders;
	}

	public void setProductOrders(List<ProductNeedDto> productOrders) {
		this.productOrders = productOrders;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public List<ProductSupplierBean> getProductSuppliers() {
		return productSuppliers;
	}

	public void setProductSuppliers(List<ProductSupplierBean> productSuppliers) {
		this.productSuppliers = productSuppliers;
	}

	public String getForce_flg() {
		return force_flg;
	}

	public void setForce_flg(String force_flg) {
		this.force_flg = force_flg;
	}

	public ProductDelayBean getDelay() {
		return delay;
	}

	public void setDelay(ProductDelayBean delay) {
		this.delay = delay;
	}

	public FlightBean getFlight() {
		return flight;
	}

	public void setFlight(FlightBean flight) {
		this.flight = flight;
	}

	public List<ProductLocalBean> getProductLocals() {
		return productLocals;
	}

	public void setProductLocals(List<ProductLocalBean> productLocals) {
		this.productLocals = productLocals;
	}

	public List<String> getTeam_numbers() {
		return team_numbers;
	}

	public void setTeam_numbers(List<String> team_numbers) {
		this.team_numbers = team_numbers;
	}

	public String getUrgent_flg() {
		return urgent_flg;
	}

	public void setUrgent_flg(String urgent_flg) {
		this.urgent_flg = urgent_flg;
	}

	public List<ProductProfitBean> getProductProfits() {
		return productProfits;
	}

	public void setProductProfits(List<ProductProfitBean> productProfits) {
		this.productProfits = productProfits;
	}

	public ProductProfitBean getProductProfit() {
		return productProfit;
	}

	public void setProductProfit(ProductProfitBean productProfit) {
		this.productProfit = productProfit;
	}

}