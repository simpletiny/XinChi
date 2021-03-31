package com.xinchi.backend.payable.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.AccPaidDAO;
import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.finance.dao.PaymentDetailDAO;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.payable.dao.AirTicketPaidDetailDAO;
import com.xinchi.backend.payable.dao.AirTicketPayableDAO;
import com.xinchi.backend.payable.service.AirTicketPaidDetailService;
import com.xinchi.backend.supplier.dao.DepositTicketPaidDAO;
import com.xinchi.backend.supplier.dao.SupplierDepositDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPaidDto;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.bean.CardBean;
import com.xinchi.bean.DepositTicketPaidBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.SupplierDepositBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.DBCommonUtil;
import com.xinchi.common.DateUtil;
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
public class AirTicketPaidDetailServiceImpl implements AirTicketPaidDetailService {

	@Autowired
	private AirTicketPaidDetailDAO dao;

	@Override
	public void insert(AirTicketPaidDetailBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(AirTicketPaidDetailBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public AirTicketPaidDetailBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<AirTicketPaidDetailBean> selectByParam(AirTicketPaidDetailBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<AirTicketPaidDto> selectByPage(Page<AirTicketPaidDto> page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<AirTicketPaidDetailBean> selectByRelatedPk(String related_pk) {
		return dao.selectByRelatedPk(related_pk);
	}

	@Autowired
	private AccPaidDAO accPaidDao;

	@Autowired
	private PaymentDetailDAO paymentDetailDao;

	@Autowired
	private AirTicketPayableDAO airTicketPayableDao;

	@Autowired
	private PaymentDetailService paymentDetailService;

	@Override
	public String rollBackPayApply(String related_pk) {

		List<AirTicketPaidDetailBean> details = dao.selectByRelatedPk(related_pk);

		for (AirTicketPaidDetailBean detail : details) {
			// 更新应付款的尾款和已付款
			String payable_pk = detail.getBase_pk();
			AirTicketPayableBean airTicketPayable = airTicketPayableDao.selectByPrimaryKey(payable_pk);
			airTicketPayable.setPaid(airTicketPayable.getPaid().subtract(detail.getMoney()));

			airTicketPayable.setBudget_balance(airTicketPayable.getBudget_balance().add(detail.getMoney()));
			if (airTicketPayable.getFinal_flg().equals("Y")) {
				airTicketPayable.setFinal_balance(airTicketPayable.getFinal_balance().add(detail.getMoney()));
			}
			airTicketPayableDao.update(airTicketPayable);

			// 删除要打回的机票往来详情
			dao.delete(detail.getPk());

		}

		// 删除待支付信息
		List<WaitingForPaidBean> wfps = accPaidDao.selectByRelatedPk(related_pk);
		for (WaitingForPaidBean wfp : wfps) {
			accPaidDao.deleteByPk(wfp.getPk());
		}

		// 删除银行流水的支出信息
		String voucher_number = details.get(0).getVoucher_number();
		if (SimpletinyString.isEmpty(voucher_number))
			return SUCCESS;
		String[] voucher_numbers = voucher_number.split(",");
		String fileFolder = PropertiesUtil.getProperty("voucherFileFolder");

		for (String v_n : voucher_numbers) {
			List<PaymentDetailBean> paymentDetails = paymentDetailDao.selectByVoucherNumber(v_n);
			for (PaymentDetailBean paymentDetail : paymentDetails) {

				// 删除支付凭证
				File destfile = new File(fileFolder + File.separator + paymentDetail.getAccount_pk() + File.separator
						+ paymentDetail.getVoucher_file_name());
				if (destfile.exists()) {
					destfile.delete();
				}

				paymentDetailService.deleteDetail(paymentDetail.getPk());
			}
		}

		return SUCCESS;
	}

	@Autowired
	private CardDAO cardDao;

	@Autowired
	private NumberService numberService;

	@Autowired
	private AirTicketPayableDAO payableDao;

	@Override
	public String backRecive(AirTicketPaidDetailBean detail, String json) {
		UserSessionBean user = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		String related_pk = DBCommonUtil.genPk();
		JSONArray array = JSONArray.fromObject(json);
		BigDecimal allot_money = detail.getAllot_money();
		String time = detail.getTime();

		String voucher_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
				ResourcesConstants.PAY_TYPE_PIAOWU, DateUtil.getDateStr(DateUtil.YYYYMMDD));
		// 生成银行流水数据
		String msg = SUCCESS;
		String account = detail.getAccount();

		CardBean card = cardDao.getCardByAccount(account);
		BigDecimal balance = card.getBalance().add(allot_money);

		String voucher_file_name = detail.getVoucher_file_name();

		PaymentDetailBean payment = new PaymentDetailBean();
		payment.setVoucher_number(voucher_number);
		payment.setAccount(account);
		payment.setTime(time);
		payment.setMoney(allot_money);
		payment.setBalance(balance);
		payment.setType("收入");
		payment.setComment("机票返款收入,凭证号：" + voucher_number);
		payment.setVoucher_file_name(voucher_file_name);

		msg = paymentDetailService.insert(payment);

		if (!msg.equals(SUCCESS)) {
			return msg;
		}

		// 更新账户余额
		card.setBalance(balance);
		cardDao.update(card);

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));

			String payable_pk = obj.getString("base_pk");
			String received = obj.getString("received");
			String supplier_employee_pk = obj.getString("supplier_employee_pk");

			AirTicketPayableBean airTicketPayable = payableDao.selectByPrimaryKey(payable_pk);

			AirTicketPaidDetailBean currentDetail = new AirTicketPaidDetailBean();

			currentDetail.setAllot_money(allot_money);
			currentDetail.setSupplier_employee_pk(supplier_employee_pk);
			currentDetail.setBase_pk(payable_pk);
			currentDetail.setType(ResourcesConstants.PAID_TYPE_BACK);
			currentDetail.setStatus(ResourcesConstants.PAID_STATUS_PAID);
			currentDetail.setRelated_pk(related_pk);
			BigDecimal money = BigDecimal.ZERO;
			if (!SimpletinyString.isEmpty(received)) {
				money = new BigDecimal(Integer.valueOf(received) * -1);
			}
			currentDetail.setTime(time);
			currentDetail.setMoney(money);
			currentDetail.setConfirm_time(DateUtil.getTimeMillis());
			currentDetail.setApprove_user(user.getUser_number());
			currentDetail.setVoucher_number(voucher_number);
			dao.insert(currentDetail);

			airTicketPayable.setPaid(airTicketPayable.getPaid() == null ? BigDecimal.ZERO.add(money)
					: airTicketPayable.getPaid().add(money));
			airTicketPayable.setBudget_balance(airTicketPayable.getBudget_balance().subtract(money));
			if (airTicketPayable.getFinal_flg().equals("Y")) {
				airTicketPayable.setFinal_balance(airTicketPayable.getFinal_balance().subtract(money));
			}
			payableDao.update(airTicketPayable);
		}

		return SUCCESS;
	}

	@Override
	public String businessStrike(String json) {
		String related_pk = DBCommonUtil.genPk();
		UserSessionBean user = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		String voucher_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
				ResourcesConstants.PAY_TYPE_PIAOWU, DateUtil.getDateStr(DateUtil.YYYYMMDD));

		JSONObject obj = JSONObject.fromObject(json);
		JSONArray outArr = obj.getJSONArray("out");
		BigDecimal allot_money = new BigDecimal(
				SimpletinyString.isEmpty(obj.getString("allot_money")) ? "0" : obj.getString("allot_money"));

		for (int i = 0; i < outArr.size(); i++) {

			JSONObject out = outArr.getJSONObject(i);

			String payable_pk = out.getString("payable_pk");
			BigDecimal money = new BigDecimal(
					SimpletinyString.isEmpty(out.getString("money")) ? "0" : out.getString("money"));

			AirTicketPayableBean airTicketPayable = payableDao.selectByPrimaryKey(payable_pk);

			AirTicketPaidDetailBean currentDetail = new AirTicketPaidDetailBean();

			currentDetail.setAllot_money(allot_money);
			currentDetail.setSupplier_employee_pk(airTicketPayable.getSupplier_employee_pk());
			currentDetail.setBase_pk(payable_pk);
			currentDetail.setType(ResourcesConstants.PAID_TYPE_STRIKE_OUT);
			currentDetail.setStatus(ResourcesConstants.PAID_STATUS_PAID);
			currentDetail.setRelated_pk(related_pk);

			currentDetail.setTime(DateUtil.getMinStr());
			currentDetail.setMoney(money);
			currentDetail.setConfirm_time(DateUtil.getTimeMillis());
			currentDetail.setApprove_user(user.getUser_number());
			currentDetail.setVoucher_number(voucher_number);
			dao.insert(currentDetail);

			airTicketPayable.setPaid(airTicketPayable.getPaid() == null ? BigDecimal.ZERO.subtract(money)
					: airTicketPayable.getPaid().subtract(money));

			airTicketPayable.setBudget_balance(airTicketPayable.getBudget_balance().add(money));

			if (airTicketPayable.getFinal_flg().equals("Y")) {
				airTicketPayable.setFinal_balance(airTicketPayable.getFinal_balance().add(money));
			}

			payableDao.update(airTicketPayable);

		}

		JSONArray inArr = obj.getJSONArray("in");

		for (int i = 0; i < inArr.size(); i++) {

			JSONObject in = inArr.getJSONObject(i);

			String payable_pk = in.getString("payable_pk");
			BigDecimal money = new BigDecimal(
					SimpletinyString.isEmpty(in.getString("money")) ? "0" : in.getString("money"));

			AirTicketPayableBean airTicketPayable = payableDao.selectByPrimaryKey(payable_pk);

			AirTicketPaidDetailBean currentDetail = new AirTicketPaidDetailBean();

			currentDetail.setAllot_money(allot_money);
			currentDetail.setSupplier_employee_pk(airTicketPayable.getSupplier_employee_pk());
			currentDetail.setBase_pk(payable_pk);
			currentDetail.setType(ResourcesConstants.PAID_TYPE_STRIKE_IN);
			currentDetail.setStatus(ResourcesConstants.PAID_STATUS_PAID);
			currentDetail.setRelated_pk(related_pk);

			currentDetail.setTime(DateUtil.getMinStr());
			currentDetail.setMoney(money);
			currentDetail.setConfirm_time(DateUtil.getTimeMillis());
			currentDetail.setApprove_user(user.getUser_number());
			currentDetail.setVoucher_number(voucher_number);
			dao.insert(currentDetail);

			airTicketPayable
					.setPaid(airTicketPayable.getPaid() == null ? money : airTicketPayable.getPaid().add(money));

			airTicketPayable.setBudget_balance(airTicketPayable.getBudget_balance().subtract(money));

			if (airTicketPayable.getFinal_flg().equals("Y")) {
				airTicketPayable.setFinal_balance(airTicketPayable.getFinal_balance().subtract(money));
			}
			payableDao.update(airTicketPayable);

		}

		return SUCCESS;
	}

	@Autowired
	private DepositTicketPaidDAO depositTicketPaidDao;

	@Autowired
	private SupplierDepositDAO depositDao;

	@Override
	public String depositStrike(String json) {
		String related_pk = DBCommonUtil.genPk();
		UserSessionBean user = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);

		String voucher_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
				ResourcesConstants.PAY_TYPE_PIAOWU, DateUtil.getDateStr(DateUtil.YYYYMMDD));

		JSONObject obj = JSONObject.fromObject(json);
		BigDecimal allot_money = new BigDecimal(
				SimpletinyString.isEmpty(obj.getString("allot_money")) ? "0" : obj.getString("allot_money"));

		JSONArray outArr = obj.getJSONArray("out");
		for (int i = 0; i < outArr.size(); i++) {
			JSONObject out = outArr.getJSONObject(i);
			String deposit_pk = out.getString("deposit_pk");
			BigDecimal money = new BigDecimal(
					SimpletinyString.isEmpty(out.getString("money")) ? "0" : out.getString("money"));

			SupplierDepositBean deposit = depositDao.selectByPrimaryKey(deposit_pk);
			deposit.setReceived(deposit.getReceived() == null ? money : deposit.getReceived().add(money));
			deposit.setBalance(deposit.getBalance().subtract(money));

			if (deposit.getBalance().compareTo(BigDecimal.ZERO) == 0) {
				deposit.setStatus("Y");
			}

			String return_way = deposit.getReturn_way();
			if (SimpletinyString.isEmpty(return_way)) {
				deposit.setReturn_way("C");
			} else {
				if (return_way.indexOf("C") < 0) {
					deposit.setReturn_way("C," + return_way);
				}
			}
			// 更新航司押金
			depositDao.update(deposit);

			// 保存押金和票务支付详情的对应关系
			DepositTicketPaidBean dtp = new DepositTicketPaidBean();
			dtp.setDeposit_pk(deposit_pk);
			dtp.setRelated_pk(related_pk);
			depositTicketPaidDao.insert(dtp);
		}

		JSONArray inArr = obj.getJSONArray("in");

		for (int i = 0; i < inArr.size(); i++) {

			JSONObject in = inArr.getJSONObject(i);

			String payable_pk = in.getString("payable_pk");
			BigDecimal money = new BigDecimal(
					SimpletinyString.isEmpty(in.getString("money")) ? "0" : in.getString("money"));

			AirTicketPayableBean airTicketPayable = payableDao.selectByPrimaryKey(payable_pk);

			AirTicketPaidDetailBean currentDetail = new AirTicketPaidDetailBean();

			currentDetail.setAllot_money(allot_money);
			currentDetail.setSupplier_employee_pk(airTicketPayable.getSupplier_employee_pk());
			currentDetail.setBase_pk(payable_pk);
			currentDetail.setType(ResourcesConstants.PAID_TYPE_DEPOSIT_IN);
			currentDetail.setStatus(ResourcesConstants.PAID_STATUS_PAID);
			currentDetail.setRelated_pk(related_pk);

			currentDetail.setTime(DateUtil.getMinStr());
			currentDetail.setMoney(money);
			currentDetail.setConfirm_time(DateUtil.getTimeMillis());
			currentDetail.setApprove_user(user.getUser_number());
			currentDetail.setVoucher_number(voucher_number);
			dao.insert(currentDetail);

			airTicketPayable
					.setPaid(airTicketPayable.getPaid() == null ? money : airTicketPayable.getPaid().add(money));

			airTicketPayable.setBudget_balance(airTicketPayable.getBudget_balance().subtract(money));

			if (airTicketPayable.getFinal_flg().equals("Y")) {
				airTicketPayable.setFinal_balance(airTicketPayable.getFinal_balance().subtract(money));
			}
			payableDao.update(airTicketPayable);

		}

		return SUCCESS;
	}

}