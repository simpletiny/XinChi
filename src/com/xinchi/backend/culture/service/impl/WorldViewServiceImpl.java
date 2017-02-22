package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.WorldViewDAO;
import com.xinchi.backend.culture.service.WorldViewService;
import com.xinchi.bean.WorldViewBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class WorldViewServiceImpl implements WorldViewService {

	@Autowired
	private WorldViewDAO dao;

	@Override
	public void insert(WorldViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<WorldViewBean> getAllViewsByPage(Page page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public WorldViewBean selectViewByPk(String view_pk) {
		
		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(WorldViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

}
