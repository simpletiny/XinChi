package com.xinchi.backend.userinfo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.userinfo.dao.UserInfoDAO;
import com.xinchi.backend.userinfo.service.UserInfoService;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private UserInfoDAO dao;

	@Override
	public void insert(com.xinchi.bean.UserInfoBean bo) {
		dao.insert(bo);
	}

	@Override
	public void update(com.xinchi.bean.UserInfoBean bo) {
		dao.update(bo);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public com.xinchi.bean.UserInfoBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<com.xinchi.bean.UserInfoBean> getAllByParam(com.xinchi.bean.UserInfoBean bo) {
		return dao.getAllByParam(bo);
	}

}