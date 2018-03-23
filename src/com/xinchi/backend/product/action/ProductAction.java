package com.xinchi.backend.product.action;

import java.math.BigDecimal;
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
import com.xinchi.bean.ProductAirTicketBean;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ProductDelayBean;
import com.xinchi.bean.ProductOrderDto;
import com.xinchi.bean.ProductReportDto;
import com.xinchi.bean.ProductSupplierBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

	@Autowired
	private ProductSupplierService productSupplierService;
	private String json;

	public String createProduct() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		product.setProduct_manager(sessionBean.getUser_number());
		resultStr = service.insert(product);

		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			int supplier_index = obj.getInt("supplier_index");
			String supplier_employee_pk = obj.getString("supplier_employee_pk");
			String supplier_product_name = obj.getString("supplier_product_name");
			String c = obj.getString("supplier_cost");
			BigDecimal supplier_cost = null == c ? BigDecimal.ZERO : new BigDecimal(c);

			int land_day = obj.getInt("land_day");
			String pick_type = obj.getString("pick_type");
			String picker = obj.getString("picker");
			String picker_cellphone = obj.getString("picker_cellphone");
			int off_day = obj.getInt("off_day");
			String send_type = obj.getString("send_type");

			ProductSupplierBean psb = new ProductSupplierBean();
			psb.setSupplier_index(supplier_index);
			psb.setSupplier_employee_pk(supplier_employee_pk);
			psb.setLand_day(land_day);
			psb.setPick_type(pick_type);
			psb.setPicker(picker);
			psb.setPicker_cellphone(picker_cellphone);
			psb.setOff_day(off_day);
			psb.setProduct_pk(product.getPk());
			psb.setSupplier_product_name(supplier_product_name);
			psb.setSupplier_cost(supplier_cost);
			psb.setSend_type(send_type);

			productSupplierService.insert(psb);
		}

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

	public String updateProduct() {
		resultStr = service.update(product, delay);

		// 删除之前的供应商联系
		productSupplierService.deleteByProductPk(product.getPk());

		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			int supplier_index = obj.getInt("supplier_index");
			String supplier_employee_pk = obj.getString("supplier_employee_pk");
			String supplier_product_name = obj.getString("supplier_product_name");
			String c = obj.getString("supplier_cost");
			BigDecimal supplier_cost = null == c ? BigDecimal.ZERO : new BigDecimal(c);

			int land_day = obj.getInt("land_day");
			String pick_type = obj.getString("pick_type");
			String picker = obj.getString("picker");
			String picker_cellphone = obj.getString("picker_cellphone");
			int off_day = obj.getInt("off_day");
			String send_type = obj.getString("send_type");

			ProductSupplierBean psb = new ProductSupplierBean();
			psb.setSupplier_index(supplier_index);
			psb.setSupplier_employee_pk(supplier_employee_pk);
			psb.setLand_day(land_day);
			psb.setPick_type(pick_type);
			psb.setPicker(picker);
			psb.setPicker_cellphone(picker_cellphone);
			psb.setOff_day(off_day);
			psb.setProduct_pk(product.getPk());
			psb.setSupplier_product_name(supplier_product_name);
			psb.setSupplier_cost(supplier_cost);
			psb.setSend_type(send_type);

			productSupplierService.insert(psb);
		}

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

	// 上下架产品
	public String onSaleProduct() {
		resultStr = service.onSale(product_pks, sale_flg, force_flg);
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

	private ProductOrderDto order_option;

	@Autowired
	private ProductOrderService productOrderService;

	private List<ProductOrderDto> productOrders;

	/**
	 * 搜索产品订单
	 * 
	 * @return
	 */
	public String searchProductOrderByPage() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String roles = sessionBean.getUser_roles();
		if (null == order_option)
			order_option = new ProductOrderDto();

		if (!roles.contains(ResourcesConstants.USER_ROLE_ADMIN)) {
			order_option.setProduct_manager_number(sessionBean.getUser_number());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", order_option);
		page.setParams(params);

		productOrders = productOrderService.selectByPage(page);
		return SUCCESS;
	}

	private String ticket_charge;

	@Autowired
	private ProductAirTicketService productAirTicketService;

	private String ticket_json;

	/**
	 * 保存产品机票信息
	 * 
	 * @return
	 */
	public String saveAirTicket() {
		// 删除已有的机票信息
		productAirTicketService.deleteByProductPk(product_pk);

		if (!ticket_charge.equals("NONE")) {
			JSONArray arr = JSONArray.fromObject(ticket_json);
			for (int i = 0; i < arr.size(); i++) {
				JSONObject obj = arr.getJSONObject(i);

				int index = obj.getInt("index");
				int start_day = obj.getInt("start_day");
				String start_city = obj.getString("start_city");
				int end_day = obj.getInt("end_day");
				String end_city = obj.getString("end_city");
				String ticket_number = obj.getString("ticket_number");

				ProductAirTicketBean ticket = new ProductAirTicketBean();

				ticket.setProduct_pk(product_pk);
				ticket.setTicket_index(index);
				ticket.setStart_day(start_day);
				ticket.setStart_city(start_city);
				ticket.setEnd_day(end_day);
				ticket.setEnd_city(end_city);
				ticket.setTicket_number(ticket_number);
				productAirTicketService.insert(ticket);

			}
		}

		product = new ProductBean();
		product.setAir_ticket_charge(ticket_charge);
		product.setPk(product_pk);
		service.updateStraight(product);

		resultStr = SUCCESS;
		return SUCCESS;
	}

	private List<ProductAirTicketBean> air_tickets;

	public String searchProductAirTicketInfoByProductPk() {
		product = service.selectByPrimaryKey(product_pk);

		air_tickets = productAirTicketService.selectByProductPk(product_pk);
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

	public String getTicket_charge() {
		return ticket_charge;
	}

	public void setTicket_charge(String ticket_charge) {
		this.ticket_charge = ticket_charge;
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

	public ProductOrderDto getOrder_option() {
		return order_option;
	}

	public void setOrder_option(ProductOrderDto order_option) {
		this.order_option = order_option;
	}

	public List<ProductOrderDto> getProductOrders() {
		return productOrders;
	}

	public void setProductOrders(List<ProductOrderDto> productOrders) {
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
}