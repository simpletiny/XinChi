package com.xinchi.backend.supplier.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.supplier.dao.DepositPaymentDAO;
import com.xinchi.backend.supplier.dao.SupplierDAO;
import com.xinchi.backend.supplier.dao.SupplierDepositDAO;
import com.xinchi.backend.supplier.service.SupplierDepositService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.CardBean;
import com.xinchi.bean.DepositPaymentBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.SupplierBean;
import com.xinchi.bean.SupplierDepositBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.tools.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class SupplierDepositServiceImpl implements SupplierDepositService {

	@Autowired
	private SupplierDepositDAO dao;

	@Override
	public void insert(SupplierDepositBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(SupplierDepositBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public SupplierDepositBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<SupplierDepositBean> selectByParam(SupplierDepositBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<SupplierDepositBean> selectByPage(Page page) {

		return dao.selectByPage(page);
	}

	@Autowired
	private NumberService numberService;

	@Autowired
	private CardDAO cardDao;

	@Autowired
	private SupplierDAO supplierDao;

	@Autowired
	private PaymentDetailService paymentDetailService;

	@Override
	public String createDeposit(SupplierDepositBean deposit) {
		SupplierBean supplier = supplierDao.selectByPrimaryKey(deposit.getSupplier_pk());

		// 生成银行流水账
		String voucher_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
				ResourcesConstants.PAY_TYPE_DEPOSIT_AIR, DateUtil.getDateStr(DateUtil.YYYYMMDD));

		// 生成银行流水数据
		String msg = SUCCESS;
		String account = deposit.getAccount();
		CardBean card = cardDao.getCardByAccount(account);

		BigDecimal balance = card.getBalance().subtract(deposit.getMoney());

		String voucher_file_name = deposit.getVoucher_file_name();

		PaymentDetailBean payment = new PaymentDetailBean();
		payment.setVoucher_number(voucher_number);
		payment.setAccount(account);
		payment.setTime(deposit.getTime());
		payment.setMoney(deposit.getMoney());
		payment.setBalance(balance);
		payment.setType("支出");
		payment.setComment(supplier.getSupplier_short_name() + "机票押金,凭证号：" + voucher_number);
		payment.setVoucher_file_name(voucher_file_name);

		msg = paymentDetailService.insert(payment);
		if (!msg.equals(SUCCESS)) {
			return msg;
		}

		// 保存航司押金记录
		deposit.setVoucher_number(voucher_number);
		deposit.setReceived(BigDecimal.ZERO);
		deposit.setBalance(deposit.getMoney());
		dao.insert(deposit);

		return SUCCESS;
	}

	@Override
	public String deleteSupplierDeposit(String deposit_pk) {
		String result = SUCCESS;
		SupplierDepositBean deposit = dao.selectByPrimaryKey(deposit_pk);
		// 删除银行流水
		List<PaymentDetailBean> details = paymentDetailService.selectByVoucherNumber(deposit.getVoucher_number());
		if (null != details && details.size() > 0) {
			PaymentDetailBean detail = details.get(0);
			result = paymentDetailService.deleteDetail(detail.getPk());
		}

		if (result.equals(SUCCESS)) {
			// 删除押金账
			dao.delete(deposit_pk);
		}

		return result;
	}

	@Autowired
	private DepositPaymentDAO depositPaymentDao;

	@Override
	public String receiveSupplierDeposit(SupplierDepositBean deposit, String json) {

		// 生成银行流水账
		String voucher_number = numberService.generatePayOrderNumber(ResourcesConstants.COUNT_TYPE_PAY_ORDER,
				ResourcesConstants.PAY_TYPE_DEPOSIT_AIR, DateUtil.getDateStr(DateUtil.YYYYMMDD));

		// 生成银行流水数据
		String msg = SUCCESS;
		String account = deposit.getAccount();
		CardBean card = cardDao.getCardByAccount(account);

		BigDecimal balance = card.getBalance().add(deposit.getMoney());

		String voucher_file_name = deposit.getVoucher_file_name();

		PaymentDetailBean payment = new PaymentDetailBean();
		payment.setVoucher_number(voucher_number);
		payment.setAccount(account);
		payment.setTime(deposit.getTime());
		payment.setMoney(deposit.getMoney());
		payment.setBalance(balance);
		payment.setType("收入");
		payment.setComment("机票押金退反,凭证号：" + voucher_number);
		payment.setVoucher_file_name(voucher_file_name);

		msg = paymentDetailService.insert(payment);
		if (!msg.equals(SUCCESS)) {
			return msg;
		}

		JSONArray arr = JSONArray.fromObject(json);
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			String deposit_pk = obj.getString("deposit_pk");
			BigDecimal received = new BigDecimal(
					SimpletinyString.isEmpty(obj.getString("money")) ? "0" : obj.getString("money"));

			SupplierDepositBean currentDeposit = dao.selectByPrimaryKey(deposit_pk);

			currentDeposit.setReceived(currentDeposit.getReceived() == null ? BigDecimal.ZERO
					: currentDeposit.getReceived().add(received));
			currentDeposit.setBalance(currentDeposit.getBalance().subtract(received));

			String return_way = currentDeposit.getReturn_way();
			if (SimpletinyString.isEmpty(return_way)) {
				currentDeposit.setReturn_way("T");
			} else {
				if (currentDeposit.getReturn_way().indexOf("T") < 0) {
					currentDeposit.setReturn_way(return_way + ",T");
				}
			}

			if (currentDeposit.getBalance().compareTo(BigDecimal.ZERO) == 0) {
				currentDeposit.setStatus("Y");
			}
			// 更新押金账
			dao.update(currentDeposit);
			// 保存押金账和银行流水押金退回收入之间的对应关系
			DepositPaymentBean dp = new DepositPaymentBean();
			dp.setDeposit_pk(deposit_pk);
			dp.setPayment_voucher_number(voucher_number);
			depositPaymentDao.insert(dp);
		}

		return SUCCESS;
	}

}