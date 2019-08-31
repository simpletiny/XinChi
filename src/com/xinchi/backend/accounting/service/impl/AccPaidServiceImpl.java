package com.xinchi.backend.accounting.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.AccPaidDAO;
import com.xinchi.backend.accounting.dao.PayApprovalDAO;
import com.xinchi.backend.accounting.dao.ReimbursementDAO;
import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.PaidDetailSummary;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.bean.ReimbursementBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.tools.Page;

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

}
