package com.xinchi.backend.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.FinalStandardOrderDAO;
import com.xinchi.backend.order.service.FinalStandardOrderService;
import com.xinchi.bean.FinalStandardOrderBean;

@Service
@Transactional
public class FinalStandardOrderServiceImpl implements FinalStandardOrderService {

	@Autowired
	private FinalStandardOrderDAO dao;

	@Override
	public void insert(FinalStandardOrderBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(FinalStandardOrderBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public FinalStandardOrderBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<FinalStandardOrderBean> selectByParam(FinalStandardOrderBean bean) {
		return dao.selectByParam(bean);
	}

}