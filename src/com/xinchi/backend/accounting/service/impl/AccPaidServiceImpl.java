package com.xinchi.backend.accounting.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.AccPaidDAO;
import com.xinchi.backend.accounting.dao.PayApprovalDAO;
import com.xinchi.backend.accounting.dao.ReimbursementDAO;
import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.accounting.service.ReimbursementService;
import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.payable.dao.AirTicketPaidDetailDAO;
import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.payable.service.PaidService;
import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.PaidDetailSummary;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.ReimbursementBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class AccPaidServiceImpl implements AccPaidService {

	@Autowired
	private AccPaidDAO dao;

	@Override
	public String insert(WaitingForPaidBean paid) {
		return dao.insert(paid);
	}

	@Override
	public List<WaitingForPaidBean> selectByPage(Page page) {

		return dao.selectByPage(page);
	}

	@Override
	public WaitingForPaidBean selectByPk(String wfp_pk) {

		return dao.selectByPk(wfp_pk);
	}

	@Override
	public WaitingForPaidBean selectByPayNumber(String voucher_number) {

		return dao.selectByPayNumber(voucher_number);
	}

	@Override
	public void update(WaitingForPaidBean wfp) {
		dao.update(wfp);
	}

	@Override
	public PaidDetailSummary selectPaidDetailSummaryByPayNumber(String voucher_number) {
		return dao.selectPaidSummaryByPayNumber(voucher_number);
	}

	@Autowired
	private PaidDAO paidDao;

	@Autowired
	private PayApprovalDAO payApprovalDao;

	@Autowired
	private ReceivedDAO receivedDao;

	@Autowired
	private ReimbursementDAO reimbursementDao;

	@Override
	public String rollBackWfp(String wfp_pk) {
		WaitingForPaidBean wfp = dao.selectByPk(wfp_pk);

		String related_pk = wfp.getRelated_pk();
		String back_pk = "";
		// 更新申请状态
		if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			List<SupplierPaidDetailBean> supplierDetails = paidDao.selectSupplierPaidDetailByRelatedPk(related_pk);
			for (SupplierPaidDetailBean detail : supplierDetails) {
				detail.setStatus(ResourcesConstants.PAID_STATUS_ING);
				detail.setApprove_user("");
				detail.setConfirm_time("");
				paidDao.update(detail);
			}
			back_pk = related_pk;

		} else if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_FLY)) {
			ClientReceivedDetailBean detail = receivedDao.selectByPk(related_pk);
			detail.setStatus(ResourcesConstants.PAID_STATUS_ING);
			detail.setConfirm_time("");
			receivedDao.update(detail);
			back_pk = detail.getRelated_pk();

		} else if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_MORE_BACK)) {
			ClientReceivedDetailBean detail = receivedDao.selectByPk(related_pk);
			detail.setConfirm_time("");
			detail.setStatus(ResourcesConstants.PAID_STATUS_ING);
			receivedDao.update(detail);

			back_pk = detail.getRelated_pk();

		} else {
			ReimbursementBean reim = reimbursementDao.selectByPk(related_pk);
			reim.setApproval_user("");
			reim.setApproval_time("");
			reim.setStatus(ResourcesConstants.PAID_STATUS_ING);
			reimbursementDao.update(reim);

			back_pk = reim.getPk();

		}
		PayApprovalBean pa = payApprovalDao.selectByBackPk(back_pk);
		pa.setApproval_user("");
		pa.setApproval_time("");
		pa.setStatus(ResourcesConstants.PAID_STATUS_ING);
		payApprovalDao.update(pa);

		dao.deleteByPk(wfp_pk);
		return SUCCESS;
	}

	@Autowired
	private PaymentDetailService pds;

	@Autowired
	private PaidService paidService;

	@Autowired
	private ReceivedService receivedService;

	@Autowired
	private ReimbursementService reimService;

	public String rollBackPay(String voucher_number) {
		// 更新待支付状态
		WaitingForPaidBean wfp = dao.selectByPayNumber(voucher_number);

		if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_PIAOWU)) {
			return "不允许打回票务支出！";
		}

		// 删除银行流水支出单,删除上传的凭证
		List<PaymentDetailBean> details = pds.selectByVoucherNumber(voucher_number);
		for (PaymentDetailBean detail : details) {
			String voucher_file = detail.getVoucher_file_name();

			String fileFolder = PropertiesUtil.getProperty("voucherFileFolder");
			File destfile = new File(
					fileFolder + File.separator + detail.getAccount_pk() + File.separator + voucher_file);
			destfile.delete();

			pds.deleteDetail(detail.getPk());

		}

		wfp.setStatus(ResourcesConstants.PAY_STATUS_ING);
		wfp.setPay_user("");
		dao.update(wfp);

		String related_pk = wfp.getRelated_pk();

		// 更新申请状态
		if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			List<SupplierPaidDetailBean> supplierDetails = paidService.selectByRelatedPk(related_pk);
			for (SupplierPaidDetailBean detail : supplierDetails) {
				detail.setTime("");
				detail.setStatus(ResourcesConstants.PAID_STATUS_YES);
				paidService.update(detail);
			}
		}
		// else if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_PIAOWU)) {
		// List<AirTicketPaidDetailBean> paids =
		// airTicketPaidDetailService.selectByRelatedPk(related_pk);
		// for (AirTicketPaidDetailBean paid : paids) {
		// paid.setStatus(ResourcesConstants.PAID_STATUS_YES);
		// paid.setTime(DateUtil.getDateStr(""));
		// airTicketPaidDetailService.update(paid);
		// }
		// }
		else if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_FLY)) {
			ClientReceivedDetailBean detail = receivedService.selectByPk(related_pk);
			detail.setConfirm_time(DateUtil.getTimeMillis());
			detail.setStatus(ResourcesConstants.PAID_STATUS_YES);
			receivedService.update(detail);
		} else if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_MORE_BACK)) {
			ClientReceivedDetailBean detail = receivedService.selectByPk(related_pk);
			detail.setConfirm_time(DateUtil.getTimeMillis());
			detail.setStatus(ResourcesConstants.PAID_STATUS_YES);
			receivedService.update(detail);
		} else {
			ReimbursementBean reim = reimService.selectByPk(related_pk);
			reim.setPay_user("");
			reim.setPay_time("");
			reim.setStatus(ResourcesConstants.PAID_STATUS_YES);
			reimService.update(reim);
		}
		return SUCCESS;
	}

	@Override
	public BigDecimal selectSumWFP() {
		return dao.selectSumWFP();
	}

	@Override
	public List<WaitingForPaidBean> selectWfpByRelatedPk(String related_pk) {

		return dao.selectByRelatedPk(related_pk);
	}

	@Autowired
	private AirTicketPaidDetailDAO airTicketPaidDetailDao;

	@Autowired
	private CardDAO cardDao;

	@Override
	public String pay(String json, String voucher_number) {
		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = JSONObject.fromObject(array.get(i));
			String account = obj.getString("account");
			String time = obj.getString("time");
			String receiver = obj.getString("receiver");
			BigDecimal balance = new BigDecimal(cardDao.getAccountBalance(account));
			BigDecimal money = new BigDecimal(obj.getString("money"));
			String voucher_file_name = obj.getString("voucherFile");

			PaymentDetailBean detail = new PaymentDetailBean();
			detail.setVoucher_number(voucher_number);
			detail.setAccount(account);
			detail.setTime(time);
			detail.setReceiver(receiver);
			detail.setMoney(money);
			detail.setBalance(balance.subtract(money));
			detail.setType("支出");
			detail.setComment(receiver + ",凭证号：" + voucher_number);
			detail.setVoucher_file_name(voucher_file_name);
			pds.insert(detail);
		}
		// 更新待支付状态
		WaitingForPaidBean wfp = dao.selectByPayNumber(voucher_number);
		wfp.setStatus(ResourcesConstants.PAY_STATUS_YES);
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		wfp.setPay_user(sessionBean.getUser_number());
		dao.update(wfp);

		String related_pk = wfp.getRelated_pk();
		// 更新申请状态
		if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_DIJIE)) {
			List<SupplierPaidDetailBean> details = paidService.selectByRelatedPk(related_pk);
			for (SupplierPaidDetailBean detail : details) {
				detail.setTime(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
				detail.setStatus(ResourcesConstants.PAID_STATUS_PAID);
				detail.setPaid_user(sessionBean.getUser_number());
				paidService.update(detail);
			}
		} else if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_PIAOWU)) {
			List<AirTicketPaidDetailBean> paids = airTicketPaidDetailDao.selectByRelatedPk(related_pk);
			for (AirTicketPaidDetailBean paid : paids) {
				paid.setStatus(ResourcesConstants.PAID_STATUS_PAID);
				paid.setTime(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
				airTicketPaidDetailDao.update(paid);
			}
		} else if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_FLY)) {
			ClientReceivedDetailBean detail = receivedService.selectByPk(related_pk);
			detail.setConfirm_time(DateUtil.getTimeMillis());
			detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ENTER);
			receivedService.update(detail);
		} else if (wfp.getItem().equals(ResourcesConstants.PAY_TYPE_MORE_BACK)) {
			ClientReceivedDetailBean detail = receivedService.selectByPk(related_pk);
			detail.setConfirm_time(DateUtil.getTimeMillis());
			detail.setStatus(ResourcesConstants.RECEIVED_STATUS_ENTER);
			receivedService.update(detail);
		} else {
			ReimbursementBean reim = reimService.selectByPk(related_pk);
			reim.setPay_user(sessionBean.getUser_number());
			reim.setPay_time(DateUtil.getDateStr("yyyy-MM-dd HH:mm"));
			reim.setStatus(ResourcesConstants.PAID_STATUS_PAID);
			reimService.update(reim);
		}
		return SUCCESS;
	}

}
