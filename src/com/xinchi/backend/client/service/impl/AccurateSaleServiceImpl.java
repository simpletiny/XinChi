package com.xinchi.backend.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.AccurateSaleDAO;
import com.xinchi.backend.client.service.AccurateSaleService;
import com.xinchi.bean.AccurateSaleBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AccurateSaleServiceImpl implements AccurateSaleService {

	@Autowired
	private AccurateSaleDAO dao;

	@Override
	public void insert(AccurateSaleBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(AccurateSaleBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public AccurateSaleBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<AccurateSaleBean> getAllByParam(AccurateSaleBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<AccurateSaleBean> selectByPage(Page page) {
		return dao.selectByPage(page);
	}

}