package com.xinchi.backend.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.user.dao.UserLogDAO;
import com.xinchi.backend.user.service.UserLogService;
import com.xinchi.bean.UserLogBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class UserLogServiceImpl implements UserLogService {

	@Autowired
	private UserLogDAO dao;

	@Override
	public void insert(UserLogBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(UserLogBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public UserLogBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<UserLogBean> getAllByParam(UserLogBean bean) {
		return dao.getAllByParam(bean);
	}

	@Override
	public List<UserLogBean> selectByPage(Page page) {
		
		return dao.selectByPage(page);
	}

}