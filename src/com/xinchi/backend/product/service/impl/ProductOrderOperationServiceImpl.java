package com.xinchi.backend.product.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetNonStandardOrderDAO;
import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.payable.dao.PayableDAO;
import com.xinchi.backend.product.dao.ProductOrderOperationDAO;
import com.xinchi.backend.product.dao.ProductOrderSupplierDAO;
import com.xinchi.backend.product.dao.ProductOrderSupplierInfoDAO;
import com.xinchi.backend.product.service.ProductOrderOperationService;
import com.xinchi.bean.BudgetNonStandardOrderBean;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.OrderSupplierBean;
import com.xinchi.bean.OrderSupplierInfoBean;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.ProductOrderOperationBean;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ProductOrderOperationServiceImpl implements ProductOrderOperationService {

	@Autowired
	private ProductOrderOperationDAO dao;

	@Override
	public void insert(ProductOrderOperationBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(ProductOrderOperationBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ProductOrderOperationBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<ProductOrderOperationBean> selectByParam(ProductOrderOperationBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<ProductOrderOperationBean> selectByPage(Page<ProductOrderOperationBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<ProductOrderOperationBean> selectByTeamNumber(String team_number) {
		return dao.selectByTeamNumber(team_number);
	}

	@Override
	public void deleteByTeamNumber(String team_number) {
		dao.deleteByTeamNumber(team_number);
	}

	@Autowired
	private ProductOrderSupplierDAO posDao;

	@Autowired
	private ProductOrderSupplierInfoDAO posiDao;

	@Autowired
	private OrderDAO orderDao;

	@Autowired
	private PayableDAO payableDao;

	@Autowired
	private BudgetStandardOrderDAO bsoDao;

	@Autowired
	private BudgetNonStandardOrderDAO bnsoDao;

	@Override
	public String createOrderOperation(String json) {

		// 保存订单地接信息
		JSONObject obj = JSONObject.fromObject(json);
		String order_pk = obj.getString("order_pk");

		OrderDto order = orderDao.searchCOrderByPk(order_pk);
		String departure_date = order.getDeparture_date();
		int people_count = order.getAdult_count() + (null == order.getSpecial_count() ? 0 : order.getSpecial_count());
		String return_date = DateUtil.addDate(departure_date, order.getDays() - 1);

		BigDecimal product_cost = BigDecimal.ZERO;

		JSONArray suppliers = obj.getJSONArray("json");

		int sumcount = suppliers.size();

		for (int i = 0; i < suppliers.size(); i++) {
			JSONObject supplier = suppliers.getJSONObject(i);
			OrderSupplierBean osb = new OrderSupplierBean();

			int supplier_index = supplier.getInt("supplier_index");
			String supplier_pk = supplier.getString("supplier_pk");
			String supplier_product_name = supplier.getString("supplier_product_name");
			int supplier_product_days = supplier.getInt("supplier_product_days");
			BigDecimal supplier_cost = new BigDecimal(supplier.getString("supplier_cost"));

			String tourist_info = supplier.getString("tourist_info");
			String confirm_file_templet = supplier.getString("confirm_file_templet");

			osb.setSupplier_index(supplier_index);
			osb.setSupplier_employee_pk(supplier_pk);
			osb.setSupplier_product_name(supplier_product_name);
			osb.setDays(supplier_product_days);
			osb.setOrder_pk(order_pk);
			osb.setSupplier_cost(supplier_cost);
			osb.setTourist_info(tourist_info);

			String order_supplier_templet = DBCommonUtil.genPk() + ".doc";

			osb.setConfirm_file_templet(
					SimpletinyString.isEmpty(confirm_file_templet) || confirm_file_templet.equals("default") ? "default"
							: order_supplier_templet);

			if (!osb.getConfirm_file_templet().equals("default")) {
				// 保存地接社确认模板文件
				saveSupplierConfirmTemplet(confirm_file_templet, order_supplier_templet);
			}

			String order_supplier_pk = posDao.insert(osb);

			JSONArray infos = supplier.getJSONArray("info_json");

			int land_day = 0;
			int off_day = 0;

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

				if (j == 0) {
					land_day = pick_day;
				}
				if (j == infos.size() - 1) {
					off_day = send_day;
				}

				OrderSupplierInfoBean osib = new OrderSupplierInfoBean();

				osib.setOrder_supplier_pk(order_supplier_pk);
				osib.setInfo_index(info_index);
				osib.setPick_type(pick_type);
				osib.setPick_leg(pick_leg);
				osib.setPick_other(pick_other);
				osib.setPick_day(pick_day);
				osib.setPick_traffic(pick_traffic);
				osib.setPick_time(pick_time);
				osib.setPick_city(pick_city);
				osib.setPick_place(pick_place);
				osib.setSend_type(send_type);
				osib.setSend_leg(send_leg);
				osib.setSend_other(send_other);
				osib.setSend_day(send_day);
				osib.setSend_traffic(send_traffic);
				osib.setSend_time(send_time);
				osib.setSend_city(send_city);
				osib.setSend_place(send_place);
				posiDao.insert(osib);

			}
			// 生成产品订单操作
			// 订单地接产品成本
			product_cost = product_cost.add(supplier_cost);

			ProductOrderOperationBean poo = new ProductOrderOperationBean();
			poo.setSupplier_count(sumcount);
			poo.setTeam_number(order.getTeam_number());
			poo.setOperate_index(supplier_index + 1);
			poo.setSupplier_cost(supplier_cost);
			poo.setSupplier_product_name(supplier_product_name);
			poo.setPeople_count(people_count);
			String pick_date = DateUtil.addDate(departure_date, land_day - 1);
			String send_date = DateUtil.addDate(departure_date, off_day - 1);
			poo.setPick_date(pick_date);
			poo.setSend_date(send_date);
			poo.setSupplier_employee_pk(supplier_pk);
			poo.setOff_day(off_day);
			poo.setLand_day(land_day);
			dao.insert(poo);

			// 生成应付款
			PayableBean payable = new PayableBean();

			payable.setTeam_number(order.getTeam_number());
			payable.setFinal_flg("N");
			payable.setSupplier_employee_pk(supplier_pk);
			payable.setDeparture_date(departure_date);
			payable.setReturn_date(return_date);
			payable.setProduct(supplier_product_name);
			payable.setPeople_count(people_count);
			payable.setBudget_payable(supplier_cost);
			payable.setPaid(BigDecimal.ZERO);
			payable.setBudget_balance(supplier_cost);
			payable.setSales(order.getCreate_user_number());

			payableDao.insert(payable);
		}

		// 更新产品状态
		String standard_flg = order.getStandard_flg();
		if (standard_flg.equals("Y")) {
			BudgetStandardOrderBean bsOrder = bsoDao.selectByPrimaryKey(order_pk);
			bsOrder.setProduct_cost(product_cost);
			bsOrder.setOperate_flg("I");
			bsoDao.update(bsOrder);
		} else {
			BudgetNonStandardOrderBean bnsOrder = bnsoDao.selectByPrimaryKey(order_pk);
			bnsOrder.setProduct_cost(product_cost);
			bnsOrder.setOperate_flg("I");
			bnsoDao.update(bnsOrder);
		}
		return SUCCESS;
	}

	private void saveSupplierConfirmTemplet(String confirm_file_templet, String order_supplier_templet) {
		if (SimpletinyString.isEmpty(confirm_file_templet))
			return;

		String fileFolder = PropertiesUtil.getProperty("supplierConfirmTempletFolder");
		String destFolder = PropertiesUtil.getProperty("orderSupplierTempletFolder");

		File sourceFile = new File(fileFolder + File.separator + confirm_file_templet);
		File destfile = new File(destFolder + File.separator + order_supplier_templet);

		try {
			FileUtils.copyFile(sourceFile, destfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteOrderSupplier(String order_pk) {
		List<OrderSupplierBean> osbs = posDao.selectByOrderPk(order_pk);
		String destFolder = PropertiesUtil.getProperty("orderSupplierTempletFolder");
		for (OrderSupplierBean osb : osbs) {
			// 删除订单供应商详细信息
			posiDao.deleteByOrderSupplierPk(osb.getPk());
			// 删除上传的模板
			if (!osb.getConfirm_file_templet().equals(ResourcesConstants.DEFAULT)) {
				File destFile = new File(destFolder + File.separator + osb.getConfirm_file_templet());
				destFile.delete();
			}
		}

		// 删除订单供应商信息
		posDao.deleteByOrderPk(order_pk);
	}
}