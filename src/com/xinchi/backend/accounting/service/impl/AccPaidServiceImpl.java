package com.xinchi.backend.accounting.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.AccPaidDAO;
import com.xinchi.backend.accounting.service.AccPaidService;
import com.xinchi.bean.WaitingForPaidBean;
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

}
