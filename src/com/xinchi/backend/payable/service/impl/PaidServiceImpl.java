package com.xinchi.backend.payable.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.xinchi.backend.accounting.dao.PayApprovalDAO;
import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.payable.dao.PayableDAO;
import com.xinchi.backend.payable.service.PaidService;
import com.xinchi.backend.payable.service.PayableService;
import com.xinchi.backend.supplier.dao.SupplierDAO;
import com.xinchi.backend.supplier.dao.SupplierEmployeeDAO;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.SupplierBean;
import com.xinchi.bean.SupplierEmployeeBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
import com.xinchi.common.FileFolder;
import com.xinchi.common.FileUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.SimpletinyUser;
import com.xinchi.tools.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class PaidServiceImpl implements PaidService {

	@Autowired
	private PaidDAO dao;

	@Override
	public void insertWithPk(SupplierPaidDetailBean detail) {
		dao.insertWithPk(detail);
	}

	@Override
	public void insert(SupplierPaidDetailBean detail) {
		dao.insert(detail);
	}

	@Override
	public List<SupplierPaidDetailBean> getAllPaidsByPage(Page page) {
		return dao.getAllByPage(page);
	}

	@Override
	public String update(SupplierPaidDetailBean detail) {
		dao.update(detail);
		return "success";
	}

	@Override
	public List<SupplierPaidDetailBean> selectByRelatedPk(String related_pk) {

		return dao.selectSupplierPaidDetailByRelatedPk(related_pk);
	}

	@Override
	public SupplierPaidDetailBean selectPaidDetailByRelatedPk(String related_pk) {

		return dao.selectByRelatedPk(related_pk);
	}

	@Autowired
	private PayableDAO payableDao;
	@Autowired
	private PayApprovalDAO payApprovalDao;

	@Override
	public String rollBackPayApply(String related_pk) {
		SupplierPaidDetailBean options = new SupplierPaidDetailBean();
		options.setRelated_pk(related_pk);
		List<SupplierPaidDetailBean> paids = dao.selectByParam(options);

		for (SupplierPaidDetailBean paid : paids) {
			PayableBean payable = payableDao.selectByPk(paid.getPayable_pk());
			if (null != payable) {
				if (paid.getType().equals(ResourcesConstants.PAID_TYPE_STRIKE_OUT)) {
					payable.setPaid(payable.getPaid().add(paid.getMoney()));
					payable.setBudget_balance(payable.getBudget_balance().subtract(paid.getMoney()));
					if (payable.getFinal_flg().equals("Y")) {
						payable.setFinal_balance(payable.getFinal_balance().subtract(paid.getMoney()));
					}
				} else {
					payable.setPaid(payable.getPaid().subtract(paid.getMoney()));
					payable.setBudget_balance(payable.getBudget_balance().add(paid.getMoney()));
					if (payable.getFinal_flg().equals("Y")) {
						payable.setFinal_balance(payable.getFinal_balance().add(paid.getMoney()));
					}
				}
				payableDao.update(payable);
			}
			dao.deleteByPk(paid.getPk());
		}

		// 如果是待审批状态，则清除待审批支付
		PayApprovalBean pa = payApprovalDao.selectByBackPk(related_pk);
		if (null != pa && pa.getStatus().equals(ResourcesConstants.PAID_STATUS_ING)) {
			payApprovalDao.delete(pa.getPk());
		}
		return SUCCESS;
	}

	@Autowired
	private PayableService payableService;

	@Override
	public String applyStrike(String json) {

		JSONObject obj = JSONObject.fromObject(json);

		String comment = obj.getString("comment");

		JSONArray out_array = obj.getJSONArray("out_json");
		JSONArray in_array = obj.getJSONArray("in_json");

		String related_pk = DBCommonUtil.genPk();

		for (int i = 0; i < out_array.size(); i++) {
			SupplierPaidDetailBean current = new SupplierPaidDetailBean();

			current.setType(ResourcesConstants.PAID_TYPE_STRIKE_OUT);
			current.setStatus(ResourcesConstants.PAID_STATUS_ING);
			current.setRelated_pk(related_pk);

			JSONObject out_obj = JSONObject.fromObject(out_array.get(i));
			String payable_pk = out_obj.getString("payable_pk");
			String t = out_obj.getString("team_number");
			String r = out_obj.getString("out");
			String supplier_employee_pk = out_obj.getString("supplier_employee_pk");
			current.setTeam_number(t);
			current.setSupplier_employee_pk(supplier_employee_pk);
			current.setComment(comment);
			current.setPayable_pk(payable_pk);
			if (!SimpletinyString.isEmpty(r)) {
				BigDecimal out_money = new BigDecimal(r);
				current.setMoney(out_money);
			}

			dao.insert(current);
			current.setMoney(current.getMoney().negate());
			payableService.updatePayablePaid(current);

		}

		for (int i = 0; i < in_array.size(); i++) {
			SupplierPaidDetailBean current = new SupplierPaidDetailBean();

			current.setType(ResourcesConstants.PAID_TYPE_STRIKE_IN);
			current.setStatus(ResourcesConstants.PAID_STATUS_ING);
			current.setRelated_pk(related_pk);

			JSONObject in_obj = JSONObject.fromObject(in_array.get(i));
			String payable_pk = in_obj.getString("payable_pk");
			String t = in_obj.getString("team_number");
			String r = in_obj.getString("in");
			String supplier_employee_pk = in_obj.getString("supplier_employee_pk");
			current.setTeam_number(t);
			current.setSupplier_employee_pk(supplier_employee_pk);
			current.setComment(comment);
			current.setPayable_pk(payable_pk);

			if (!SimpletinyString.isEmpty(r)) {
				current.setMoney(new BigDecimal(r));
			}

			dao.insert(current);
			payableService.updatePayablePaid(current);
		}

		return SUCCESS;
	}

	@Override
	public String applyBackRecive(SupplierPaidDetailBean detail, String allot_json) {

		// 保存凭证文件
		String subFolder = detail.getTime().substring(0, 4) + File.separator + detail.getTime().substring(5, 7);
		FileUtil.saveFile(detail.getVoucher_file(), FileFolder.SUPPLIER_RECEIVED_VOUCHER.value(), subFolder);

		detail.setType(ResourcesConstants.PAID_TYPE_BACK);
		detail.setStatus(ResourcesConstants.PAID_STATUS_ING);

		JSONArray array = JSONArray.fromObject(allot_json);

		String[] pks = DBCommonUtil.genPks(array.size());
		String related_pk = DBCommonUtil.genPk();
		detail.setRelated_pk(related_pk);

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String payable_pk = obj.getString("payable_pk");
			String t = obj.getString("team_number");
			String r = obj.getString("received");
			String m = obj.getString("supplier_employee_name");
			String p = obj.getString("supplier_employee_pk");
			detail.setTeam_number(t);
			detail.setSupplier_employee_name(m);
			detail.setSupplier_employee_pk(p);
			detail.setPk(pks[i]);
			detail.setPayable_pk(payable_pk);
			if (!SimpletinyString.isEmpty(r)) {
				detail.setMoney((new BigDecimal(r)).multiply(new BigDecimal(-1)));
			}

			dao.insertWithPk(detail);
			payableService.updatePayablePaid(detail);
		}
		return SUCCESS;
	}

	@Override
	public String applyDeduct(SupplierPaidDetailBean detail, String allot_json) {
		detail.setType(ResourcesConstants.PAID_TYPE_DEDUCT);
		detail.setStatus(ResourcesConstants.PAID_STATUS_ING);

		JSONArray array = JSONArray.fromObject(allot_json);

		String[] pks = DBCommonUtil.genPks(array.size());
		detail.setRelated_pk(Joiner.on(",").join(pks));

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String payable_pk = obj.getString("payable_pk");
			String t = obj.getString("team_number");
			String r = obj.getString("deduct");
			String m = obj.getString("supplier_employee_name");
			String p = obj.getString("supplier_employee_pk");
			detail.setTeam_number(t);
			detail.setSupplier_employee_name(m);
			detail.setSupplier_employee_pk(p);
			detail.setPk(pks[i]);
			detail.setPayable_pk(payable_pk);

			if (!SimpletinyString.isEmpty(r)) {
				detail.setMoney((new BigDecimal(r)).multiply(new BigDecimal(-1)));
			}

			dao.insertWithPk(detail);
			payableService.updatePayablePaid(detail);
		}

		return SUCCESS;
	}

	@Autowired
	private SupplierEmployeeDAO supplierEmployeeDao;

	@Autowired
	private SupplierDAO supplierDao;

	@Override
	public String applyPay(SupplierPaidDetailBean detail, String allot_json) {
		JSONArray array = JSONArray.fromObject(allot_json);
		String related_pk = DBCommonUtil.genPk();
		String supplier_employee_pk = "";
		BigDecimal sum_money = BigDecimal.ZERO;
		// 获得支付详情数组
		List<SupplierPaidDetailBean> positive_details = new ArrayList<>();
		List<SupplierPaidDetailBean> negative_details = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			SupplierPaidDetailBean one_detail = new SupplierPaidDetailBean();
			one_detail.setRelated_pk(related_pk);
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String payable_pk = obj.getString("payable_pk");
			String t = obj.getString("team_number");
			String r = obj.getString("paid");
			String m = obj.getString("supplier_employee_name");
			String p = obj.getString("supplier_employee_pk");
			if (supplier_employee_pk.equals(""))
				supplier_employee_pk = p;
			one_detail.setAllot_money(detail.getAllot_money());
			one_detail.setLimit_time(detail.getLimit_time());
			one_detail.setTeam_number(t);
			one_detail.setSupplier_employee_name(m);
			one_detail.setSupplier_employee_pk(p);
			one_detail.setPayable_pk(payable_pk);

			BigDecimal paid = !SimpletinyString.isEmpty(r) ? new BigDecimal(r) : BigDecimal.ZERO;
			one_detail.setMoney(paid);
			sum_money = sum_money.add(paid);

			if (paid.compareTo(BigDecimal.ZERO) == 1) {
				positive_details.add(one_detail);
			} else if (paid.compareTo(BigDecimal.ZERO) == -1) {
				negative_details.add(one_detail);
			} else {
				// donoting
			}
		}

		if (negative_details.size() == 0) {
			for (SupplierPaidDetailBean d : positive_details) {
				d.setStatus(ResourcesConstants.PAID_STATUS_ING);
				d.setType(ResourcesConstants.PAID_TYPE_PAID);
				dao.insert(d);
				payableService.updatePayablePaid(d);
			}
		} else if (negative_details.size() > 0) {

			List<SupplierPaidDetailBean> details = processDetails(positive_details, negative_details);
			for (SupplierPaidDetailBean d : details) {
				dao.insert(d);
				if (d.getType().equals(ResourcesConstants.PAID_TYPE_STRIKE_OUT)) {
					d.setMoney(d.getMoney().negate());
				}
				payableService.updatePayablePaid(d);
			}
		}
		
		// 生成支出审批数据
		PayApprovalBean pa = new PayApprovalBean();
		SupplierEmployeeBean supplierEmployee = supplierEmployeeDao.selectByPrimaryKey(supplier_employee_pk);
		SupplierBean supplier = supplierDao.selectByPrimaryKey(supplierEmployee.getFinancial_body_pk());
		pa.setReceiver(supplier.getSupplier_short_name());
		pa.setMoney(sum_money);
		pa.setItem(ResourcesConstants.PAY_TYPE_DIJIE);
		pa.setStatus(ResourcesConstants.PAID_STATUS_ING);
		pa.setRelated_pk(related_pk);
		pa.setApply_user(SimpletinyUser.user().getUser_number());
		pa.setApply_time(DateUtil.getTimeMillis());
		pa.setLimit_time(detail.getLimit_time());
		pa.setBack_pk(related_pk);
		pa.setComment(detail.getComment());
		payApprovalDao.insert(pa);
		return SUCCESS;
	}

	private List<SupplierPaidDetailBean> processDetails(List<SupplierPaidDetailBean> positive_details,
			List<SupplierPaidDetailBean> negative_details) {
		List<SupplierPaidDetailBean> transfer_details = new ArrayList<>();
		int ne_index = 0;
		List<SupplierPaidDetailBean> need_remove = new ArrayList<>();
		for (SupplierPaidDetailBean po : positive_details) {
			BigDecimal need_amount = po.getMoney();
			while (need_amount.compareTo(BigDecimal.ZERO) == 1 && ne_index < negative_details.size()) {
				SupplierPaidDetailBean ne = negative_details.get(ne_index);
				BigDecimal can_provide = ne.getMoney().negate();
				if (can_provide.compareTo(BigDecimal.ZERO) < 1) {
					ne_index++;
					continue;
				}

				BigDecimal strike_amount = can_provide.min(need_amount);

				SupplierPaidDetailBean in_detail = new SupplierPaidDetailBean();
				in_detail.setRelated_pk(po.getRelated_pk());
				in_detail.setTeam_number(po.getTeam_number());
				in_detail.setSupplier_employee_name(po.getSupplier_employee_name());
				in_detail.setSupplier_employee_pk(po.getSupplier_employee_pk());
				in_detail.setPayable_pk(po.getPayable_pk());
				in_detail.setMoney(strike_amount);
				in_detail.setComment("冲账");
				in_detail.setStatus(ResourcesConstants.PAID_STATUS_ING);
				in_detail.setType(ResourcesConstants.PAID_TYPE_STRIKE_IN);

				SupplierPaidDetailBean out_detail = new SupplierPaidDetailBean();
				out_detail.setRelated_pk(ne.getRelated_pk());
				out_detail.setTeam_number(ne.getTeam_number());
				out_detail.setSupplier_employee_name(ne.getSupplier_employee_name());
				out_detail.setSupplier_employee_pk(ne.getSupplier_employee_pk());
				out_detail.setPayable_pk(ne.getPayable_pk());
				out_detail.setMoney(strike_amount);
				out_detail.setComment("冲账");
				out_detail.setStatus(ResourcesConstants.PAID_STATUS_ING);
				out_detail.setType(ResourcesConstants.PAID_TYPE_STRIKE_OUT);

				transfer_details.add(in_detail);
				transfer_details.add(out_detail);

				need_amount = need_amount.subtract(strike_amount);
				can_provide = can_provide.subtract(strike_amount);
				
				ne.setMoney(can_provide.negate());
				if (can_provide.compareTo(BigDecimal.ZERO) < 1) {
					ne_index++;
				}
			}
			po.setMoney(need_amount);
			if (need_amount.compareTo(BigDecimal.ZERO) < 1) {
				need_remove.add(po);
			}
		}
		positive_details.removeAll(need_remove);
		for (SupplierPaidDetailBean po : positive_details) {
			po.setStatus(ResourcesConstants.PAID_STATUS_ING);
			po.setType(ResourcesConstants.PAID_TYPE_PAID);
		}

		transfer_details.addAll(positive_details);
		return transfer_details;
	}
}
