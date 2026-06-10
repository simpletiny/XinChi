package com.xinchi.backend.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sys.dao.DishonestPersonDAO;
import com.xinchi.backend.sys.service.DishonestPersonService;
import com.xinchi.bean.DishonestPersonBean;

@Service
@Transactional
public class DishonestPersonServiceImpl implements DishonestPersonService {

	@Autowired
	private DishonestPersonDAO dao;

	@Override
	public void insert(DishonestPersonBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(DishonestPersonBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public DishonestPersonBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<DishonestPersonBean> selectByParam(DishonestPersonBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public DishonestPersonBean selectByPersonId(String id) {
		return dao.selectByPersonId(id);
	}

}