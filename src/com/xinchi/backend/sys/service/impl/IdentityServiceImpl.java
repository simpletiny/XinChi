package com.xinchi.backend.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sys.dao.IdentityDAO;
import com.xinchi.backend.sys.service.IdentityService;
import com.xinchi.bean.IdentityBean;

@Service
@Transactional
public class IdentityServiceImpl implements IdentityService {

	@Autowired
	private IdentityDAO dao;

	@Override
	public void insert(IdentityBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(IdentityBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public IdentityBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<IdentityBean> selectByParam(IdentityBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public IdentityBean selectById(String id) {
		return dao.selectById(id);
	}

}