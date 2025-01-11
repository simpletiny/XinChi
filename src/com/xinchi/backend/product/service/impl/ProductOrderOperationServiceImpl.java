package com.xinchi.backend.product.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.OrderDAO;
import com.xinchi.backend.order.dao.OrderReportDAO;
import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.payable.dao.PayableDAO;
import com.xinchi.backend.payable.dao.PayableOrderDAO;
import com.xinchi.backend.product.dao.ProductOrderDAO;
import com.xinchi.backend.product.dao.ProductOrderOperationDAO;
import com.xinchi.backend.product.dao.ProductOrderSupplierDAO;
import com.xinchi.backend.product.dao.ProductOrderSupplierInfoDAO;
import com.xinchi.backend.product.dao.ProductOrderSupplierSaleOrderDAO;
import com.xinchi.backend.product.dao.ProductOrderSupplierSaleOrderNameInfoDAO;
import com.xinchi.backend.product.dao.ProductOrderTeamNumberDAO;
import com.xinchi.backend.product.service.ProductOrderOperationService;
import com.xinchi.backend.product.service.ProductSupplierService;
import com.xinchi.bean.DropOffBean;
import com.xinchi.bean.OrderDto;
import com.xinchi.bean.OrderSupplierBean;
import com.xinchi.bean.OrderSupplierInfoBean;
import com.xinchi.bean.OrderSupplierSaleOrderBean;
import com.xinchi.bean.OrderSupplierSaleOrderNameInfoBean;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.PayableOrderBean;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.bean.ProductOrderOperationBean;
import com.xinchi.bean.ProductOrderTeamNumberBean;
import com.xinchi.bean.ProductSupplierBean;
import com.xinchi.bean.SaleOrderBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.bean.TeamReportBean;
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
	private PaidDAO paidDao;

	@Autowired
	private ProductSupplierService psService;

	@Autowired
	private ProductOrderDAO productOrderDao;

	@Autowired
	private PayableOrderDAO payableOrderDao;

	@Autowired
	private ProductOrderSupplierSaleOrderDAO productOrderSupplierSaleOrderDao;

	@Autowired
	private ProductOrderSupplierSaleOrderNameInfoDAO productOrderSupplierSaleOrderNameInfoDao;

	@Override
	public String createOrderOperation(String json) {

		// 保存订单地接信息
		JSONObject obj = JSONObject.fromObject(json);
		String order_number = obj.getString("order_number");
		ProductOrderBean product_order = productOrderDao.selectByOrderNumber(order_number);

		String departure_date = product_order.getDeparture_date();
		int people_count = product_order.getAdult_count()
				+ (null == product_order.getSpecial_count() ? 0 : product_order.getSpecial_count());
		String return_date = DateUtil.addDate(departure_date, product_order.getDays() - 1);

		String passenger_captain = product_order.getPassenger_captain();

		JSONArray suppliers = obj.getJSONArray("supplier_infos");

		int sumcount = suppliers.size();

		Map<String, BigDecimal> team_cost = new HashMap<>();
		for (int i = 0; i < suppliers.size(); i++) {
			JSONObject supplier = suppliers.getJSONObject(i);
			OrderSupplierBean osb = new OrderSupplierBean();
			int supplier_index = supplier.getInt("supplier_index");

			String supplier_pk = supplier.getString("supplier_pk");

			String supplier_product_name = supplier.getString("supplier_product_name");
			int supplier_product_days = supplier.getInt("supplier_product_days");
			BigDecimal supplier_cost = new BigDecimal(supplier.getString("supplier_cost"));
			String supplier_pick_date = supplier.getString("pick_date");
			String tourist_info = supplier.getString("tourist_info");
			String confirm_file_templet = supplier.getString("confirm_file_templet");
			String supplier_treat_comment = supplier.getString("treat_comment");
			String payable_comment = supplier.getString("payable_comment");

			osb.setPick_date(supplier_pick_date);
			osb.setTreat_comment(supplier_treat_comment);
			osb.setPayable_comment(payable_comment);

			osb.setSupplier_index(supplier_index);
			osb.setSupplier_employee_pk(supplier_pk);
			osb.setSupplier_product_name(supplier_product_name);
			osb.setDays(supplier_product_days);
			osb.setOrder_pk(product_order.getPk());
			osb.setSupplier_cost(supplier_cost);
			osb.setTourist_info(tourist_info);

			String order_supplier_templet = DBCommonUtil.genPk() + ".doc";

			osb.setConfirm_file_templet(
					SimpletinyString.isEmpty(confirm_file_templet) || confirm_file_templet.equals("default") ? "default"
							: order_supplier_templet);

			if (!osb.getConfirm_file_templet().equals("default")) {
				String copy_type = "new";
				ProductSupplierBean option = new ProductSupplierBean();
				option.setProduct_pk(product_order.getProduct_pk());
				option.setSupplier_employee_pk(supplier_pk);
				List<ProductSupplierBean> pss = psService.selectByParam(option);

				if (null != pss && pss.size() > 0) {
					ProductSupplierBean ps = pss.get(0);
					if (confirm_file_templet.equals(ps.getConfirm_file_templet())) {
						copy_type = "old";
					}
				}
				// 保存地接社确认模板文件
				saveSupplierConfirmTemplet(confirm_file_templet, order_supplier_templet, copy_type);
			}

			String order_supplier_pk = posDao.insert(osb);

			// 接送信息
			JSONArray leg_infos = supplier.getJSONArray("leg_infos");

			int land_day = 0;
			int off_day = 0;

			for (int j = 0; j < leg_infos.size(); j++) {

				JSONObject info = leg_infos.getJSONObject(j);

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
				if (j == leg_infos.size() - 1) {
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

			// 销售订单信息
			JSONArray order_infos = supplier.getJSONArray("order_infos");

			for (int j = 0; j < order_infos.size(); j++) {
				JSONObject info = order_infos.getJSONObject(j);
				String team_number = info.getString("team_number");
				String contact_way = info.getString("contact_way");
				String sale_receivable_comment = info.getString("sale_receivable_comment");
				String hotel_comment = info.getString("hotel_comment");
				String sale_treat_comment = info.getString("sale_treat_comment");

				OrderSupplierSaleOrderBean ossob = new OrderSupplierSaleOrderBean();

				ossob.setTeam_number(team_number);
				ossob.setContact_way(contact_way);
				ossob.setReceivable_comment(sale_receivable_comment);
				ossob.setHotel_comment(hotel_comment);
				ossob.setTreat_comment(sale_treat_comment);
				ossob.setBase_pk(order_supplier_pk);
				String osso_pk = productOrderSupplierSaleOrderDao.insert(ossob);

				BigDecimal supplier_team_cost = BigDecimal.ZERO;
				// 名单信息
				JSONArray name_infos = info.getJSONArray("name_infos");
				for (int k = 0; k < name_infos.size(); k++) {
					JSONObject name_info = name_infos.getJSONObject(k);
					String name_pk = name_info.getString("name_pk");
					BigDecimal name_price = new BigDecimal(name_info.getString("price"));
					OrderSupplierSaleOrderNameInfoBean ossonib = new OrderSupplierSaleOrderNameInfoBean();
					ossonib.setName_pk(name_pk);
					ossonib.setPrice(name_price);
					ossonib.setBase_pk(osso_pk);
					productOrderSupplierSaleOrderNameInfoDao.insert(ossonib);

					supplier_team_cost = supplier_team_cost.add(name_price);
				}

				String team_cost_key = team_number + ";" + supplier_pk;
				team_cost.put(team_cost_key, supplier_team_cost);
			}

			Map<String, BigDecimal> product_cost = new HashMap<>();
			for (Map.Entry<String, BigDecimal> entry : team_cost.entrySet()) {
				String key = entry.getKey();
				BigDecimal value = entry.getValue();

				String[] keys = key.split(";");
				// 保存每个订单的应付款
				PayableOrderBean pob = new PayableOrderBean();
				pob.setTeam_number(keys[0]);
				pob.setSupplier_employee_pk(keys[1]);
				pob.setBudget_payable(value);
				payableOrderDao.insert(pob);

				if (product_cost.containsKey(keys[0])) {
					product_cost.put(keys[0], product_cost.get(keys[0]).add(value));
				} else {
					product_cost.put(keys[0], value);
				}
			}
			for (Map.Entry<String, BigDecimal> entry : product_cost.entrySet()) {
				String key = entry.getKey();
				BigDecimal value = entry.getValue();

				OrderDto order = orderDao.selectByTeamNumber(key);
				SaleOrderBean sale_order = new SaleOrderBean();
				sale_order.setPk(order.getPk());
				sale_order.setProduct_cost(value);
				sale_order.setOperate_flg(SimpletinyString.replaceCharFromLeft(order.getOperate_flg(),
						ResourcesConstants.ORDER_OPERATE_STATUS_YES));
				orderDao.update(sale_order);
			}

			// 生成产品订单操作
			// 订单地接产品成本
			ProductOrderOperationBean poo = new ProductOrderOperationBean();
			poo.setSupplier_count(sumcount);
			poo.setTeam_number(order_number);
			poo.setOperate_index(supplier_index + 1);
			poo.setSupplier_cost(supplier_cost);
			poo.setSupplier_product_name(supplier_product_name);
			poo.setAdult_count(product_order.getAdult_count());
			poo.setSpecial_count(product_order.getSpecial_count());
			String pick_date = DateUtil.addDate(departure_date, land_day - 1);
			String send_date = DateUtil.addDate(departure_date, off_day - 1);
			poo.setPick_date(pick_date);
			poo.setSend_date(send_date);
			poo.setSupplier_employee_pk(supplier_pk);
			poo.setOff_day(off_day);
			poo.setLand_day(land_day);
			poo.setPassenger_captain(passenger_captain);
			dao.insert(poo);

			// 生成应付款
			PayableBean payable = new PayableBean();
			payable = new PayableBean();
			payable.setTeam_number(order_number);
			payable.setFinal_flg("N");
			payable.setSupplier_employee_pk(supplier_pk);
			payable.setDeparture_date(departure_date);
			payable.setReturn_date(return_date);
			payable.setProduct(supplier_product_name);
			payable.setPeople_count(people_count);
			payable.setBudget_payable(supplier_cost);
			payable.setSales(product_order.getProduct_manager_number());
			// 计算已付款
			SupplierPaidDetailBean option = new SupplierPaidDetailBean();
			option.setTeam_number(order_number);
			option.setSupplier_employee_pk(supplier_pk);

			List<SupplierPaidDetailBean> paids = paidDao.selectByParam(option);
			if (null != paids && paids.size() > 0) {
				BigDecimal alreadPaid = BigDecimal.ZERO;
				for (SupplierPaidDetailBean paid : paids) {
					alreadPaid = alreadPaid.add(paid.getMoney());
				}
				payable.setPaid(alreadPaid);
				payable.setBudget_balance(supplier_cost.subtract(alreadPaid));
			} else {
				payable.setPaid(BigDecimal.ZERO);
				payable.setBudget_balance(supplier_cost);
			}

			payableDao.insert(payable);

		}

		// 更新产品订单
		product_order.setStatus("Y");
		productOrderDao.update(product_order);
		return SUCCESS;
	}

	@Override
	public String updateOrderOperation(String json) {
		// 保存订单地接信息
		JSONObject obj = JSONObject.fromObject(json);
		String order_number = obj.getString("order_number");

		ProductOrderBean product_order = productOrderDao.selectByOrderNumber(order_number);
		/* 删除之前的数据 start */
		List<OrderSupplierBean> oldOrderSuppliers = posDao.selectByOrderPk(product_order.getPk());

		List<String> oldTemplets = new ArrayList<>();
		for (OrderSupplierBean oldOs : oldOrderSuppliers) {

			// 删除之前的order_supplier_info
			posiDao.deleteByOrderSupplierPk(oldOs.getPk());

			List<OrderSupplierSaleOrderBean> oldOrderSupplierSaleOrders = productOrderSupplierSaleOrderDao
					.selectByBasePk(oldOs.getPk());
			// 删除之前的order_supplier_sale_order_name_info
			for (OrderSupplierSaleOrderBean osso : oldOrderSupplierSaleOrders) {
				productOrderSupplierSaleOrderNameInfoDao.deleteByBasePk(osso.getPk());
			}

			// 删除之前的order_supplier_sale_order
			productOrderSupplierSaleOrderDao.deleteByBasePk(oldOs.getPk());
			oldTemplets.add(oldOs.getConfirm_file_templet());
		}
		// 删除之前的order_supplier
		posDao.deleteByOrderPk(product_order.getPk());
		// 删除之前的 product_order_operation
		dao.deleteByTeamNumber(order_number);
		// 删除每个订单的应付款
		List<ProductOrderTeamNumberBean> potns = productOrderTeamNumberDao.selectByOrderNumber(order_number);
		for (ProductOrderTeamNumberBean potn : potns) {
			payableOrderDao.deleteByTeamNumber(potn.getTeam_number());
		}
		// 删除之前的 payable
		payableDao.deleteByTeamNumber(order_number);
		/* 删除之前的数据 end */

		String departure_date = product_order.getDeparture_date();
		int people_count = product_order.getAdult_count()
				+ (null == product_order.getSpecial_count() ? 0 : product_order.getSpecial_count());
		String return_date = DateUtil.addDate(departure_date, product_order.getDays() - 1);

		String passenger_captain = product_order.getPassenger_captain();

		JSONArray suppliers = obj.getJSONArray("supplier_infos");

		int sumcount = suppliers.size();

		Map<String, BigDecimal> team_cost = new HashMap<>();
		for (int i = 0; i < suppliers.size(); i++) {
			JSONObject supplier = suppliers.getJSONObject(i);
			OrderSupplierBean osb = new OrderSupplierBean();
			int supplier_index = supplier.getInt("supplier_index");

			String supplier_pk = supplier.getString("supplier_pk");

			String supplier_product_name = supplier.getString("supplier_product_name");
			int supplier_product_days = supplier.getInt("supplier_product_days");
			BigDecimal supplier_cost = new BigDecimal(supplier.getString("supplier_cost"));
			String supplier_pick_date = supplier.getString("pick_date");
			String tourist_info = supplier.getString("tourist_info");
			String confirm_file_templet = supplier.getString("confirm_file_templet");
			String supplier_treat_comment = supplier.getString("treat_comment");
			String payable_comment = supplier.getString("payable_comment");

			osb.setPick_date(supplier_pick_date);
			osb.setTreat_comment(supplier_treat_comment);
			osb.setPayable_comment(payable_comment);

			osb.setSupplier_index(supplier_index);
			osb.setSupplier_employee_pk(supplier_pk);
			osb.setSupplier_product_name(supplier_product_name);
			osb.setDays(supplier_product_days);
			osb.setOrder_pk(product_order.getPk());
			osb.setSupplier_cost(supplier_cost);
			osb.setTourist_info(tourist_info);

			String order_supplier_templet = DBCommonUtil.genPk() + ".doc";

			osb.setConfirm_file_templet(
					SimpletinyString.isEmpty(confirm_file_templet) || confirm_file_templet.equals("default") ? "default"
							: order_supplier_templet);

			if (!osb.getConfirm_file_templet().equals("default") && !oldTemplets.contains(confirm_file_templet)) {
				String copy_type = "new";
				ProductSupplierBean option = new ProductSupplierBean();
				option.setProduct_pk(product_order.getProduct_pk());
				option.setSupplier_employee_pk(supplier_pk);
				List<ProductSupplierBean> pss = psService.selectByParam(option);

				if (null != pss && pss.size() > 0) {
					ProductSupplierBean ps = pss.get(0);
					if (confirm_file_templet.equals(ps.getConfirm_file_templet())) {
						copy_type = "old";
					}
				}
				// 保存地接社确认模板文件
				saveSupplierConfirmTemplet(confirm_file_templet, order_supplier_templet, copy_type);
			}

			String order_supplier_pk = posDao.insert(osb);

			// 接送信息
			JSONArray leg_infos = supplier.getJSONArray("leg_infos");

			int land_day = 0;
			int off_day = 0;

			// 删除之前的order_supplier_info

			for (int j = 0; j < leg_infos.size(); j++) {

				JSONObject info = leg_infos.getJSONObject(j);

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
				if (j == leg_infos.size() - 1) {
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

			// 销售订单信息
			JSONArray order_infos = supplier.getJSONArray("order_infos");

			for (int j = 0; j < order_infos.size(); j++) {
				JSONObject info = order_infos.getJSONObject(j);
				String team_number = info.getString("team_number");
				String contact_way = info.getString("contact_way");
				String sale_receivable_comment = info.getString("sale_receivable_comment");
				String hotel_comment = info.getString("hotel_comment");
				String sale_treat_comment = info.getString("sale_treat_comment");

				OrderSupplierSaleOrderBean ossob = new OrderSupplierSaleOrderBean();

				ossob.setTeam_number(team_number);
				ossob.setContact_way(contact_way);
				ossob.setReceivable_comment(sale_receivable_comment);
				ossob.setHotel_comment(hotel_comment);
				ossob.setTreat_comment(sale_treat_comment);
				ossob.setBase_pk(order_supplier_pk);
				String osso_pk = productOrderSupplierSaleOrderDao.insert(ossob);

				BigDecimal supplier_team_cost = BigDecimal.ZERO;

				// 名单信息
				JSONArray name_infos = info.getJSONArray("name_infos");
				for (int k = 0; k < name_infos.size(); k++) {
					JSONObject name_info = name_infos.getJSONObject(k);
					String name_pk = name_info.getString("name_pk");
					BigDecimal name_price = new BigDecimal(name_info.getString("price"));
					OrderSupplierSaleOrderNameInfoBean ossonib = new OrderSupplierSaleOrderNameInfoBean();
					ossonib.setName_pk(name_pk);
					ossonib.setPrice(name_price);
					ossonib.setBase_pk(osso_pk);
					productOrderSupplierSaleOrderNameInfoDao.insert(ossonib);

					supplier_team_cost = supplier_team_cost.add(name_price);
				}

				String team_cost_key = team_number + ";" + supplier_pk;
				team_cost.put(team_cost_key, supplier_team_cost);
			}

			Map<String, BigDecimal> product_cost = new HashMap<>();
			for (Map.Entry<String, BigDecimal> entry : team_cost.entrySet()) {
				String key = entry.getKey();
				BigDecimal value = entry.getValue();

				String[] keys = key.split(";");

				// 保存每个订单的应付款
				PayableOrderBean pob = new PayableOrderBean();
				pob.setTeam_number(keys[0]);
				pob.setSupplier_employee_pk(keys[1]);
				pob.setBudget_payable(value);
				payableOrderDao.insert(pob);

				if (product_cost.containsKey(keys[0])) {
					product_cost.put(keys[0], product_cost.get(keys[0]).add(value));
				} else {
					product_cost.put(keys[0], value);
				}
			}
			for (Map.Entry<String, BigDecimal> entry : product_cost.entrySet()) {
				String key = entry.getKey();
				BigDecimal value = entry.getValue();

				// 更新订单产品费用
				OrderDto order = orderDao.selectByTeamNumber(key);
				SaleOrderBean sale_order = new SaleOrderBean();
				sale_order.setPk(order.getPk());
				sale_order.setProduct_cost(value);
				orderDao.update(sale_order);
			}

			// 生成产品订单操作
			ProductOrderOperationBean poo = new ProductOrderOperationBean();
			poo.setSupplier_count(sumcount);
			poo.setTeam_number(order_number);
			poo.setOperate_index(supplier_index + 1);
			poo.setSupplier_cost(supplier_cost);
			poo.setSupplier_product_name(supplier_product_name);
			poo.setAdult_count(product_order.getAdult_count());
			poo.setSpecial_count(product_order.getSpecial_count());
			String pick_date = DateUtil.addDate(departure_date, land_day - 1);
			String send_date = DateUtil.addDate(departure_date, off_day - 1);
			poo.setPick_date(pick_date);
			poo.setSend_date(send_date);
			poo.setSupplier_employee_pk(supplier_pk);
			poo.setOff_day(off_day);
			poo.setLand_day(land_day);
			poo.setPassenger_captain(passenger_captain);
			dao.insert(poo);

			// 删除之前的应付款
			// 生成应付款
			PayableBean payable = new PayableBean();
			payable = new PayableBean();
			payable.setTeam_number(order_number);
			payable.setFinal_flg("N");
			payable.setSupplier_employee_pk(supplier_pk);
			payable.setDeparture_date(departure_date);
			payable.setReturn_date(return_date);
			payable.setProduct(supplier_product_name);
			payable.setPeople_count(people_count);
			payable.setBudget_payable(supplier_cost);
			payable.setSales(product_order.getProduct_manager_number());
			// 计算已付款
			SupplierPaidDetailBean option = new SupplierPaidDetailBean();
			option.setTeam_number(order_number);
			option.setSupplier_employee_pk(supplier_pk);

			List<SupplierPaidDetailBean> paids = paidDao.selectByParam(option);
			if (null != paids && paids.size() > 0) {
				BigDecimal alreadPaid = BigDecimal.ZERO;
				for (SupplierPaidDetailBean paid : paids) {
					alreadPaid = alreadPaid.add(paid.getMoney());
				}
				payable.setPaid(alreadPaid);
				payable.setBudget_balance(supplier_cost.subtract(alreadPaid));
			} else {
				payable.setPaid(BigDecimal.ZERO);
				payable.setBudget_balance(supplier_cost);
			}

			payableDao.insert(payable);
		}
		return SUCCESS;
	}

	private void saveSupplierConfirmTemplet(String confirm_file_templet, String order_supplier_templet,
			String copy_type) {
		if (SimpletinyString.isEmpty(confirm_file_templet))
			return;

		String fileFolder = "";
		if (copy_type.equals("new")) {
			fileFolder = PropertiesUtil.getProperty("tempUploadFolder");
		} else {
			fileFolder = PropertiesUtil.getProperty("supplierConfirmTempletFolder");
		}

		String destFolder = PropertiesUtil.getProperty("orderSupplierTempletFolder");

		File sourceFile = new File(fileFolder + File.separator + confirm_file_templet);
		File destfile = new File(destFolder + File.separator + order_supplier_templet);

		try {
			FileUtils.copyFile(sourceFile, destfile);
			if (copy_type.equals("new")) {
				sourceFile.delete();
			}
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

	@Autowired
	private ProductOrderTeamNumberDAO productOrderTeamNumberDao;

	@Override
	public String deleteOperation(String team_numbers) {
		String[] t_ns = team_numbers.split(",");
		for (String t_n : t_ns) {

			// 如果是老数据，订单号是团号的，T0****
			if (t_n.startsWith("N")) {
				OrderDto order = orderDao.selectByTeamNumber(t_n);

				String order_pk = order.getPk();
				SaleOrderBean sale_order = new SaleOrderBean();
				sale_order.setPk(order_pk);
				sale_order.setOperate_flg(SimpletinyString.replaceCharFromLeft(order.getOperate_flg(),
						ResourcesConstants.ORDER_OPERATE_STATUS_ORDERED));
				orderDao.update(sale_order);

				dao.deleteByTeamNumber(t_n);
				// 删除订单地接维护信息
				deleteOrderSupplier(order_pk);
				// 删除应付款
				payableDao.deleteByTeamNumber(t_n);
			}
			// 新产品操作下的订单号，开头为P的
			else if (t_n.startsWith("P")) {
				// 需要判断应付款是否已经付款
				SupplierPaidDetailBean option = new SupplierPaidDetailBean();
				option.setTeam_number(t_n);
				List<SupplierPaidDetailBean> paids = paidDao.selectByParam(option);
				if (null != paids && paids.size() > 0) {
					return "haspaid";
				}

				List<ProductOrderTeamNumberBean> potns = productOrderTeamNumberDao.selectByOrderNumber(t_n);
				for (ProductOrderTeamNumberBean potn : potns) {
					// 删除每个订单的应付款
					payableOrderDao.deleteByTeamNumber(potn.getTeam_number());

					OrderDto order = orderDao.selectByTeamNumber(potn.getTeam_number());

					// 更新订单状态
					SaleOrderBean sale_order = new SaleOrderBean();
					sale_order.setPk(order.getPk());
					sale_order.setOperate_flg(SimpletinyString.replaceCharFromLeft(order.getOperate_flg(),
							ResourcesConstants.ORDER_OPERATE_STATUS_ORDERED));
					orderDao.update(sale_order);
				}

				// 删除订单供应商信息,删除订单供应商维护信息
				ProductOrderBean product_order = productOrderDao.selectByOrderNumber(t_n);
				deleteOrderSupplier(product_order.getPk());

				// 删除操作中订单
				dao.deleteByTeamNumber(t_n);
				// 删除应付款
				payableDao.deleteByTeamNumber(t_n);

				product_order.setStatus("N");
				productOrderDao.update(product_order);
			}
		}
		return SUCCESS;
	}

	@Override
	public List<DropOffBean> searchDropOff(DropOffBean drop_off) {
		return dao.selectDropOff(drop_off);
	}

	@Override
	public String finalOperation(String operate_pk, BigDecimal final_supplier_cost, String json) {
		ProductOrderOperationBean operation = dao.selectByPrimaryKey(operate_pk);
		operation.setStatus("F");
		operation.setFinal_supplier_cost(final_supplier_cost);

		dao.update(operation);

		// 更新应付款
		PayableBean payable_option = new PayableBean();
		payable_option.setTeam_number(operation.getTeam_number());
		payable_option.setSupplier_employee_pk(operation.getSupplier_employee_pk());
		List<PayableBean> payables = payableDao.selectByParam(payable_option);
		if (null != payables && payables.size() > 0) {
			PayableBean payable = payables.get(0);
			payable.setFinal_flg("Y");
			payable.setFinal_payable(final_supplier_cost);
			payable.setFinal_balance(final_supplier_cost.subtract(payable.getPaid()));
			payableDao.update(payable);
		}
		if (operation.getTeam_number().startsWith("P")) {

			// 更新每个订单的应付款
			JSONArray arr = JSONArray.fromObject(json);
			for (int i = 0; i < arr.size(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				String team_number = obj.getString("team_number");
				BigDecimal final_payable = new BigDecimal(obj.getString("team_payable"));
				PayableOrderBean option = new PayableOrderBean();
				option.setTeam_number(team_number);
				option.setSupplier_employee_pk(operation.getSupplier_employee_pk());

				List<PayableOrderBean> pos = payableOrderDao.selectByParam(option);

				if (pos != null && pos.size() > 0) {
					PayableOrderBean po = pos.get(0);
					po.setFinal_payable(final_payable);
					po.setFinal_flg("Y");
					payableOrderDao.update(po);

				}
			}

		} else if (operation.getTeam_number().startsWith("N")) {
			PayableOrderBean option = new PayableOrderBean();
			option.setTeam_number(operation.getTeam_number());
			option.setSupplier_employee_pk(operation.getSupplier_employee_pk());

			List<PayableOrderBean> pos = payableOrderDao.selectByParam(option);

			if (pos != null && pos.size() > 0) {
				PayableOrderBean po = pos.get(0);
				po.setFinal_payable(final_supplier_cost);
				po.setFinal_flg("Y");
				payableOrderDao.update(po);

			}
		}
		return SUCCESS;
	}

	@Autowired
	private OrderReportDAO orderReportDao;

	@Override
	public String rollBackOperation(String operate_pk) {
		ProductOrderOperationBean operation = dao.selectByPrimaryKey(operate_pk);

		List<ProductOrderTeamNumberBean> potns = new ArrayList<ProductOrderTeamNumberBean>();
		potns = productOrderTeamNumberDao.selectByOrderNumber(operation.getTeam_number());
		// 检验
		// 单团是否已审核
		for (ProductOrderTeamNumberBean potn : potns) {
			TeamReportBean tr = orderReportDao.selectTeamReportByTn(potn.getTeam_number());
			if (tr.getApproved().equals("Y")) {
				return "approved";
			}
		}

		// 更新产品订单操作
		operation.setStatus("Y");
		operation.setFinal_supplier_cost(BigDecimal.ZERO);

		dao.update(operation);

		// 更新应付款
		PayableBean payable_option = new PayableBean();
		payable_option.setTeam_number(operation.getTeam_number());
		payable_option.setSupplier_employee_pk(operation.getSupplier_employee_pk());
		List<PayableBean> payables = payableDao.selectByParam(payable_option);
		if (null != payables && payables.size() > 0) {
			PayableBean payable = payables.get(0);
			payable.setFinal_flg("N");
			payable.setFinal_payable(BigDecimal.ZERO);
			payable.setFinal_balance(BigDecimal.ZERO);
			payableDao.update(payable);
		}

		// 更新每个订单的应付款

		if (null == potns || potns.size() == 0)
			return SUCCESS;

		for (ProductOrderTeamNumberBean potn : potns) {
			PayableOrderBean option = new PayableOrderBean();
			option.setTeam_number(potn.getTeam_number());
			option.setSupplier_employee_pk(operation.getSupplier_employee_pk());
			List<PayableOrderBean> pos = payableOrderDao.selectByParam(option);

			if (null != pos && pos.size() > 0) {
				PayableOrderBean po = pos.get(0);
				po.setFinal_flg("N");
				po.setFinal_payable(BigDecimal.ZERO);
				payableOrderDao.update(po);
			}
		}

		return SUCCESS;

	}

	@Override
	public List<PayableOrderBean> selectPayableOrderByParam(PayableOrderBean option) {
		return payableOrderDao.selectByParam(option);
	}

	@Override
	public List<DropOffBean> selectDropOffByPage(Page<DropOffBean> page) {

		return dao.selectDropOffByPage(page);
	}

	@Override
	public List<PayableOrderBean> selectPayableOrderByTeamNumber(String team_number) {
		return payableOrderDao.selectByTeamNumber(team_number);
	}

}