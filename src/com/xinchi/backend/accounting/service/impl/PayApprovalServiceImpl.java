package com.xinchi.backend.accounting.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.PayApprovalDAO;
import com.xinchi.backend.accounting.service.PayApprovalService;
import com.xinchi.bean.PayApprovalBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class PayApprovalServiceImpl implements PayApprovalService {

	@Autowired
	private PayApprovalDAO dao;

	@Override
	public void insert(PayApprovalBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(PayApprovalBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public PayApprovalBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<PayApprovalBean> selectByParam(PayApprovalBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<PayApprovalBean> selectByPage(Page<PayApprovalBean> page) {
		return dao.selectByPage(page);
	}

}