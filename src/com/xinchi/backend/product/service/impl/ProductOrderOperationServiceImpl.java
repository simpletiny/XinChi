package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductOrderOperationDAO;
import com.xinchi.backend.product.service.ProductOrderOperationService;
import com.xinchi.bean.ProductOrderOperationBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ProductOrderOperationServiceImpl implements ProductOrderOperationService {

	@Autowired
	private ProductOrderOperationDAO dao;

	@Override
	public void insert(ProductOrderOperationBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(ProductOrderOperationBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ProductOrderOperationBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<ProductOrderOperationBean> selectByParam(ProductOrderOperationBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<ProductOrderOperationBean> selectByPage(Page<ProductOrderOperationBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<ProductOrderOperationBean> selectByTeamNumber(String team_number) {
		return dao.selectByTeamNumber(team_number);
	}

	@Override
	public void deleteByTeamNumber(String team_number) {
		dao.deleteByTeamNumber(team_number);
	}

}