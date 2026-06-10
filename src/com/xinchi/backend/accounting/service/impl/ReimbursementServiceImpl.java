package com.xinchi.backend.accounting.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.PayApprovalDAO;
import com.xinchi.backend.accounting.dao.ReimbursementDAO;
import com.xinchi.backend.accounting.service.ReimbursementService;
import com.xinchi.backend.client.dao.EmployeeDAO;
import com.xinchi.bean.ClientEmployeeBean;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.bean.ReimbursementBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.SimpletinyUser;
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

	@Autowired
	private EmployeeDAO employeeDAO;

	@Override
	public String save(ReimbursementBean reimbursement) {

		// 生成支付审批
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		PayApprovalBean pa = new PayApprovalBean();
		pa.setReceiver(sessionBean.getUser_name());
		pa.setMoney(reimbursement.getMoney());
		pa.setItem(reimbursement.getItem());
		pa.setStatus(ResourcesConstants.PAID_STATUS_ING);
		String comment = "归属月份：" + reimbursement.getMonth() + "\n" + reimbursement.getComment();
		// 如果是销售费用，则要在备注中添加客户信息
		if (reimbursement.getItem().equals(ResourcesConstants.PAY_TYPE_XIAOSHOU)) {
			ClientEmployeeBean employee = employeeDAO.selectByPrimaryKey(reimbursement.getClient_employee_pk());
			String sale_cost_comment = "客户：" + employee.getName() + ";" + "财务主体：" + employee.getFinancial_body_name() + ";\n";
			reimbursement.setComment(sale_cost_comment + reimbursement.getComment());
			comment = sale_cost_comment + comment;
		}
		dao.insert(reimbursement);
		pa.setComment(comment.length() > 200 ? comment.substring(0, 200) : comment);
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
	public List<ReimbursementBean> selectByParam(ReimbursementBean option) {
		return dao.selectByParam(option);
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

	@Override
	public String reApply(ReimbursementBean reimbursement) {
		UserSessionBean user = SimpletinyUser.user();
		reimbursement.setStatus("I");
		reimbursement.setApproval_user("");
		reimbursement.setApproval_time("");
		reimbursement.setApply_user(user.getUser_number());

		// 生成支付审批
		PayApprovalBean pa = new PayApprovalBean();
		pa.setReceiver(user.getUser_name());
		pa.setMoney(reimbursement.getMoney());
		pa.setItem(reimbursement.getItem());
		pa.setStatus(ResourcesConstants.PAID_STATUS_ING);
		String old_comment = SimpletinyString.replaceByRegex(reimbursement.getComment(), "客户：\\s*[^;]+;\\s*财务主体：\\s*[^;]+;\\s*");
		String comment = "归属月份：" + reimbursement.getMonth() + old_comment + "；\n";
		// 如果是销售费用，则要在备注中添加客户信息
		if (reimbursement.getItem().equals(ResourcesConstants.PAY_TYPE_XIAOSHOU)) {
			ClientEmployeeBean employee = employeeDAO.selectByPrimaryKey(reimbursement.getClient_employee_pk());
			String sale_cost_comment = "客户：" + employee.getName() + ";" + "财务主体：" + employee.getFinancial_body_name() + ";\n";
			reimbursement.setComment(sale_cost_comment + old_comment);
			comment = sale_cost_comment + comment;
		} else {
			reimbursement.setClient_employee_pk(null);
			reimbursement.setComment(old_comment);
		}
		payApprovalDao.deleteByBackPk(reimbursement.getPk());
		dao.update(reimbursement);
		pa.setComment(comment);
		pa.setApply_user(user.getUser_number());
		pa.setBack_pk(reimbursement.getPk());
		pa.setApply_time(DateUtil.getTimeMillis());
		payApprovalDao.insert(pa);
		return SUCCESS;
	}

	@Override
	public Map<String, BigDecimal> searchSummaries(ReimbursementBean reimbursement) {
		List<ReimbursementBean> results = dao.selectSummaries(reimbursement);
		Map<String, BigDecimal> summaries = new HashMap<>();
		for (ReimbursementBean rei : results) {
			summaries.put(rei.getItem(), rei.getMoney());
			summaries.put('P' + rei.getItem(), rei.getPaid_money());
			summaries.put('Y' + rei.getItem(), rei.getWaiting_money());
			summaries.put('I' + rei.getItem(), rei.getSuspense_money());
		}
		return summaries;
	}

	@Override
	public List<ReimbursementBean> selectSumByParam(ReimbursementBean option) {
		return dao.selectSumByParam(option);
	}

}
