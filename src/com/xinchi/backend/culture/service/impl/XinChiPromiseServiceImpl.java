package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.XinChiPromiseDAO;
import com.xinchi.backend.culture.service.XinChiPromiseService;
import com.xinchi.bean.XinChiPromiseBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class XinChiPromiseServiceImpl implements XinChiPromiseService {

	@Autowired
	private XinChiPromiseDAO dao;

	@Override
	public void insert(XinChiPromiseBean view) {
		dao.insert(view);
	}

	@Override
	public List<XinChiPromiseBean> getAllViewsByPage(Page<XinChiPromiseBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public XinChiPromiseBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(XinChiPromiseBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
