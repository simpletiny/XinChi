package com.xinchi.backend.product.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.backend.product.dao.ProductDelayDAO;
import com.xinchi.backend.product.dao.ProductLocalDAO;
import com.xinchi.backend.product.dao.ProductSupplierDAO;
import com.xinchi.backend.product.dao.ProductSupplierInfoDAO;
import com.xinchi.backend.product.service.ProductService;
import com.xinchi.backend.ticket.dao.FlightDAO;
import com.xinchi.backend.ticket.dao.FlightInfoDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.FlightBean;
import com.xinchi.bean.FlightInfoBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ProductDelayBean;
import com.xinchi.bean.ProductLocalBean;
import com.xinchi.bean.ProductSupplierBean;
import com.xinchi.bean.ProductSupplierInfoBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO dao;

	@Autowired
	private NumberService numberService;

	@Override
	public String insert(ProductBean bean) {
		dao.insert(bean);
		return "success";
	}

	@Override
	public String createProduct(ProductBean product) {
		ProductBean option = new ProductBean();
		option.setProduct_model(product.getProduct_model());

		// 检测产品型号
		List<ProductBean> exists = dao.selectByParam(option);

		if (null != exists && exists.size() > 0)
			return "exists";

		dao.insert(product);
		return SUCCESS;
	}

	@Autowired
	private ProductDelayDAO delayDao;

	@Override
	public String update(ProductBean bean, ProductDelayBean delay) {
		// 检测产品型号
		ProductBean option = new ProductBean();
		option.setProduct_model(bean.getProduct_model());
		List<ProductBean> exists = dao.selectByParam(option);
		if (null != exists) {
			for (ProductBean exist : exists) {
				if (!exist.getPk().equals(bean.getPk())) {
					return "exists";
				}
			}
		}

		ProductBean oldProduct = dao.selectByPrimaryKey(bean.getPk());
		ProductDelayBean exist_delay = delayDao.selectByProductPk(bean.getPk());

		if (oldProduct.getSale_flg().equals("Y")) {
			if (null == exist_delay) {
				delay.setProduct_pk(bean.getPk());
				delayDao.insert(delay);
			} else {
				if (exist_delay.getUpdate_cnt() >= 3) {
					return "more_update";
				} else {
					delay.setPk(exist_delay.getPk());
					delay.setUpdate_cnt(exist_delay.getUpdate_cnt() + 1);
					delayDao.update(delay);
				}
			}
		} else {
			// 检测是否之前架上有过修改,如果有删除
			if (null != exist_delay)
				delayDao.delete(exist_delay.getPk());

			bean.setAdult_price(delay.getAdult_price());
			bean.setChild_price(delay.getChild_price());
			bean.setBusiness_profit_substract(delay.getBusiness_profit_substract());
			bean.setMax_profit_substract(delay.getMax_profit_substract());
			bean.setProduct_value(delay.getProduct_value());
			bean.setProduct_child_value(delay.getProduct_child_value());
			bean.setGross_profit(delay.getGross_profit());
			bean.setGross_profit_rate(delay.getGross_profit_rate());
			bean.setCash_flow(delay.getCash_flow());
			bean.setSpot_cash(delay.getSpot_cash());
		}
		dao.update(bean);
		return "success";
	}

	@Override
	public String updateProductValue(ProductBean bean, ProductDelayBean delay) {
		ProductBean product = dao.selectByPrimaryKey(bean.getPk());
		ProductDelayBean exist_delay = delayDao.selectByProductPk(bean.getPk());

		if (product.getSale_flg().equals("Y")) {
			if (null == exist_delay) {
				delay.setProduct_pk(bean.getPk());
				delayDao.insert(delay);
			} else {
				if (exist_delay.getUpdate_cnt() >= 3) {
					return "more_update";
				} else {
					delay.setPk(exist_delay.getPk());
					delay.setUpdate_cnt(exist_delay.getUpdate_cnt() + 1);
					delayDao.update(delay);
				}
			}
		} else {
			// 检测是否之前架上有过修改,如果有删除
			if (null != exist_delay) {
				exist_delay.setProduct_value(delay.getProduct_value());
				exist_delay.setProduct_child_value(delay.getProduct_child_value());
				delayDao.update(exist_delay);
			}
			bean.setProduct_value(delay.getProduct_value());
			bean.setProduct_child_value(delay.getProduct_child_value());
			dao.update(bean);
		}

		return SUCCESS;
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ProductBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<ProductBean> getAllByParam(ProductBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<ProductBean> selectByPage(Page<ProductBean> page) {
		return dao.selectByPage(page);
	}

	@Autowired
	private BudgetStandardOrderDAO bsoDao;

	@Override
	public String onSale(String product_pks, String sale_flg, String force_flg) {
		String[] pks = product_pks.split(",");
		List<ProductBean> products = dao.selectByPks(pks);

		// 如果是上架产品
		if (sale_flg.equals("Y")) {
			for (ProductBean product : products) {
				if (product.getAir_ticket_charge().equals("NO")) {
					return "请绑定产品：" + product.getProduct_number() + "机票信息";
				}
				// 如果没有产品编号（即新建产品，创建你妈的产品编号）
				if (SimpletinyString.isEmpty(product.getProduct_number())) {
					String product_number = numberService.generateProductNumber();
					product.setProduct_number(product_number);
				}

				// 判断架上是否有存在(名称+产品型号)相同的产品
				ProductBean option = new ProductBean();
				option.setName(product.getName());
				option.setProduct_model(product.getProduct_model());
				List<ProductBean> pros = dao.selectByParam(option);

				if (null != pros && pros.size() > 0) {
					for (ProductBean pro : pros) {
						if (!pro.getPk().equals(product.getPk()))
							return "存在产品名称+产品型号相同的架上产品：" + product.getName() + "--" + product.getProduct_model();
					}

				}
				// 判断是否为当日下架产品
				if (!SimpletinyString.isEmpty(product.getOff_shelves_date())) {
					if (DateUtil.compare(product.getOff_shelves_date()) == 2) {
						product.setSale_flg(sale_flg);
						dao.update(product);
					} else {
						return "当日下架的产品：" + product.getProduct_number() + "，不能重新上架";
					}
				} else {
					product.setSale_flg(sale_flg);
					dao.update(product);
				}

			}
		}
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String user_roles = sessionBean.getUser_roles();

		// 如果是下架产品
		if (sale_flg.equals("N")) {
			for (ProductBean product : products) {
				// 判断是否存在待确认订单
				BudgetStandardOrderBean option = new BudgetStandardOrderBean();
				option.setProduct_pk(product.getPk());
				option.setConfirm_flg("N");
				List<BudgetStandardOrderBean> orders = bsoDao.selectByParam(option);
				if (null != orders && orders.size() > 0) {
					// 判断是否有经理权限
					if (!user_roles.contains(ResourcesConstants.USER_ROLE_ADMIN)
							&& !user_roles.contains(ResourcesConstants.USER_ROLE_MANAGER)) {
						return product.getProduct_number() + product.getName() + "有待确认订单，请沟通后下架！";
					} else {
						if (!SimpletinyString.isEmpty(force_flg) && force_flg.equals("Y")) {
							// 设定下架日期
							product.setOff_shelves_date(DateUtil.today());
							product.setSale_flg(sale_flg);
							dao.update(product);
							// 删除待确认订单
							for (BudgetStandardOrderBean order : orders) {
								bsoDao.delete(order.getPk());
							}
						} else {
							return "second&&" + product.getProduct_number() + product.getName() + "有待确认订单！";
						}
					}
				} else {
					// 设定下架日期
					product.setOff_shelves_date(DateUtil.today());
					product.setSale_flg(sale_flg);
					dao.update(product);
				}
			}
		}
		// 如果是废弃产品
		if (sale_flg.equals("D")) {
			for (ProductBean product : products) {
				// 有产品编号的废弃，没有产品编号的删除
				if (SimpletinyString.isEmpty(product.getProduct_number())) {
					dao.delete(product.getPk());
				} else {
					product.setSale_flg(sale_flg);
					dao.update(product);
				}

			}
		}
		return SUCCESS;
	}

	@Override
	public void updateStraight(ProductBean product) {
		dao.update(product);
	}

	@Override
	public String updateProductDirectly(ProductBean product) {
		dao.update(product);
		return SUCCESS;
	}

	@Override
	public void sysUpdate(ProductBean product) {
		dao.sysUpdate(product);

	}

	@Override
	public String uploadClientConfirmTemplet(ProductBean product) {
		String fileFolder = PropertiesUtil.getProperty("clientConfirmTempletFolder");
		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		// 删除旧的模板
		ProductBean oldProduct = dao.selectByPrimaryKey(product.getPk());
		if (!oldProduct.getClient_confirm_templet().equals("no")
				&& !oldProduct.getClient_confirm_templet().equals("default")) {

			File oldFile = new File(fileFolder + File.separator + oldProduct.getClient_confirm_templet());
			oldFile.delete();
		}
		if (!product.getClient_confirm_templet().equals("no")) {

			File sourceFile = new File(tempFolder + File.separator + product.getClient_confirm_templet());
			File destfile = new File(fileFolder + File.separator + product.getClient_confirm_templet());
			try {
				FileUtils.copyFile(sourceFile, destfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sourceFile.delete();
		}

		// 保存文件
		dao.update(product);

		return SUCCESS;
	}

	@Override
	public String uploadOutNoticeTemplet(ProductBean product) {
		String fileFolder = PropertiesUtil.getProperty("outNoticeTempletFolder");
		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");

		// 删除旧的模板
		ProductBean oldProduct = dao.selectByPrimaryKey(product.getPk());
		if (!oldProduct.getOut_notice_templet().equals("no") && !oldProduct.getOut_notice_templet().equals("default")) {

			File oldFile = new File(fileFolder + File.separator + oldProduct.getOut_notice_templet());
			oldFile.delete();
		}

		if (!product.getOut_notice_templet().equals("no")) {
			File sourceFile = new File(tempFolder + File.separator + product.getOut_notice_templet());
			File destfile = new File(fileFolder + File.separator + product.getOut_notice_templet());
			try {
				FileUtils.copyFile(sourceFile, destfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sourceFile.delete();
		}

		// 保存文件
		dao.update(product);

		return SUCCESS;
	}

	@Autowired
	private ProductSupplierDAO psDao;

	@Autowired
	private ProductSupplierInfoDAO psiDao;

	@Override
	public String saveProductSupplier(String json) {
		JSONObject obj = JSONObject.fromObject(json);
		String product_pk = obj.getString("product_pk");

		// 原产品标记为已维护
		ProductBean product = dao.selectByPrimaryKey(product_pk);
		product.setSupplier_upkeep_flg("Y");

		JSONArray suppliers = obj.getJSONArray("json");

		for (int i = 0; i < suppliers.size(); i++) {
			JSONObject supplier = suppliers.getJSONObject(i);
			ProductSupplierBean psb = new ProductSupplierBean();

			int supplier_index = supplier.getInt("supplier_index");
			String supplier_pk = supplier.getString("supplier_pk");
			String supplier_product_name = supplier.getString("supplier_product_name");
			int supplier_product_days = supplier.getInt("supplier_product_days");
			BigDecimal adult_cost = new BigDecimal(supplier.getString("adult_cost"));
			BigDecimal child_cost = new BigDecimal(supplier.getString("child_cost"));

			String tourist_info = supplier.getString("tourist_info");
			String confirm_file_templet = supplier.getString("confirm_file_templet");

			psb.setSupplier_index(supplier_index);
			psb.setSupplier_employee_pk(supplier_pk);
			psb.setSupplier_product_name(supplier_product_name);
			psb.setDays(supplier_product_days);
			psb.setProduct_pk(product_pk);
			psb.setAdult_cost(adult_cost);
			psb.setChild_cost(child_cost);
			psb.setTourist_info(tourist_info);
			psb.setConfirm_file_templet(
					SimpletinyString.isEmpty(confirm_file_templet) ? "default" : confirm_file_templet);

			// 保存地接社确认模板文件
			if (!psb.getConfirm_file_templet().equals("default")) {
				saveSupplierConfirmTemplet(confirm_file_templet);
			}
			String product_supplier_pk = psDao.insert(psb);

			JSONArray infos = supplier.getJSONArray("info_json");

			for (int j = 0; j < infos.size(); j++) {

				JSONObject info = infos.getJSONObject(j);

				int info_index = info.getInt("info_index");
				String pick_type = info.getString("pick_type");
				String pick_leg = info.getString("pick_leg");
				String pick_other = info.getString("pick_other");
				int pick_day = info.getInt("pick_day");
				String pick_traffic = info.getString("pick_traffic");
				String pick_time = info.getString("pick_time");
				String pick_city = info.getString("pick_city");
				String pick_place = info.getString("pick_place");

				String send_type = info.getString("send_type");
				String send_leg = info.getString("send_leg");
				String send_other = info.getString("send_other");
				int send_day = info.getInt("send_day");
				String send_traffic = info.getString("send_traffic");
				String send_time = info.getString("send_time");
				String send_city = info.getString("send_city");
				String send_place = info.getString("send_place");

				ProductSupplierInfoBean psib = new ProductSupplierInfoBean();

				psib.setProduct_supplier_pk(product_supplier_pk);
				psib.setInfo_index(info_index);
				psib.setPick_type(pick_type);
				psib.setPick_leg(pick_leg);
				psib.setPick_other(pick_other);
				psib.setPick_day(pick_day);
				psib.setPick_traffic(pick_traffic);
				psib.setPick_time(pick_time);
				psib.setPick_city(pick_city);
				psib.setPick_place(pick_place);
				psib.setSend_type(send_type);
				psib.setSend_leg(send_leg);
				psib.setSend_other(send_other);
				psib.setSend_day(send_day);
				psib.setSend_traffic(send_traffic);
				psib.setSend_time(send_time);
				psib.setSend_city(send_city);
				psib.setSend_place(send_place);
				psiDao.insert(psib);
			}
		}

		dao.update(product);
		return SUCCESS;
	}

	@Autowired
	private ProductLocalDAO plDao;

	@Override
	public String saveProductLocal(String json) {
		JSONObject obj = JSONObject.fromObject(json);
		String product_pk = obj.getString("product_pk");

		// 原产品标记为已维护
		ProductBean product = dao.selectByPrimaryKey(product_pk);
		product.setLocal_upkeep_flg("Y");

		JSONArray locals = obj.getJSONArray("json");

		for (int i = 0; i < locals.size(); i++) {
			JSONObject local = locals.getJSONObject(i);

			String service_type = local.getString("service_type");
			String cost = local.getString("cost");
			String supplier_pk = local.getString("supplier_pk");
			String service_name = local.getString("service_name");
			String adult_cost = local.getString("adult_cost");
			String child_cost = local.getString("child_cost");
			String service_comment = local.getString("service_comment");
			String tourist_info = local.getString("tourist_info");

			ProductLocalBean pl = new ProductLocalBean();
			pl.setProduct_pk(product_pk);
			pl.setService_type(service_type);
			pl.setSupplier_pk(supplier_pk);
			pl.setService_name(service_name);
			pl.setService_comment(service_comment);
			pl.setTourist_info(tourist_info);

			pl.setCost(new BigDecimal(cost));
			pl.setAdult_cost(new BigDecimal(adult_cost));

			if (!SimpletinyString.isEmpty(child_cost)) {
				pl.setChild_cost(new BigDecimal(child_cost));
			}

			plDao.insert(pl);

		}

		dao.update(product);
		return SUCCESS;
	}

	private void saveSupplierConfirmTemplet(String confirm_file_templet) {
		if (SimpletinyString.isEmpty(confirm_file_templet))
			return;

		String fileFolder = PropertiesUtil.getProperty("supplierConfirmTempletFolder");
		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");

		File sourceFile = new File(tempFolder + File.separator + confirm_file_templet);
		File destfile = new File(fileFolder + File.separator + confirm_file_templet);
		try {
			FileUtils.copyFile(sourceFile, destfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sourceFile.delete();
	}

	@Autowired
	private FlightDAO flightDao;

	@Autowired
	private FlightInfoDAO flightInfoDao;

	@Override
	public String saveProductFlight(FlightBean flight, String json) {
		String product_pk = flight.getProduct_pk();

		ProductBean product = dao.selectByPrimaryKey(product_pk);

		String flight_pk = flightDao.insert(flight);
		JSONArray arr = JSONArray.fromObject(json);

		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			int flight_index = obj.getInt("flight_index");
			String flight_leg = obj.getString("flight_leg");

			int start_day = obj.getInt("start_day");
			String start_city = obj.getString("start_city");
			int end_day = obj.getInt("end_day");
			String end_city = obj.getString("end_city");
			String flight_number = obj.getString("flight_number");

			FlightInfoBean info = new FlightInfoBean();

			info.setFlight_pk(flight_pk);
			info.setFlight_index(flight_index);
			info.setFlight_leg(flight_leg);
			info.setStart_day(start_day);
			info.setStart_city(start_city);
			info.setEnd_day(end_day);
			info.setEnd_city(end_city);
			info.setFlight_number(flight_number);

			flightInfoDao.insert(info);
		}
		// 更新产品机票维护标识
		product.setAir_ticket_upkeep_flg("Y");
		dao.update(product);
		return SUCCESS;
	}

	/**
	 * 更新产品机票信息
	 */
	@Override
	public String updateProductFlight(FlightBean flight, String json) {
		String flight_pk = flight.getPk();

		// 删除之前的机票对应信息
		flightInfoDao.deleteByFlightPk(flight_pk);

		// 保存新的机票对应信息
		JSONArray arr = JSONArray.fromObject(json);

		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			int flight_index = obj.getInt("flight_index");
			String flight_leg = obj.getString("flight_leg");

			int start_day = obj.getInt("start_day");
			String start_city = obj.getString("start_city");
			int end_day = obj.getInt("end_day");
			String end_city = obj.getString("end_city");
			String flight_number = obj.getString("flight_number");

			FlightInfoBean info = new FlightInfoBean();

			info.setFlight_pk(flight_pk);
			info.setFlight_index(flight_index);
			info.setFlight_leg(flight_leg);
			info.setStart_day(start_day);
			info.setStart_city(start_city);
			info.setEnd_day(end_day);
			info.setEnd_city(end_city);
			info.setFlight_number(flight_number);

			flightInfoDao.insert(info);
		}
		// 更新产品机票维护基本信息
		flightDao.update(flight);

		return SUCCESS;
	}

	@Override
	public String updateProductSupplier(String json) {
		JSONObject obj = JSONObject.fromObject(json);
		String product_pk = obj.getString("product_pk");

		// 原先的地接维护信息
		List<ProductSupplierBean> oldSuppliers = psDao.selectByProductPk(product_pk);
		List<String> templetFiles = new ArrayList<String>();
		for (ProductSupplierBean psb : oldSuppliers) {
			templetFiles.add(psb.getConfirm_file_templet());
			// 原先的地接信息info
			List<ProductSupplierInfoBean> oldSupplierInfos = psiDao.selectByProductSupplierPk(psb.getPk());
			// 删除原先的的地接维护info信息
			for (ProductSupplierInfoBean psi : oldSupplierInfos) {
				psiDao.delete(psi.getPk());
			}

			// 删除原先的地接信息
			psDao.delete(psb.getPk());
		}

		JSONArray suppliers = obj.getJSONArray("json");

		for (int i = 0; i < suppliers.size(); i++) {
			JSONObject supplier = suppliers.getJSONObject(i);
			ProductSupplierBean psb = new ProductSupplierBean();

			int supplier_index = supplier.getInt("supplier_index");
			String supplier_pk = supplier.getString("supplier_pk");
			String supplier_product_name = supplier.getString("supplier_product_name");
			int supplier_product_days = supplier.getInt("supplier_product_days");
			BigDecimal adult_cost = new BigDecimal(supplier.getString("adult_cost"));
			BigDecimal child_cost = new BigDecimal(supplier.getString("child_cost"));

			String tourist_info = supplier.getString("tourist_info");
			String confirm_file_templet = supplier.getString("confirm_file_templet");

			psb.setSupplier_index(supplier_index);
			psb.setSupplier_employee_pk(supplier_pk);
			psb.setSupplier_product_name(supplier_product_name);
			psb.setDays(supplier_product_days);
			psb.setProduct_pk(product_pk);
			psb.setAdult_cost(adult_cost);
			psb.setChild_cost(child_cost);
			psb.setTourist_info(tourist_info);
			psb.setConfirm_file_templet(
					SimpletinyString.isEmpty(confirm_file_templet) ? "default" : confirm_file_templet);

			// 更新地接社确认模板文件
			if (!psb.getConfirm_file_templet().equals("default")) {
				if (templetFiles.contains(confirm_file_templet)) {
					templetFiles.remove(confirm_file_templet);
				} else {
					saveSupplierConfirmTemplet(confirm_file_templet);
				}
			}

			String product_supplier_pk = psDao.insert(psb);

			JSONArray infos = supplier.getJSONArray("info_json");

			for (int j = 0; j < infos.size(); j++) {
				JSONObject info = infos.getJSONObject(j);

				int info_index = info.getInt("info_index");
				String pick_type = info.getString("pick_type");
				String pick_leg = info.getString("pick_leg");
				String pick_other = info.getString("pick_other");
				int pick_day = info.getInt("pick_day");
				String pick_traffic = info.getString("pick_traffic");
				String pick_time = info.getString("pick_time");
				String pick_city = info.getString("pick_city");
				String pick_place = info.getString("pick_place");

				String send_type = info.getString("send_type");
				String send_leg = info.getString("send_leg");
				String send_other = info.getString("send_other");
				int send_day = info.getInt("send_day");
				String send_traffic = info.getString("send_traffic");
				String send_time = info.getString("send_time");
				String send_city = info.getString("send_city");
				String send_place = info.getString("send_place");

				ProductSupplierInfoBean psib = new ProductSupplierInfoBean();

				psib.setProduct_supplier_pk(product_supplier_pk);
				psib.setInfo_index(info_index);
				psib.setPick_type(pick_type);
				psib.setPick_leg(pick_leg);
				psib.setPick_other(pick_other);
				psib.setPick_day(pick_day);
				psib.setPick_traffic(pick_traffic);
				psib.setPick_time(pick_time);
				psib.setPick_city(pick_city);
				psib.setPick_place(pick_place);
				psib.setSend_type(send_type);
				psib.setSend_leg(send_leg);
				psib.setSend_other(send_other);
				psib.setSend_day(send_day);
				psib.setSend_traffic(send_traffic);
				psib.setSend_time(send_time);
				psib.setSend_city(send_city);
				psib.setSend_place(send_place);
				psiDao.insert(psib);
			}
		}
		// 删除之前没用的模板
		for (String file : templetFiles) {
			if (!file.equals("default")) {
				FileUtil.deleteFile(file, "supplierConfirmTempletFolder", null);
			}
		}
		return SUCCESS;
	}

	@Override
	public List<ProductLocalBean> searchProductLocalByProductPk(String product_pk) {
		return plDao.selectByProductPk(product_pk);
	}

	@Override
	public String updateProductLocal(String json) {
		JSONObject obj = JSONObject.fromObject(json);
		String product_pk = obj.getString("product_pk");

		// 删除之前的本地维护
		plDao.deleteByProductPk(product_pk);

		// 保存新的本地维护
		JSONArray locals = obj.getJSONArray("json");

		for (int i = 0; i < locals.size(); i++) {
			JSONObject local = locals.getJSONObject(i);

			String service_type = local.getString("service_type");
			String cost = local.getString("cost");
			String supplier_pk = local.getString("supplier_pk");
			String service_name = local.getString("service_name");
			String adult_cost = local.getString("adult_cost");
			String child_cost = local.getString("child_cost");
			String service_comment = local.getString("service_comment");
			String tourist_info = local.getString("tourist_info");

			ProductLocalBean pl = new ProductLocalBean();
			pl.setProduct_pk(product_pk);
			pl.setService_type(service_type);
			pl.setSupplier_pk(supplier_pk);
			pl.setService_name(service_name);
			pl.setService_comment(service_comment);
			pl.setTourist_info(tourist_info);

			pl.setCost(new BigDecimal(cost));
			pl.setAdult_cost(new BigDecimal(adult_cost));

			if (!SimpletinyString.isEmpty(child_cost)) {
				pl.setChild_cost(new BigDecimal(child_cost));
			}

			plDao.insert(pl);

		}
		return SUCCESS;
	}

	@Autowired
	private OrderDAO orderDao;

	@Autowired
	private BudgetNonStandardOrderDAO bnsoDao;

	@Override
	public String unlockOrders(List<String> team_numbers) {
		for (String team_number : team_numbers) {
			OrderDto order = orderDao.selectByTeamNumber(team_number);
			if (order.getStandard_flg().equals("Y")) {
				BudgetStandardOrderBean bso = new BudgetStandardOrderBean();
				bso.setPk(order.getPk());
				bso.setLock_flg("N");
				bsoDao.update(bso);
			} else {
				BudgetNonStandardOrderBean bnso = new BudgetNonStandardOrderBean();
				bnso.setPk(order.getPk());
				bnso.setLock_flg("N");
				bnsoDao.update(bnso);
			}
		}

		return SUCCESS;
	}

	@Override
	public String tipSalesConfirmName(List<String> team_numbers) {
		for (String team_number : team_numbers) {
			OrderDto order = orderDao.selectByTeamNumber(team_number);

			if (order.getName_confirm_status().equals("3") || order.getName_confirm_status().equals("5"))
				continue;
			if (order.getStandard_flg().equals("Y")) {
				BudgetStandardOrderBean bso = new BudgetStandardOrderBean();
				bso.setPk(order.getPk());

				bso.setName_confirm_status(ResourcesConstants.NAME_CONFIRM_STATUS_PRODUCTING);
				bsoDao.update(bso);
			} else {
				BudgetNonStandardOrderBean bnso = new BudgetNonStandardOrderBean();
				bnso.setPk(order.getPk());
				bnso.setName_confirm_status(ResourcesConstants.NAME_CONFIRM_STATUS_PRODUCTING);
				bnsoDao.update(bnso);
			}
		}

		return SUCCESS;
	}

}