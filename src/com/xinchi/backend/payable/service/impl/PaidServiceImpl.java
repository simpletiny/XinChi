package com.xinchi.backend.payable.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.payable.dao.PaidDAO;
import com.xinchi.backend.payable.service.PaidService;
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

}
