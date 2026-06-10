package com.xinchi.backend.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sys.dao.DishonestLogDAO;
import com.xinchi.backend.sys.service.DishonestLogService;
import com.xinchi.bean.DishonestLogBean;

@Service
@Transactional
public class DishonestLogServiceImpl implements DishonestLogService {

	@Autowired
	private DishonestLogDAO dao;

	@Override
	public void insert(DishonestLogBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(DishonestLogBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public DishonestLogBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<DishonestLogBean> selectByParam(DishonestLogBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<DishonestLogBean> selectByPersonId(String id) {
		return dao.selectByPersonId(id);
	}

}