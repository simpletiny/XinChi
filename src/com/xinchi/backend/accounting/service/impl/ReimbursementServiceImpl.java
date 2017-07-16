package com.xinchi.backend.accounting.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.ReimbursementDAO;
import com.xinchi.backend.accounting.service.ReimbursementService;
import com.xinchi.bean.ReimbursementBean;

@Service
@Transactional
public class ReimbursementServiceImpl implements ReimbursementService {

	@Autowired
	private ReimbursementDAO dao;

	@Override
	public String save(ReimbursementBean reimbursement) {
		dao.insert(reimbursement);
		return "success";
	}

	@Override
	public ReimbursementBean selectByPk(String reimbursement_pk) {
		return dao.selectByPk(reimbursement_pk);
	}

	@Override
	public void update(ReimbursementBean reim) {
		 dao.update(reim);
	}

}
