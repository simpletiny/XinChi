package com.xinchi.backend.accounting.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.PayApprovalDAO;
import com.xinchi.backend.accounting.dao.ReimbursementDAO;
import com.xinchi.backend.accounting.service.ReimbursementService;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.bean.ReimbursementBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ReimbursementServiceImpl implements ReimbursementService {

	@Autowired
	private ReimbursementDAO dao;

	@Autowired
	private PayApprovalDAO payApprovalDao;

	@Override
	public String save(ReimbursementBean reimbursement) {
		dao.insert(reimbursement);

		// 生成支付审批
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		PayApprovalBean pa = new PayApprovalBean();
		pa.setReceiver(sessionBean.getUser_name());
		pa.setMoney(reimbursement.getMoney());
		pa.setItem(reimbursement.getItem());
		pa.setStatus(ResourcesConstants.PAID_STATUS_ING);
		pa.setComment("归属月份：" + reimbursement.getMonth() + "\n" + reimbursement.getComment());
		pa.setApply_user(reimbursement.getApply_user());
		pa.setBack_pk(reimbursement.getPk());
		pa.setApply_time(DateUtil.getTimeMillis());
		payApprovalDao.insert(pa);

		return SUCCESS;
	}

	@Override
	public ReimbursementBean selectByPk(String reimbursement_pk) {
		return dao.selectByPk(reimbursement_pk);
	}

	@Override
	public void update(ReimbursementBean reim) {
		dao.update(reim);
	}

	@Override
	public List<ReimbursementBean> selectByPage(Page page) {
		return dao.selectByPage(page);
	}

	@Override
	public String deleteReibursement(List<String> reimbursement_pks) {
		for (String pk : reimbursement_pks) {
			ReimbursementBean rei = dao.selectByPk(pk);
			if (null == rei)
				continue;
			rei.setDelete_flg("Y");
			dao.update(rei);

		}
		return SUCCESS;
	}

}
