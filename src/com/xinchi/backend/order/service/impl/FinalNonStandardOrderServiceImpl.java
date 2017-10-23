package com.xinchi.backend.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.FinalNonStandardOrderDAO;
import com.xinchi.backend.order.service.FinalNonStandardOrderService;
import com.xinchi.bean.FinalNonStandardOrderBean;

@Service
@Transactional
public class FinalNonStandardOrderServiceImpl implements FinalNonStandardOrderService {

	@Autowired
	private FinalNonStandardOrderDAO dao;

	@Override
	public void insert(FinalNonStandardOrderBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(FinalNonStandardOrderBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public FinalNonStandardOrderBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<FinalNonStandardOrderBean> selectByParam(FinalNonStandardOrderBean bean) {
		return dao.selectByParam(bean);
	}

}