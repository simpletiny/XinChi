package com.xinchi.backend.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.user.dao.AssistantManagerDAO;
import com.xinchi.backend.user.service.AssistantManagerService;
import com.xinchi.bean.AssistantManagerBean;

@Service
@Transactional
public class AssistantManagerServiceImpl implements AssistantManagerService {

	@Autowired
	private AssistantManagerDAO dao;

	@Override
	public List<AssistantManagerBean> selectByParam(AssistantManagerBean option) {
		return dao.selectByParam(option);
	}

}