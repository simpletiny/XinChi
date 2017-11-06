package com.xinchi.backend.payable.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.payable.dao.PayableDAO;
import com.xinchi.backend.payable.service.PaidService;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.tools.Page;

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

	@Override
	public String rollBackPayApply(String related_pk) {
		SupplierPaidDetailBean options = new SupplierPaidDetailBean();
		options.setRelated_pk(related_pk);
		List<SupplierPaidDetailBean> paids = dao.selectByParam(options);

		for (SupplierPaidDetailBean paid : paids) {
			String supplier_employee_pk = paid.getSupplier_employee_pk();
			String team_number = paid.getTeam_number();
			PayableBean options2 = new PayableBean();
			options2.setTeam_number(team_number);
			options2.setSupplier_employee_pk(supplier_employee_pk);
			List<PayableBean> payables = payableDao.selectByParam(options2);
			if (null != payables && payables.size() > 0) {
				PayableBean payable = payables.get(0);

				payable.setPaid(payable.getPaid().subtract(paid.getMoney()));
				payable.setBudget_balance(payable.getBudget_balance().add(paid.getMoney()));
				if (payable.getFinal_flg().equals("Y")) {
					payable.setFinal_balance(payable.getFinal_balance().add(paid.getMoney()));
				}
				payableDao.update(payable);
			}
			dao.deleteByPk(paid.getPk());
		}
		return SUCCESS;
	}
}
